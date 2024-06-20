package com.beanbeanjuice.cafebot.utility.listener;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafebot.command.settings.welcome.EditWelcomeMessageSubCommand;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.wrapper.cafebot.welcomes.GuildWelcome;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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

            guildWelcome.getMessage().ifPresentOrElse(
                    (guildWelcomeMessage) -> welcomeChannel.sendMessage(guildWelcomeMessage).setEmbeds(getWelcomeEmbed(guildWelcome, event.getMember().getUser())).queue(),
                    () -> welcomeChannel.sendMessageEmbeds(getWelcomeEmbed(guildWelcome, event.getMember().getUser())).queue()
            );

            Bot.commandsRun++;
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

        guildWelcome.getDescription().ifPresent((description) -> embedBuilder.setDescription(parseDescription(description, joiner)));

        // Attempts to set the thumbnail URL.
        guildWelcome.getThumbnailURL().ifPresent((thumbnailURL) -> {
            try {
                embedBuilder.setThumbnail(thumbnailURL);
            } catch (IllegalArgumentException e) {
                GuildHandler.getCustomGuild(guildWelcome.getGuildID()).log(new EditWelcomeMessageSubCommand(), LogLevel.ERROR,
                        "Invalid Thumbnail URL", "Invalid thumbnail URL for Welcome: " + guildWelcome.getThumbnailURL());
            }
        });

        // Attempts to set the image URL.
        guildWelcome.getImageURL().ifPresent((imageURL) -> {
            try {
                embedBuilder.setImage(imageURL);
            } catch (IllegalArgumentException e) {
                GuildHandler.getCustomGuild(guildWelcome.getGuildID()).log(new EditWelcomeMessageSubCommand(), LogLevel.ERROR,
                        "Invalid Image URL", "Invalid image URL for Welcome: " + guildWelcome.getImageURL());
            }
        });

        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setAuthor(joiner.getName(), joiner.getAvatarUrl(), joiner.getAvatarUrl());
        return embedBuilder.build();
    }

    /**
     * Retrieves the {@link GuildWelcome} from the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} for the {@link GuildWelcome}.
     * @return The {@link GuildWelcome}.
     */
    @NotNull
    public static GuildWelcome getGuildWelcome(@NotNull String guildID) {
        try {
            GuildWelcome guildWelcome = Bot.getCafeAPI().WELCOME.getGuildWelcome(guildID);
            return new GuildWelcome(
                    guildWelcome.getGuildID(),
                    guildWelcome.getDescription().orElse(null),
                    guildWelcome.getThumbnailURL().orElse(null),
                    guildWelcome.getImageURL().orElse(null),
                    guildWelcome.getMessage().orElse(null)
            );
        } catch (NotFoundException e) {
            return new GuildWelcome(guildID, null, null, null, null);
        }
    }

}
