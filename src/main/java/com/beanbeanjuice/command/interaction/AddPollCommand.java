package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

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
        return "`!!add-pole Red_or_Blue? Which_colour_is_the_best? 12 Red,Blue`";
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
