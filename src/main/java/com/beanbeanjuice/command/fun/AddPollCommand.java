package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.sections.fun.poll.Poll;
import com.beanbeanjuice.utility.sections.fun.poll.PollEmoji;
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
 * A command used to add a {@link com.beanbeanjuice.utility.sections.fun.poll.Poll Poll}.
 *
 * @author beanbeanjuice
 */
public class AddPollCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        // Checking for if they are a moderator.
        if (!CafeBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        // Makes sure that guilds only have 3 polls.
        if (CafeBot.getPollHandler().getPollsForGuild(event.getGuild()).size()+1 > 3) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Too Many Polls",
                    "You can currently only have a total of " +
                            "3 polls per Discord Server. This is due to server costs."
            )).queue();
            return;
        }

        String title = CafeBot.getGeneralHelper().removeUnderscores(args.get(0));
        String description = CafeBot.getGeneralHelper().removeUnderscores(args.get(1));
        ArrayList<String> arguments = convertToList(args.get(2));
        Integer minutes = Integer.parseInt(args.get(3));

        // Making sure the poll channel exists.
        TextChannel pollChannel = ctx.getCustomGuild().getPollChannel();
        if (pollChannel == null) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "No Poll Channel",
                    "You do not currently have a poll channel set."
            )).queue();
            return;
        }

        // Making sure there are less than 20 arguments.
        if (arguments.size() > 20) {
            pollChannel.sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Too Many Items",
                    "You can only have 20 items. Please try again. Discord only supports 20 message reactions."
            )).queue();
            return;
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + (minutes*60000));

        // Sending a message in the poll channel.
        pollChannel.sendMessage(startingPollEmbed()).queue(message -> {
            Poll poll = new Poll(event.getGuild().getId(), message.getId(), timestamp);

            if (!CafeBot.getPollHandler().addPoll(poll)) {
                message.delete().queue();
                event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            message.editMessage(pollEmbed(title, description, minutes, arguments)).queue();

            ArrayList<PollEmoji> pollEmojis = new ArrayList<>(Arrays.asList(PollEmoji.values()));
            for (int i = 0; i < arguments.size(); i++) {
                message.addReaction(pollEmojis.get(i).getUnicode()).queue();
            }

            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Poll Created",
                    "A poll has been successfully created! Check the " + message.getTextChannel().getAsMention() + " channel."
            )).queue();
        });
    }

    @NotNull
    private MessageEmbed startingPollEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Setting Up Polls...");
        embedBuilder.setDescription("The poll is currently being setup. Please hold on.");
        embedBuilder.setColor(Color.orange);
        return embedBuilder.build();
    }

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

    @NotNull
    private ArrayList<String> convertToList(@NotNull String string) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String argument : string.split(",")) {
            arrayList.add(CafeBot.getGeneralHelper().removeUnderscores(argument));
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
        return "`!!add-poll Red_or_Blue? Which_colour_is_the_best? Red,Blue 12`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Poll Title", true);
        usage.addUsage(CommandType.TEXT, "Poll Description", true);
        usage.addUsage(CommandType.TEXT, "Poll Arguments", true);
        usage.addUsage(CommandType.NUMBER, "Poll Duration (In Minutes)", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.FUN;
    }
}
