package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.generic.CafeGeneric;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.sections.fun.poll.Poll;
import com.beanbeanjuice.utility.sections.fun.poll.PollEmoji;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A command used to add a {@link com.beanbeanjuice.utility.sections.fun.poll.Poll Poll}.
 *
 * @author beanbeanjuice
 */
public class AddPollCommand implements ICommand {

    private final int MAX_POLLS = 3;

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        // Checking for if they are a moderator.
        if (!CafeBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        // Makes sure that guilds only have 3 polls.
        if (CafeBot.getCafeAPI().polls().getGuildPolls(event.getGuild().getId()).size()+1 > MAX_POLLS) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Too Many Polls",
                    "You can currently only have a total of " +
                            "3 polls per Discord Server. This is due to server costs."
            )).queue();
            return;
        }

        HashMap<String, String> parsedMap = CafeBot.getGeneralHelper().createCommandTermMap(getCommandTerms(), args);

        // Makes sure there is a title.
        String title = parsedMap.get("title");
        if (title == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Missing Argument",
                    "You are missing the `title` argument."
            )).queue();
            return;
        }

        // Makes sure there is a description.
        String description = parsedMap.get("description");
        if (description == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Missing Argument",
                    "You are missing the `description` argument."
            )).queue();
            return;
        }

        // Makes sure there are arguments for the poll.
        ArrayList<String> arguments = convertToList(parsedMap.get("arguments"));
        if (arguments.size() == 0) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Missing Argument",
                    "You are missing the `arguments` argument."
            )).queue();
            return;
        }

        // Makes sure there are minutes.
        int minutes;
        try {
            minutes = Integer.parseInt(parsedMap.get("time"));
        } catch (NumberFormatException e) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Invalid Time Amount",
                    "The amount you have entered for time is not valid. Please enter a whole number."
            )).queue();
            return;
        }

        // Making sure the poll channel exists.
        TextChannel pollChannel = ctx.getCustomGuild().getPollChannel();
        if (pollChannel == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "No Poll Channel",
                    "You do not currently have a poll channel set."
            )).queue();
            return;
        }

        // Making sure there are less than 20 arguments.
        if (arguments.size() > 20) {
            pollChannel.sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Too Many Items",
                    "You can only have 20 items. Please try again. Discord only supports 20 message reactions."
            )).queue();
            return;
        }

        Timestamp timestamp = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis() + (minutes*60000)).toString());

        // Sending a message in the poll channel.
        if (parsedMap.get("message") != null) {
            pollChannel.sendMessage(parsedMap.get("message")).setEmbeds(startingPollEmbed()).queue(message -> {
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
     * @param event The {@link GuildMessageReceivedEvent event} that triggered the {@link Message}.
     * @param timestamp The {@link Timestamp} to end the {@link }.
     * @param title The {@link String title} of the {@link Poll}.
     * @param description The {@link String description} of the {@link Poll}.
     * @param minutes The length in {@link Integer minutes} of the {@link Poll}.
     * @param arguments The {@link ArrayList} of {@link String argument}.
     */
    private void editMessage(Message message, GuildMessageReceivedEvent event, Timestamp timestamp,
                             String title, String description, Integer minutes, ArrayList<String> arguments) {
        Poll poll = new Poll(message.getId(), timestamp);

        // Tries to create the poll.
        if (!CafeBot.getPollHandler().addPoll(event.getGuild().getId(), poll)) {
            // If it can't, say why.
            message.delete().queue();
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Creating Poll",
                    "There has been an error creating the Poll..."
            )).queue();
            return;
        }

        message.editMessageEmbeds(pollEmbed(title, description, minutes, arguments)).queue();

        ArrayList<PollEmoji> pollEmojis = new ArrayList<>(Arrays.asList(PollEmoji.values()));
        for (int i = 0; i < arguments.size(); i++) {
            message.addReaction(pollEmojis.get(i).getUnicode()).queue();
        }

        event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().successEmbed(
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
     * @return The created {@link MessageEmbed} for the {@link Poll}.
     */
    @NotNull
    private MessageEmbed pollEmbed(@NotNull String pollTitle, @NotNull String pollDescription,
                                   @NotNull Integer pollTime, @NotNull ArrayList<String> arguments) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(pollTitle);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());

        ArrayList<PollEmoji> pollEmojis = new ArrayList<>(Arrays.asList(PollEmoji.values()));
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < arguments.size(); i++) {
            stringBuilder.append(pollEmojis.get(i).getDiscordString()).append(" - ")
                    .append(arguments.get(i)).append("\n");
        }

        stringBuilder.append("\n`").append(pollDescription).append("`");
        embedBuilder.setDescription(stringBuilder.toString());

        if (pollTime == 1) {
            embedBuilder.setFooter("This poll will end in " + pollTime + " minute from when it was created.");
        } else {
            embedBuilder.setFooter("This poll will end in " + pollTime + " minutes from when it was created.");
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
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            for (String argument : string.split(",")) {
                arrayList.add(CafeBot.getGeneralHelper().removeUnderscores(argument));
            }
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
        return arrayList;
    }

    /**
     * @return The {@link ArrayList} of {@link String commandTerms} for the {@link Poll}.
     */
    @NotNull
    private ArrayList<String> getCommandTerms() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("title");
        arrayList.add("description");
        arrayList.add("arguments");
        arrayList.add("time");
        arrayList.add("message");
        return arrayList;
    }

    @Override
    public String getName() {
        return "add-poll";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("addpoll");
        arrayList.add("poll");
        arrayList.add("createpoll");
        arrayList.add("create-poll");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Start a poll! Please check the example above to see how to use this.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "add-poll title:Red or Blue? description:Which colour is the best? message:@fakeRole, please answer this poll. arguments:Red,Blue time:12`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.SENTENCE, "Poll Arguments", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.FUN;
    }
}
