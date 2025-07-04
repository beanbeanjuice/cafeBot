package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.goodbyes.GuildGoodbye;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformation;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.CompletableFuture;

public class GoodbyeListener extends ListenerAdapter {

    private final CafeBot cafeBot;

    public GoodbyeListener(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        String guildID = event.getGuild().getId();

        CompletableFuture<GuildInformation> informationFuture = cafeBot.getCafeAPI().getGuildsEndpoint().getGuildInformation(guildID);
        CompletableFuture<GuildGoodbye> goodbyeFuture = cafeBot.getCafeAPI().getGoodbyesEndpoint().getGuildGoodbye(guildID);

        informationFuture.thenCombineAsync(goodbyeFuture, (information, goodbye) -> {
            if (information.getSetting(GuildInformationType.WELCOME_CHANNEL_ID).equals("0")) return false;

            TextChannel channel = event.getGuild().getTextChannelById(information.getSetting(GuildInformationType.GOODBYE_CHANNEL_ID));
            if (channel == null) return false;

            goodbye.getMessage().ifPresentOrElse(
                    (message) -> channel.sendMessage(message.replace("{user}", event.getUser().getAsMention())).addEmbeds(getGoodbyeEmbed(goodbye, event.getUser())).queue(),
                    () -> channel.sendMessageEmbeds(getGoodbyeEmbed(goodbye, event.getUser())).queue()
            );

            return true;
        });
    }

    public static MessageEmbed getGoodbyeEmbed(final GuildGoodbye guildGoodbye, final User joiner) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        guildGoodbye.getDescription().ifPresent((description) -> embedBuilder.setDescription(parseDescription(description, joiner)));

        // Attempts to set the thumbnail URL.
        guildGoodbye.getThumbnailURL().ifPresent((thumbnailURL) -> {
            try { embedBuilder.setThumbnail(thumbnailURL); }
            catch (IllegalArgumentException ignored) { }
        });

        // Attempts to set the image URL.
        guildGoodbye.getImageURL().ifPresent((imageURL) -> {
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
