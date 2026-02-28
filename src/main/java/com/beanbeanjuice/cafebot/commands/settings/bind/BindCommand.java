package com.beanbeanjuice.cafebot.commands.settings.bind;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.Permission;

public class BindCommand extends Command implements ICommand {

    public BindCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "bind";
    }

    @Override
    public String getDescriptionPath() {
        return "All things to do with binding a role to a voice channel!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.SETTINGS;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] {
                Permission.MANAGE_ROLES
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
                new BindSetSubCommand(bot),
                new BindRemoveSubCommand(bot),
                new BindListSubCommand(bot)
        };
    }

}
