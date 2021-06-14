package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used for support.
 *
 * @author beanbeanjuice
 */
public class SupportCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        CafeBot.getGeneralHelper().pmUser(user, "Join this server for support! https://discord.gg/KrUFw3uHST");

        event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                "Support Discord Sent",
                "Please check your direct messages for the discord link."
        )).queue();
    }

    @Override
    public String getName() {
        return "support";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Use this command and join the discord if you need support!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "support`";
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
