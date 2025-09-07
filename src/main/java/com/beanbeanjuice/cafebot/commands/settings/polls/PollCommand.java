package com.beanbeanjuice.cafebot.commands.settings.polls;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.Permission;

public class PollCommand extends Command implements ICommand {

    public PollCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "poll";
    }

    @Override
    public String getDescription() {
        return "All things to do with polls!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.SETTINGS;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{
                Permission.MANAGE_SERVER,
                Permission.MANAGE_CHANNEL
        };
    }

    @Override
    public boolean isEphemeral() {
        return true;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return false;
    }

    @Override
    public ISubCommand[] getSubCommands() {
        return new ISubCommand[] {
                new PollCreateSubCommand(bot),
                new PollDeleteSubCommand(bot)
        };
    }

}
