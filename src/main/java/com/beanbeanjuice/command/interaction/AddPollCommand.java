package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.poll.Poll;
import com.beanbeanjuice.utility.poll.PollEmoji;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A command used to add a {@link com.beanbeanjuice.utility.poll.Poll Poll}.
 *
 * @author beanbeanjuice
 */
public class AddPollCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        // Checking for if they are a moderator.
        if (!BeanBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        // Makes sure that guilds only have 3 polls.
        if (BeanBot.getPollHandler().getPollsForGuild(event.getGuild()).size()+1 > 3) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "Too Many Poll",
                    "You can currently only have a total of " +
                            "3 polls per Discord Server. This is due to server costs."
            )).queue();
            return;
        }

        String title = BeanBot.getGeneralHelper().removeUnderscores(args.get(0));
        String description = BeanBot.getGeneralHelper().removeUnderscores(args.get(1));
        Integer minutes = Integer.parseInt(args.get(2));
        ArrayList<String> arguments = convertToList(args.get(3));

        // Making sure the poll channel exists.
        TextChannel pollChannel = BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).getPollChannel();
        if (pollChannel == null) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "No Poll Channel",
                    "You do not currently have a poll channel set."
            )).queue();
            return;
        }

        // Making sure there are less than 20 arguments.
        if (arguments.size() > 20) {
            pollChannel.sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "Too Many Items",
                    "You can only have 20 items. Please try again. Discord only supports 20 message reactions."
            )).queue();
            return;
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + (minutes*60000));

        // Sending a message in the poll channel.
        pollChannel.sendMessage(startingPollEmbed()).queue(e -> {
            Poll poll = new Poll(event.getGuild().getId(), e.getId(), timestamp);

            if (!BeanBot.getPollHandler().addPoll(poll)) {
                e.editMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            e.editMessage(pollEmbed(title, description, minutes, arguments)).queue();

            ArrayList<PollEmoji> pollEmojis = new ArrayList<>(Arrays.asList(PollEmoji.values()));
            for (int i = 0; i < arguments.size(); i++) {
                e.addReaction(pollEmojis.get(i).getUnicode()).queue();
            }
        });
    }

    @NotNull
    private MessageEmbed startingPollEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Setting Up Polls...");
        embedBuilder.setDescription("The poll is currently being setup. Please hold on.");
        embedBuilder.setColor(Color.orange);
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed pollEmbed(@NotNull String pollTitle, @NotNull String pollDescription,
                                   @NotNull Integer pollTime, @NotNull ArrayList<String> arguments) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(pollTitle);
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());

        ArrayList<PollEmoji> pollEmojis = new ArrayList<>(Arrays.asList(PollEmoji.values()));
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < arguments.size(); i++) {
            stringBuilder.append(pollEmojis.get(i).getDiscordString()).append(" - ")
                    .append(arguments.get(i)).append("\n");
        }

        stringBuilder.append("\n\n").append(pollDescription);
        embedBuilder.setDescription(stringBuilder.toString());

        if (pollTime == 1) {
            embedBuilder.setFooter("This poll will end in " + pollTime + " minute from when it was created.");
        } else {
            embedBuilder.setFooter("This poll will end in " + pollTime + " minutes from when it was created.");
        }
        return embedBuilder.build();
    }

    @NotNull
    private ArrayList<String> convertToList(@NotNull String string) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String argument : string.split(",")) {
            arrayList.add(BeanBot.getGeneralHelper().removeUnderscores(argument));
        }
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
        return "Start a poll!";
    }

    @Override
    public String exampleUsage() {
        return "`!!add-poll Red_or_Blue? Which_colour_is_the_best? 12 Red,Blue`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Poll Title", true);
        usage.addUsage(CommandType.TEXT, "Poll Description", true);
        usage.addUsage(CommandType.NUMBER, "Poll Duration (In Minutes)", true);
        usage.addUsage(CommandType.TEXT, "Poll Arguments", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.INTERACTION;
    }
}
