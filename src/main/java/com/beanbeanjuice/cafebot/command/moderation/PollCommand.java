package com.beanbeanjuice.cafebot.command.moderation;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafeapi.wrapper.generic.CafeGeneric;
import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.CommandType;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.section.moderation.poll.Poll;
import com.beanbeanjuice.cafebot.utility.section.moderation.poll.PollEmoji;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.section.moderation.poll.PollHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * An {@link ICommand} used to create a {@link com.beanbeanjuice.cafeapi.wrapper.endpoints.polls.Poll Poll}.
 *
 * @author beanbeanjuice
 */
public class PollCommand implements ICommand {

    private final int MAX_POLLS = 3;

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // Check if the poll channel exists.
        if (GuildHandler.getCustomGuild(event.getGuild()).getPollChannel() == null) {
            event.replyEmbeds(Helper.errorEmbed(
                    "No Poll Channel",
                    "It seems you do not have a poll channel set! You must have a dedicated poll channel. " +
                            "Do /poll-channel set to set the poll channel."
            )).setEphemeral(true).queue();
            return;
        }

        // Makes sure that guilds only have 3 polls.
        if (Bot.getCafeAPI().POLL.getGuildPolls(event.getGuild().getId()).size() >= MAX_POLLS) {
            event.replyEmbeds(Helper.errorEmbed(
                    "Too Many Polls",
                    "You can currently only have a total of " +
                            "3 polls per Discord Server. This is due to server costs."
            )).setEphemeral(true).queue();
            return;
        }

        Modal.Builder modalBuilder = Modal.create("poll-modal", "Create a Poll");
        getModalOptions().forEach((option) -> modalBuilder.addComponents(ActionRow.of(option)));
        event.replyModal(modalBuilder.build()).queue();
    }

    @Override
    public void handleModal(@NotNull ModalInteractionEvent event) {
        // Required items
        String title = event.getValue("poll-title").getAsString();
        ArrayList<String> arguments = convertToList(event.getValue("poll-options").getAsString());
        int minutes = Helper.stringToPositiveInteger(event.getValue("poll-time").getAsString());

        // Checking if at least 60 seconds.
        if (minutes < 1) {
            event.getHook().sendMessageEmbeds(
                    Helper.errorEmbed(
                            "Too Little Time",
                            "Polls must be at least 60 seconds long."
                    )
            ).queue();
            return;
        }

        // Making sure the poll channel exists.
        TextChannel pollChannel = GuildHandler.getCustomGuild(event.getGuild()).getPollChannel();

        // Making sure there are less than 20 arguments.
        if (arguments.size() > 20) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Too Many Items",
                    "You can only have 20 items. Please try again. Discord only supports 20 message reactions."
            )).queue();
            return;
        }

        Timestamp timestamp = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis() + (minutes*60000)).toString()).orElseThrow();

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Creating Poll",
                "I'm creating your poll right now!"
        )).queue();

        // Sending a message in the poll channel.
        if (event.getValue("message") != null) {
            pollChannel.sendMessage(event.getValue("poll-message").getAsString()).setEmbeds(startingPollEmbed()).queue(message -> {
                editMessage(message, event, timestamp, title, minutes, arguments);
            });
        } else {
            pollChannel.sendMessageEmbeds(startingPollEmbed()).queue(message -> {
                editMessage(message, event, timestamp, title, minutes, arguments);
            });
        }
    }

    @NotNull
    @Override
    public CommandType getType() {
        return CommandType.MODAL;
    }

    /**
     * Edits the specified {@link Message}.
     * @param message The {@link Message} to edit.
     * @param event The {@link SlashCommandInteractionEvent event} that triggered the {@link Message}.
     * @param timestamp The {@link Timestamp} to end the {@link }.
     * @param title The {@link String title} of the {@link Poll}.
     * @param minutes The length in {@link Integer minutes} of the {@link Poll}.
     * @param arguments The {@link ArrayList} of {@link String argument}.
     */
    private void editMessage(Message message, ModalInteractionEvent event, Timestamp timestamp,
                             String title, Integer minutes, ArrayList<String> arguments) {
        Poll poll = new Poll(message.getId(), timestamp);

        // Tries to create the poll.
        if (!PollHandler.addPoll(event.getGuild().getId(), poll)) {
            // If it can't, say why.
            message.delete().queue();
            event.getHook().editOriginalEmbeds(Helper.sqlServerError()).queue();
            return;
        }

        message.editMessageEmbeds(pollEmbed(title, minutes, arguments, event)).queue();

        ArrayList<PollEmoji> pollEmojis = new ArrayList<>(Arrays.asList(PollEmoji.values()));
        for (int i = 0; i < arguments.size(); i++) {
            message.addReaction(Emoji.fromFormatted(pollEmojis.get(i).getUnicode())).queue();
        }

        event.getHook().editOriginalEmbeds(Helper.successEmbed(
                "Poll Created",
                "A poll has been successfully created! Check the " + message.getChannel().asTextChannel().getAsMention() + " channel."
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
     * @param pollTime The {@link Integer length} of the {@link Poll} in minutes.
     * @param arguments The {@link ArrayList} of {@link String argument} for the {@link Poll}.
     * @param event The {@link SlashCommandInteractionEvent event} that ran the command.
     * @return The created {@link MessageEmbed} for the {@link Poll}.
     */
    @NotNull
    private MessageEmbed pollEmbed(@NotNull String pollTitle, @NotNull Integer pollTime,
                                   @NotNull ArrayList<String> arguments, @NotNull ModalInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(pollTitle);

        ArrayList<PollEmoji> pollEmojis = new ArrayList<>(Arrays.asList(PollEmoji.values()));
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < arguments.size(); i++) {
            stringBuilder.append(pollEmojis.get(i).getDiscordString()).append(" - ")
                    .append(arguments.get(i)).append("\n");
        }

        stringBuilder.append("\n");  // TODO: Check if this is needed.
        embedBuilder.setDescription(stringBuilder.toString());

        if (pollTime == 1) {
            embedBuilder.setFooter("This poll will end in " + pollTime + " minute from when it was created.");
        } else {
            embedBuilder.setFooter("This poll will end in " + pollTime + " minutes from when it was created.");
        }

        // Color
        if (event.getValue("poll-color") != null) {
            try {
                embedBuilder.setColor(Color.decode(event.getValue("poll-color").getAsString()));
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
        return "Just do `/poll` :sob: it shows you how to use it.";
    }

    private ArrayList<TextInput> getModalOptions() {
        ArrayList<TextInput> options = new ArrayList<>();
        options.add(
                TextInput.create("poll-title", "Question", TextInputStyle.SHORT)
                        .setPlaceholder("The question you want to ask.")
                        .build()
        );

        options.add(
                TextInput.create("poll-options", "Options", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("The options of the poll. E.g. (red, blue, green)")
                        .build()
        );

        options.add(
                TextInput.create("poll-time", "Time (Minimum 1 Minute)", TextInputStyle.SHORT)
                        .setPlaceholder("The amount of time the poll should last. (In Minutes)")
                        .build()
        );

        options.add(
                TextInput.create("poll-message", "Message", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("A message you want to send alongside the poll.")
                        .setRequired(false)
                        .build()
        );

        options.add(
                TextInput.create("poll-color", "Color", TextInputStyle.SHORT)
                        .setPlaceholder("A color you want the poll to be. E.g. (#FFC0CB)")
                        .setRequired(false)
                        .build()
        );

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
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MANAGE_SERVER);
        return permissions;
    }

}
