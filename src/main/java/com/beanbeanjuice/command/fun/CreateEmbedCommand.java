package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class CreateEmbedCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ArrayList<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String exampleUsage(String prefix) {
        return null;
    }

    @Override
    public Usage getUsage() {
        return null;
    }

    @Override
    public CategoryType getCategoryType() {
        return null;
    }
    
}
