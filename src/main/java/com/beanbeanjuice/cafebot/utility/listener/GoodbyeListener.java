package com.beanbeanjuice.cafebot.utility.listener;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.goodbyes.GuildGoodbye;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafebot.command.settings.goodbye.EditGoodbyeMessageSubCommand;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoodbyeListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        TextChannel goodbyeChannel = GuildHandler.getCustomGuild(event.getGuild()).getGoodbyeChannel();
        if (goodbyeChannel != null) {
            GuildGoodbye guildGoodbye = getGuildGoodbye(event.getGuild().getId());

            guildGoodbye.getMessage().ifPresentOrElse(
                    (guildGoodbyeMessage) -> goodbyeChannel.sendMessage(guildGoodbyeMessage).setEmbeds(getGoodbyeEmbed(guildGoodbye, event.getMember().getUser())).queue(),
                    () -> goodbyeChannel.sendMessageEmbeds(getGoodbyeEmbed(guildGoodbye, event.getMember().getUser())).queue()
            );

            Bot.commandsRun++;
        }
    }

    /**
     * Parses the {@link String description} of the {@link GuildGoodbye}.
     * @param description The {@link String description} to parse.
     * @param joiner The {@link User} who joined.
     * @return The parsed {@link String description}.
     */
    @NotNull
    private static String parseDescription(@Nullable String description, @NotNull User joiner) {
        if (description == null)
            description = "Goodbye... {user}!";

        description = description.replace("{user}", joiner.getAsMention());
        description = description.replace("\\n", "\n");
        return description;
    }

    /**
     * Retrieves the {@link GuildGoodbye} {@link MessageEmbed} to send in the goodbye {@link TextChannel}.
     * @param guildGoodbye The {@link GuildGoodbye} specified.
     * @param joiner The {@link User} who joined.
     * @return The completed {@link MessageEmbed}.
     */
    @NotNull
    public static MessageEmbed getGoodbyeEmbed(@NotNull GuildGoodbye guildGoodbye, @NotNull User joiner) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        guildGoodbye.getDescription()
                .ifPresent((description) -> embedBuilder.setDescription(parseDescription(description, joiner)));

        // Attempts to set the thumbnail URL.
        guildGoodbye.getThumbnailURL().ifPresent((thumbnailURL) -> {
            try {
                embedBuilder.setThumbnail(thumbnailURL);
            } catch (IllegalArgumentException e) {
                GuildHandler.getCustomGuild(guildGoodbye.getGuildID()).log(new EditGoodbyeMessageSubCommand(), LogLevel.ERROR,
                        "Invalid Thumbnail URL", "Invalid thumbnail URL for Goodbye: " + guildGoodbye.getThumbnailURL());
            }
        });

        // Attempts to set the image URL.
        guildGoodbye.getImageURL().ifPresent((imageURL) -> {
            try {
                embedBuilder.setImage(imageURL);
            } catch (IllegalArgumentException e) {
                GuildHandler.getCustomGuild(guildGoodbye.getGuildID()).log(new EditGoodbyeMessageSubCommand(), LogLevel.ERROR,
                        "Invalid Image URL", "Invalid image URL for Goodbye: " + guildGoodbye.getImageURL());
            }
        });

        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setAuthor(joiner.getName(), joiner.getAvatarUrl(), joiner.getAvatarUrl());
        return embedBuilder.build();
    }

    /**
     * Retrieves the {@link GuildGoodbye} from the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} for the {@link GuildGoodbye}.
     * @return The {@link GuildGoodbye}.
     */
    @NotNull
    public static GuildGoodbye getGuildGoodbye(@NotNull String guildID) {
        try {
            GuildGoodbye guildGoodbye = Bot.getCafeAPI().GOODBYE.getGuildGoodbye(guildID);
            return new GuildGoodbye(
                    guildGoodbye.getGuildID(),
                    guildGoodbye.getDescription().orElse(null),
                    guildGoodbye.getThumbnailURL().orElse(null),
                    guildGoodbye.getImageURL().orElse(null),
                    guildGoodbye.getMessage().orElse(null)
            );
        } catch (NotFoundException e) {
            return new GuildGoodbye(guildID, null, null, null, null);
        }
    }

}
