package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.cafeapi.cafebot.goodbyes.GuildGoodbye;
import com.beanbeanjuice.cafeapi.exception.api.NotFoundException;
import com.beanbeanjuice.command.settings.goodbye.EditGoodbyeMessageSubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
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

            if (guildGoodbye.getMessage() != null)
                goodbyeChannel.sendMessage(guildGoodbye.getMessage()).setEmbeds(getGoodbyeEmbed(guildGoodbye, event.getMember().getUser())).queue();
            else
                goodbyeChannel.sendMessageEmbeds(getGoodbyeEmbed(guildGoodbye, event.getMember().getUser())).queue();

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

        embedBuilder.setDescription(parseDescription(guildGoodbye.getDescription(), joiner));

        // Attempts to set the thumbnail URL.
        try {
            embedBuilder.setThumbnail(guildGoodbye.getThumbnailURL());
        } catch (IllegalArgumentException e) {
            GuildHandler.getCustomGuild(guildGoodbye.getGuildID()).log(new EditGoodbyeMessageSubCommand(), LogLevel.ERROR,
                    "Invalid Thumbnail URL", "Invalid thumbnail URL for Goodbye: " + guildGoodbye.getThumbnailURL());
        }

        // Attempts to set the image URL.
        try {
            embedBuilder.setImage(guildGoodbye.getImageURL());
        } catch (IllegalArgumentException e) {
            GuildHandler.getCustomGuild(guildGoodbye.getGuildID()).log(new EditGoodbyeMessageSubCommand(), LogLevel.ERROR,
                    "Invalid Image URL", "Invalid image URL for Goodbye: " + guildGoodbye.getImageURL());
        }
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setAuthor(joiner.getAsTag(), joiner.getAvatarUrl(), joiner.getAvatarUrl());
        return embedBuilder.build();
    }

    /**
     * Retrieves the {@link GuildGoodbye} from the {@link com.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} for the {@link GuildGoodbye}.
     * @return The {@link GuildGoodbye}.
     */
    @NotNull
    public static GuildGoodbye getGuildGoodbye(@NotNull String guildID) {
        try {
            GuildGoodbye guildGoodbye = Bot.getCafeAPI().GOODBYE.getGuildGoodbye(guildID);
            return new GuildGoodbye(
                    guildGoodbye.getGuildID(),
                    guildGoodbye.getDescription(),
                    guildGoodbye.getThumbnailURL(),
                    guildGoodbye.getImageURL(),
                    guildGoodbye.getMessage());
        } catch (NotFoundException e) {
            return new GuildGoodbye(guildID, null, null, null, null);
        }
    }

}
