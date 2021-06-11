package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to check the {@link net.dv8tion.jda.api.entities.Guild Guild} member count.
 *
 * @author beanbeanjuice
 */
public class MemberCountCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        int count = 0;

        for (Member member : event.getGuild().getMembers()) {
            if (!member.getUser().isBot()) {
                count++;
            }
        }

        event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                "Member Count",
                "You currently have `" + count + "` members in your server!"
        )).queue();
    }

    @Override
    public String getName() {
        return "member-count";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("membercount");
        arrayList.add("members");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Check the member count for your server!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "members`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }
}
