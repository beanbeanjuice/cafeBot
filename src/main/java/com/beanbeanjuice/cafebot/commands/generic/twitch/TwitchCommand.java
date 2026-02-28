package com.beanbeanjuice.cafebot.commands.generic.twitch;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.*;
import net.dv8tion.jda.api.Permission;

public class TwitchCommand extends Command implements ICommand {

    public TwitchCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "twitch";
    }

    @Override
    public String getDescriptionPath() {
        return "Add or remove channels/notification channels.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] {
                Permission.MANAGE_CHANNEL,
                Permission.MANAGE_EVENTS,
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
                new TwitchAddSubCommand(bot),
                new TwitchRemoveSubCommand(bot),
                new TwitchListSubCommand(bot)
        };
    }

}
