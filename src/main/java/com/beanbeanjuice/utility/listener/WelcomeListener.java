package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.command.settings.welcome.EditWelcomeMessageSubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
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
 * A {@link ListenerAdapter} for when someone joins the server.
 *
 * @author beanbeanjuice
 */
public class WelcomeListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        TextChannel welcomeChannel = GuildHandler.getCustomGuild(event.getGuild()).getWelcomeChannel();
        if (welcomeChannel != null) {
            GuildWelcome guildWelcome = getGuildWelcome(event.getGuild().getId());

            if (guildWelcome.getMessage() != null)
                welcomeChannel.sendMessage(guildWelcome.getMessage()).setEmbeds(getWelcomeEmbed(guildWelcome, event.getMember().getUser())).queue();
            else
                welcomeChannel.sendMessageEmbeds(getWelcomeEmbed(guildWelcome, event.getMember().getUser())).queue();
        }
    }

    /**
     * Parses the {@link String description} of the {@link GuildWelcome}.
     * @param description The {@link String description} to parse.
     * @param joiner The {@link User} who joined.
     * @return The parsed {@link String description}.
     */
    @NotNull
    private static String parseDescription(@Nullable String description, @NotNull User joiner) {
        if (description == null)
            description = "Welcome to the server {user}!";

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
    public static MessageEmbed getWelcomeEmbed(@NotNull GuildWelcome guildWelcome, @NotNull User joiner) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setDescription(parseDescription(guildWelcome.getDescription(), joiner));

        // Attempts to set the thumbnail URL.
        try {
            embedBuilder.setThumbnail(guildWelcome.getThumbnailURL());
        } catch (IllegalArgumentException e) {
            GuildHandler.getCustomGuild(guildWelcome.getGuildID()).log(new EditWelcomeMessageSubCommand(), LogLevel.ERROR,
                    "Invalid Thumbnail URL", "Invalid thumbnail URL for Welcome: " + guildWelcome.getThumbnailURL());
        }

        // Attempts to set the image URL.
        try {
            embedBuilder.setImage(guildWelcome.getImageURL());
        } catch (IllegalArgumentException e) {
            GuildHandler.getCustomGuild(guildWelcome.getGuildID()).log(new EditWelcomeMessageSubCommand(), LogLevel.ERROR,
                    "Invalid Image URL", "Invalid image URL for Welcome: " + guildWelcome.getImageURL());
        }
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setAuthor(joiner.getAsTag(), joiner.getAvatarUrl(), joiner.getAvatarUrl());
        return embedBuilder.build();
    }

    /**
     * Retrieves the {@link GuildWelcome} from the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} for the {@link GuildWelcome}.
     * @return The {@link GuildWelcome}.
     */
    @NotNull
    public static GuildWelcome getGuildWelcome(@NotNull String guildID) {
        try {
            GuildWelcome guildWelcome = Bot.getCafeAPI().WELCOME.getGuildWelcome(guildID);
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
