package com.beanbeanjuice.command.moderation.poll;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.section.moderation.poll.Poll;
import com.beanbeanjuice.utility.section.moderation.poll.PollEmoji;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.section.moderation.poll.PollHandler;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * An {@link ICommand} used to create a {@link io.github.beanbeanjuice.cafeapi.cafebot.polls.Poll Poll}.
 *
 * @author beanbeanjuice
 */
public class AddPollCommand implements ICommand {

    private final int MAX_POLLS = 3;

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // Check if the poll channel exists.
        if (Bot.getGuildHandler().getCustomGuild(event.getGuild()).getPollChannel() == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "No Poll Channel",
                    "It seems you do not have a poll channel set! " +
                            "Do /poll-channel set to set the poll channel."
            )).queue();
            return;
        }

        // Makes sure that guilds only have 3 polls.
        if (Bot.getCafeAPI().POLL.getGuildPolls(event.getGuild().getId()).size() >= MAX_POLLS) {
            event.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                    "Too Many Polls",
                    "You can currently only have a total of " +
                            "3 polls per Discord Server. This is due to server costs."
            )).queue();
            return;
        }

        // Required items
        int minutes = event.getOption("time").getAsInt();
        String title = event.getOption("title").getAsString();
        String description = event.getOption("description").getAsString();
        ArrayList<String> arguments = convertToList(event.getOption("poll_options").getAsString());

        // Making sure the poll channel exists.
        TextChannel pollChannel = Bot.getGuildHandler().getCustomGuild(event.getGuild()).getPollChannel();

        // Making sure there are less than 20 arguments.
        if (arguments.size() > 20) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Too Many Items",
                    "You can only have 20 items. Please try again. Discord only supports 20 message reactions."
            )).queue();
            return;
        }

        Timestamp timestamp = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis() + (minutes*60000)).toString());

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Creating Poll",
                "I'm creating your poll right now!"
        )).queue();

        // Sending a message in the poll channel.
        if (event.getOption("message") != null) {
            pollChannel.sendMessage(event.getOption("message").getAsString()).setEmbeds(startingPollEmbed()).queue(message -> {
                editMessage(message, event, timestamp, title, description, minutes, arguments);
            });
        } else {
            pollChannel.sendMessageEmbeds(startingPollEmbed()).queue(message -> {
                editMessage(message, event, timestamp, title, description, minutes, arguments);
            });
        }
    }

    /**
     * Edits the specified {@link Message}.
     * @param message The {@link Message} to edit.
     * @param event The {@link SlashCommandInteractionEvent event} that triggered the {@link Message}.
     * @param timestamp The {@link Timestamp} to end the {@link }.
     * @param title The {@link String title} of the {@link Poll}.
     * @param description The {@link String description} of the {@link Poll}.
     * @param minutes The length in {@link Integer minutes} of the {@link Poll}.
     * @param arguments The {@link ArrayList} of {@link String argument}.
     */
    private void editMessage(Message message, SlashCommandInteractionEvent event, Timestamp timestamp,
                             String title, String description, Integer minutes, ArrayList<String> arguments) {
        Poll poll = new Poll(message.getId(), timestamp);

        // Tries to create the poll.
        if (!PollHandler.addPoll(event.getGuild().getId(), poll)) {
            // If it can't, say why.
            message.delete().queue();
            event.getHook().editOriginalEmbeds(Helper.sqlServerError()).queue();
            return;
        }

        message.editMessageEmbeds(pollEmbed(title, description, minutes, arguments, event)).queue();

        ArrayList<PollEmoji> pollEmojis = new ArrayList<>(Arrays.asList(PollEmoji.values()));
        for (int i = 0; i < arguments.size(); i++) {
            message.addReaction(Emoji.fromFormatted(pollEmojis.get(i).getUnicode())).queue();
        }

        event.getHook().editOriginalEmbeds(Helper.successEmbed(
                "Poll Created",
                "A poll has been successfully created! Check the " + message.getTextChannel().getAsMention() + " channel."
        )).queue();
    }

    /**
     * @return The {@link MessageEmbed} to send when starting a {@link Poll}.
     */
    @NotNull
    private MessageEmbed startingPollEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Setting Up Polls...");
        embedBuilder.setDescription("The poll is currently being setup. Please hold on.");
        embedBuilder.setColor(Color.orange);
        return embedBuilder.build();
    }

    /**
     * The {@link MessageEmbed} to change to when the {@link Poll} has been created.
     * @param pollTitle The {@link String title} of the {@link Poll}.
     * @param pollDescription The {@link String description} of the {@link Poll}.
     * @param pollTime The {@link Integer length} of the {@link Poll} in minutes.
     * @param arguments The {@link ArrayList} of {@link String argument} for the {@link Poll}.
     * @param event The {@link SlashCommandInteractionEvent event} that ran the command.
     * @return The created {@link MessageEmbed} for the {@link Poll}.
     */
    @NotNull
    private MessageEmbed pollEmbed(@NotNull String pollTitle, @NotNull String pollDescription,
                                   @NotNull Integer pollTime, @NotNull ArrayList<String> arguments,
                                   @NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(pollTitle);

        ArrayList<PollEmoji> pollEmojis = new ArrayList<>(Arrays.asList(PollEmoji.values()));
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < arguments.size(); i++) {
            stringBuilder.append(pollEmojis.get(i).getDiscordString()).append(" - ")
                    .append(arguments.get(i)).append("\n");
        }

        stringBuilder.append("\n").append(pollDescription);
        embedBuilder.setDescription(stringBuilder.toString());

        if (pollTime == 1) {
            embedBuilder.setFooter("This poll will end in " + pollTime + " minute from when it was created.");
        } else {
            embedBuilder.setFooter("This poll will end in " + pollTime + " minutes from when it was created.");
        }

        // Author
        if (event.getOption("author") != null)
            embedBuilder.setAuthor(event.getOption("author").getAsString());

        // Thumbnail
        if (event.getOption("thumbnail") != null && event.getOption("thumbnail").getAsAttachment().isImage())
            embedBuilder.setThumbnail(event.getOption("thumbnail").getAsAttachment().getUrl());

        // Image
        if (event.getOption("image") != null && event.getOption("image").getAsAttachment().isImage())
            embedBuilder.setImage(event.getOption("image").getAsAttachment().getUrl());

        // Color
        if (event.getOption("color") != null) {
            try {
                embedBuilder.setColor(Color.decode(event.getOption("color").getAsString()));
            } catch (NumberFormatException ignored) {}
        }

        return embedBuilder.build();
    }

    /**
     * Converts a {@link String} with commas into an {@link ArrayList} of {@link String}.
     * @param string The {@link String} with commas.
     * @return The {@link ArrayList} of separated {@link String}.
     */
    @NotNull
    private ArrayList<String> convertToList(@NotNull String string) {
        ArrayList<String> arrayList;
        try {
            arrayList = new ArrayList<>(Arrays.asList(string.split(",")));
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
        return arrayList;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Create a poll in a specified channel.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "Just do `/add-poll` :sob: it shows you how to use it.";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "time", "The time for the poll to run, in minutes.", true, false)
                .setMinValue(1));
        options.add(new OptionData(OptionType.STRING, "title", "Title for the poll.", true, false));
        options.add(new OptionData(OptionType.STRING, "description", "The message that goes INSIDE the poll.", true, false));
        options.add(new OptionData(OptionType.STRING, "poll_options", "All of the poll options, using comma-separated value", true, false));
        options.add(new OptionData(OptionType.STRING, "author", "The author of the poll.", false, false));
        options.add(new OptionData(OptionType.STRING, "message", "The message that goes outside of the poll.", false, false));
        options.add(new OptionData(OptionType.ATTACHMENT, "thumbnail", "A thumbnail url to add to the poll.", false, false));
        options.add(new OptionData(OptionType.ATTACHMENT, "image", "An image url to add to the poll.", false, false));
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
    public Boolean allowDM() {
        return false;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

    @Nullable
    @Override
    public ArrayList<Permission> getPermissions() {
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MANAGE_SERVER);
        return permissions;
    }
}
