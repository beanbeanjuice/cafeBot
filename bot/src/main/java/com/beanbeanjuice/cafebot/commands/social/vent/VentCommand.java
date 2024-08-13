package com.beanbeanjuice.cafebot.commands.social.vent;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.*;
import net.dv8tion.jda.api.Permission;

public class VentCommand extends Command implements ICommand {

    public VentCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "vent";
    }

    @Override
    public String getDescription() {
        return "Anything to do with anonymous venting!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.SOCIAL;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
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
                new VentSendSubCommand(cafeBot)
        };
    }

    @Override
    public SubCommandGroup[] getSubCommandGroups() {
        SubCommandGroup editChannelGroup = new SubCommandGroup("channel", "Edit the venting channel.");
        ISubCommand[] editChannelSubCommands = new ISubCommand[] {
                new SetVentChannelSubCommand(cafeBot),
                new RemoveVentChannelSubCommand(cafeBot)
        };
        editChannelGroup.addSubCommands(editChannelSubCommands);

        return new SubCommandGroup[] {
                editChannelGroup
        };
    }
}
