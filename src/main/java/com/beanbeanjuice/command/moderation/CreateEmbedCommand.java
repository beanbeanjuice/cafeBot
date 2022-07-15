package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;

/**
 * An {@link ICommand} used to create custom {@link MessageEmbed}
 *
 * @author beanbeanjuice
 */
public class CreateEmbedCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // Checking if the provided channel is a text channel.
        TextChannel textChannel;
        try {
            textChannel = event.getOption("channel").getAsTextChannel();
        } catch (NullPointerException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Not A Text Channel",
                    "The channel you provided is not a valid text channel."
            )).queue();
            return;
        }

        // Checking if the provided attachments are images.
        if ((event.getOption("thumbnail") != null && !event.getOption("thumbnail").getAsAttachment().isImage()) ||
                (event.getOption("image") != null && !event.getOption("image").getAsAttachment().isImage())) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Attachments Must Be Images",
                    "The attachments you provided are not images and/or are not supported."
            )).queue();
            return;
        }

        try {
            if (event.getOption("message") != null) {
                textChannel.sendMessage(event.getOption("message").getAsString()).setEmbeds(createEmbed(event)).queue();
            } else {
                textChannel.sendMessageEmbeds(createEmbed(event)).queue();
            }

            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Created the Custom Message Embed",
                    "Successfully created the custom embed in " + textChannel.getAsMention() + "!"
            )).queue();
        } catch (IllegalStateException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Embed Cannot Be Empty",
                    "You just tried to create an empty embed!"
            )).queue();
        }

    }

    @NotNull
    private MessageEmbed createEmbed(@NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (event.getOption("title") != null) {
            embedBuilder.setTitle(event.getOption("title").getAsString());
        }

        if (event.getOption("author") != null) {
            embedBuilder.setAuthor(event.getOption("author").getAsString());
        }

        if (event.getOption("description") != null) {
            embedBuilder.setDescription(event.getOption("description").getAsString().replace("\\n", "\n"));
        }

        if (event.getOption("image") != null) {
            embedBuilder.setImage(event.getOption("image").getAsAttachment().getUrl());
        }

        if (event.getOption("thumbnail") != null) {
            embedBuilder.setThumbnail(event.getOption("thumbnail").getAsAttachment().getUrl());
        }

        if (event.getOption("footer") != null) {
            embedBuilder.setFooter(event.getOption("footer").getAsString());
        }

        if (event.getOption("color") != null) {
            try {
                embedBuilder.setColor(Color.decode(event.getOption("color").getAsString()));
            } catch (NumberFormatException ignored) {}
        }
        return embedBuilder.build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Create a customised embedded message!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/create-embed`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.CHANNEL, "channel", "The channel to send the embed in.", true, false));
        options.add(new OptionData(OptionType.STRING, "title", "Title for the embed.", false, false));
        options.add(new OptionData(OptionType.STRING, "description", "The message that goes INSIDE the embed.", false, false));
        options.add(new OptionData(OptionType.STRING, "author", "The author of the embed.", false, false));
        options.add(new OptionData(OptionType.STRING, "message", "The message that goes outside of the embed.", false, false));
        options.add(new OptionData(OptionType.STRING, "footer", "A message to add at the bottom of the embed.", false, false));
        options.add(new OptionData(OptionType.ATTACHMENT, "thumbnail", "A thumbnail url to add to the embed.", false, false));
        options.add(new OptionData(OptionType.ATTACHMENT, "image", "An image url to add to the image.", false, false));
        options.add(new OptionData(OptionType.STRING, "color", "Color hex code. Example: #FFC0CB", false, false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

    @Nullable
    @Override
    public ArrayList<Permission> getPermissions() {
        ArrayList <Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MANAGE_SERVER);
        return permissions;
    }

}
