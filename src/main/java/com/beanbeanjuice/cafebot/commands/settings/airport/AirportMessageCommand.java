package com.beanbeanjuice.cafebot.commands.settings.airport;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.Permission;

public class AirportMessageCommand extends Command implements ICommand {

    public AirportMessageCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "airport";
    }

    @Override
    public String getDescriptionPath() {
        return "Set or remove an airport message!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.SETTINGS;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] {
                Permission.MANAGE_SERVER
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
                new AirportMessageSetSubCommand(bot),
                new AirportMessageRemoveSubCommand(bot)
        };
    }

}
