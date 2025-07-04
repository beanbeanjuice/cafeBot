package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformation;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.welcomes.GuildWelcome;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.CompletableFuture;

public class WelcomeListener extends ListenerAdapter {

    private final CafeBot cafeBot;

    public WelcomeListener(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String guildID = event.getGuild().getId();

        CompletableFuture<GuildInformation> informationFuture = cafeBot.getCafeAPI().getGuildsEndpoint().getGuildInformation(guildID);
        CompletableFuture<GuildWelcome> welcomeFuture = cafeBot.getCafeAPI().getWelcomesEndpoint().getGuildWelcome(guildID);

        informationFuture.thenCombineAsync(welcomeFuture, (information, welcome) -> {
            if (information.getSetting(GuildInformationType.WELCOME_CHANNEL_ID).equals("0")) return false;

            TextChannel channel = event.getGuild().getTextChannelById(information.getSetting(GuildInformationType.WELCOME_CHANNEL_ID));
            if (channel == null) return false;

            welcome.getMessage().ifPresentOrElse(
                    (message) -> channel.sendMessage(message.replace("{user}", event.getUser().getAsMention())).addEmbeds(getWelcomeEmbed(welcome, event.getUser())).queue(),
                    () -> channel.sendMessageEmbeds(getWelcomeEmbed(welcome, event.getUser())).queue()
            );

            return true;
        });
    }

    public static MessageEmbed getWelcomeEmbed(final GuildWelcome guildWelcome, final User joiner) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        guildWelcome.getDescription().ifPresent((description) -> embedBuilder.setDescription(parseDescription(description, joiner)));

        // Attempts to set the thumbnail URL.
        guildWelcome.getThumbnailURL().ifPresent((thumbnailURL) -> {
            try { embedBuilder.setThumbnail(thumbnailURL); }
            catch (IllegalArgumentException ignored) { }
        });

        // Attempts to set the image URL.
        guildWelcome.getImageURL().ifPresent((imageURL) -> {
            try { embedBuilder.setImage(imageURL); }
            catch (IllegalArgumentException ignored) { }
        });

        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setAuthor(joiner.getName(), joiner.getAvatarUrl(), joiner.getAvatarUrl());
        return embedBuilder.build();
    }

    private static String parseDescription(String description, final User joiner) {
        description = description.replace("{user}", joiner.getAsMention());
        description = description.replace("\\n", "\n");
        return description;
    }

}
