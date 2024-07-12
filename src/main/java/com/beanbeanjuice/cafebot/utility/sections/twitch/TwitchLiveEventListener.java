package com.beanbeanjuice.cafebot.utility.sections.twitch;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformation;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import com.github.twitch4j.helix.domain.GameList;
import com.github.twitch4j.helix.domain.UserList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TwitchLiveEventListener extends SimpleEventHandler {

    private final TwitchClient twitchClient;
    private final CafeBot cafeBot;

    public TwitchLiveEventListener(final TwitchClient twitchClient, final CafeBot cafeBot) {
        this.twitchClient = twitchClient;
        this.cafeBot = cafeBot;
    }

    @EventSubscriber
    public void onChannelGoLive(final ChannelGoLiveEvent event) {
        // Converts the Twitch Name to lower case.
        String twitchID = event.getChannel().getId();
        String gameID = event.getStream().getGameId();

        cafeBot.getLogger().log(TwitchLiveEventListener.class, LogLevel.DEBUG, "LIVE: " + event.getChannel().getName());

        CompletableFuture<UserList> userFuture = CompletableFuture.supplyAsync(() -> {
            try { return twitchClient.getHelix().getUsers(null, List.of(twitchID), null).queue().get(); }
            catch (InterruptedException | ExecutionException e) { throw new RuntimeException(e); }
        });

        cafeBot.getLogger().log(TwitchLiveEventListener.class, LogLevel.DEBUG, "GAME ID: " + gameID);
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

    // TODO: Add offline edit.
    @EventSubscriber
    public void onChannelGoOffline(final ChannelGoOfflineEvent event) { }

    private void handleLiveEvent(final ChannelGoLiveEvent event, final String profileImageURL, @Nullable final String boxArtURL) {
        String twitchName = event.getChannel().getName().toLowerCase();

        cafeBot.getCafeAPI().getTwitchEndpoint().getAllTwitches().thenApplyAsync(HashMap::entrySet).thenAcceptAsync((entries) -> {
            entries.stream().filter((entry) -> entry.getValue().contains(twitchName)).forEach((entry) -> {
                String guildID = entry.getKey();

                cafeBot.getCafeAPI().getGuildsEndpoint().getGuildInformation(guildID).thenAcceptAsync((information) -> {
                    handleMessageToServers(event, profileImageURL, boxArtURL, twitchName, guildID, information);
                });
            });
        });
    }

    private void handleMessageToServers(final ChannelGoLiveEvent event, final String profileImageURL, @Nullable final String boxArtURL, final String twitchName, final String guildID, final GuildInformation information) {
        Guild guild = cafeBot.getJDA().getGuildById(guildID);
        if (guild ==  null) return;

        TextChannel channel = guild.getTextChannelById(information.getSetting(GuildInformationType.TWITCH_CHANNEL_ID));
        if (channel == null) return;

        Optional<Role> role = Optional.ofNullable(guild.getRoleById(information.getSetting(GuildInformationType.LIVE_NOTIFICATIONS_ROLE_ID)));
        String liveMessage = String.format("**%s** is live!", twitchName);
        if (role.isPresent()) liveMessage += String.format(" %s", role.get().getAsMention());

        channel.sendMessage(liveMessage).addEmbeds(liveEmbed(event, profileImageURL, boxArtURL, twitchName)).queue();
        cafeBot.increaseCommandsRun();
    }

    public MessageEmbed liveEmbed(final ChannelGoLiveEvent event, final String profileImageURL, @Nullable final String boxArtURL, final String twitchName) {
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
