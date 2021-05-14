package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.poll.Poll;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

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

        if (!BeanBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        String title = removeUnderscores(args.get(0));
        String description = removeUnderscores(args.get(1));
        Integer minutes = Integer.parseInt(args.get(2));
        ArrayList<String> arguments = convertToList(args.get(3));



    }

    @NotNull
    private MessageEmbed pollEmbed(@NotNull String pollTitle, @NotNull String pollDescription,
                                   @NotNull String pollTime, @NotNull ArrayList<String> arguments) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(pollTitle);
        embedBuilder.setDescription(pollDescription);

        // TODO: Continue here
        StringBuilder stringBuilder = new StringBuilder();

        return embedBuilder.build();
    }

    @NotNull
    private String removeUnderscores(@NotNull String string) {
        return string.replaceAll("_", " ");
    }

    @NotNull
    private ArrayList<String> convertToList(@NotNull String string) {
        return new ArrayList<>(Arrays.asList(string.split(",")));
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
