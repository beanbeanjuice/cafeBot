package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.command.moderation.welcome.EditWelcomeMessageCommand;
import com.beanbeanjuice.utility.logger.LogLevel;
import io.github.beanbeanjuice.cafeapi.cafebot.welcomes.GuildWelcome;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A listener for when someone joins the server.
 *
 * @author beanbeanjuice
 */
public class WelcomeListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        TextChannel welcomeChannel = CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getWelcomeChannel();
        if (welcomeChannel != null) {
            GuildWelcome guildWelcome = getGuildWelcome(event.getGuild().getId());
            if (guildWelcome.getMessage() != null) {
                welcomeChannel.sendMessage(guildWelcome.getMessage()).setEmbeds(getWelcomeEmbed(guildWelcome, event.getMember().getUser())).queue();
            } else {
                welcomeChannel.sendMessageEmbeds(getWelcomeEmbed(guildWelcome, event.getMember().getUser())).queue();
            }
        }
    }

    /**
     * Parses the {@link String description} of the {@link GuildWelcome}.
     * @param description The {@link String description} to parse.
     * @param joiner The {@link User} who joined.
     * @return The parsed {@link String description}.
     */
    @NotNull
    private String parseDescription(@Nullable String description, @NotNull User joiner) {
        if (description == null) {
            description = "Welcome to the server {user}!";
        }

        description = description.replace("{user}", joiner.getAsMention());
        description = description.replace("\\n", "\n");
        return description;
    }

    /**
     * Retrieves the {@link GuildWelcome} {@link MessageEmbed} to send in the welcome {@link TextChannel}.
     * @param guildWelcome The {@link GuildWelcome} specified.
     * @param joiner The {@link User} who joined.
     * @return The completed {@link MessageEmbed}.
     */
    @NotNull
    public MessageEmbed getWelcomeEmbed(@NotNull GuildWelcome guildWelcome, @NotNull User joiner) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setDescription(parseDescription(guildWelcome.getDescription(), joiner));

        // Attempts to set the thumbnail URL.
        try {
            embedBuilder.setThumbnail(guildWelcome.getThumbnailURL());
        } catch (IllegalArgumentException e) {
            CafeBot.getGuildHandler().getCustomGuild(guildWelcome.getGuildID()).log(new EditWelcomeMessageCommand(), LogLevel.ERROR,
                    "Invalid Thumbnail URL", "Invalid thumbnail URL for Welcome: " + guildWelcome.getThumbnailURL());
        }

        // Attempts to set the image URL.
        try {
            embedBuilder.setImage(guildWelcome.getImageURL());
        } catch (IllegalArgumentException e) {
            CafeBot.getGuildHandler().getCustomGuild(guildWelcome.getGuildID()).log(new EditWelcomeMessageCommand(), LogLevel.ERROR,
                    "Invalid Image URL", "Invalid image URL for Welcome: " + guildWelcome.getImageURL());
        }
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setAuthor(joiner.getAsTag(), joiner.getAvatarUrl(), joiner.getAvatarUrl());
        return embedBuilder.build();
    }

    /**
     * Retrieves the {@link GuildWelcome} from the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} for the {@link GuildWelcome}.
     * @return The {@link GuildWelcome}.
     */
    @NotNull
    public GuildWelcome getGuildWelcome(@NotNull String guildID) {
        try {
            GuildWelcome guildWelcome = CafeBot.getCafeAPI().welcomes().getGuildWelcome(guildID);
            return new GuildWelcome(
                    guildWelcome.getGuildID(),
                    guildWelcome.getDescription(),
                    guildWelcome.getThumbnailURL(),
                    guildWelcome.getImageURL(),
                    guildWelcome.getMessage());
        } catch (NotFoundException e) {
            return new GuildWelcome(guildID, null, null, null, null);
        }
    }

}
