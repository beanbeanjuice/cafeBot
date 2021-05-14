package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * A command used for setting the {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} to a {@link com.beanbeanjuice.utility.poll.Poll Poll} channel.
 *
 * @author beanbeanjuice
 */
public class SetPollChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!BeanBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        if (BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).setPollChannel(event.getChannel().getId())) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().successEmbed(
                    "Set Poll Channel",
                    "The current channel has been set to a `poll` channel!"
            )).queue();
            return;
        }

        event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();

    }

    @Override
    public String getName() {
        return "set-poll-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setpollchannel");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Set the current channel as a poll channel!";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}
