package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomRoleType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.helix.domain.GameList;
import com.github.twitch4j.helix.domain.UserList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TwitchGoLiveEventListener extends SimpleEventHandler {

    private final TwitchClient twitchClient;
    private final CafeBot cafeBot;

    public TwitchGoLiveEventListener(final TwitchClient twitchClient, final CafeBot cafeBot) {
        this.twitchClient = twitchClient;
        this.cafeBot = cafeBot;
    }

    @EventSubscriber
    public void onChannelGoLive(final ChannelGoLiveEvent event) {
        // Converts the Twitch Name to lower case.
        String twitchID = event.getChannel().getId();
        String gameID = event.getStream().getGameId();

        cafeBot.getLogger().log(TwitchGoLiveEventListener.class, LogLevel.DEBUG, "LIVE: " + event.getChannel().getName());

        CompletableFuture<UserList> userFuture = CompletableFuture.supplyAsync(() -> {
            try { return twitchClient.getHelix().getUsers(null, List.of(twitchID), null).queue().get(); }
            catch (InterruptedException | ExecutionException e) { throw new RuntimeException(e); }
        });

        cafeBot.getLogger().log(TwitchGoLiveEventListener.class, LogLevel.DEBUG, "GAME ID: " + gameID);
        if (gameID == null || gameID.isBlank()) {
            userFuture.thenAcceptAsync((users) -> {
                handleLiveEvent(event, users.getUsers().getFirst().getProfileImageUrl(), null);
            });
            return;
        }

        CompletableFuture<GameList> gameFuture = CompletableFuture.supplyAsync(() -> {
            try { return twitchClient.getHelix().getGames(null, List.of(gameID), null, null).queue().get(); }
            catch (InterruptedException | ExecutionException e) { throw new RuntimeException(e); }
        });

        userFuture.thenCombineAsync(gameFuture, (users, games) -> {
            handleLiveEvent(event, users.getUsers().getFirst().getProfileImageUrl(), games.getGames().getFirst().getBoxArtUrl());
            return true;
        });
    }

    private void handleLiveEvent(ChannelGoLiveEvent event, String profileImageURL, @Nullable String boxArtURL) {
        String twitchName = event.getChannel().getName().toLowerCase();

        cafeBot.getCafeAPI().getTwitchChannelApi().getGuilds(twitchName).thenAccept((guildIds) -> {
            guildIds.forEach((guildId) -> {
                cafeBot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildId, CustomChannelType.TWITCH_NOTIFICATIONS).thenAccept((customChannel) -> {
                    handleMessageToServers(event, profileImageURL, boxArtURL, twitchName, guildId, customChannel.getChannelId());
                });
            });
        });
    }

    private void handleMessageToServers(ChannelGoLiveEvent event, String profileImageURL, @Nullable String boxArtURL, String twitchName, String guildId, String channelId) {
        Guild guild = cafeBot.getShardManager().getGuildById(guildId);
        if (guild ==  null) return;

        TextChannel channel = guild.getTextChannelById(channelId);
        if (channel == null) return;

        cafeBot.getCafeAPI().getCustomRoleApi().getCustomRole(guildId, CustomRoleType.TWITCH_NOTIFICATIONS)
                .thenAccept((customRole) -> {
                    Optional<Role> role = Optional.ofNullable(guild.getRoleById(customRole.getRoleId()));
                    String liveMessage = String.format("**%s** is live!", twitchName);
                    if (role.isPresent()) liveMessage += String.format(" %s", role.get().getAsMention());

                    channel.sendMessage(liveMessage).addEmbeds(liveEmbed(event, profileImageURL, boxArtURL, twitchName)).queue();
                })
                .exceptionally((ex) -> {
                    String liveMessage = String.format("**%s** is live!", twitchName);
                    channel.sendMessage(liveMessage).addEmbeds(liveEmbed(event, profileImageURL, boxArtURL, twitchName)).queue();
                    return null;
                });
        cafeBot.increaseCommandsRun();
    }

    public MessageEmbed liveEmbed(ChannelGoLiveEvent event, String profileImageURL, @Nullable String boxArtURL, String twitchName) {
        String link = String.format("https://www.twitch.tv/%s", twitchName);
        String imageURL = event.getStream().getThumbnailUrl(1920, 1080);

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder
                .setColor(Color.pink)
                .setAuthor(event.getChannel().getName(), null, profileImageURL)
                .setTitle(event.getStream().getTitle(), link)
                .setImage(imageURL)
                .addField("Game", event.getStream().getGameName(), true)
                .addField("Viewers", String.valueOf(event.getStream().getViewerCount()), true)
                .setFooter("Live information brought to you by cafeBot!");

        if (boxArtURL != null) embedBuilder.setThumbnail(boxArtURL);

        return embedBuilder.build();
    }

}
