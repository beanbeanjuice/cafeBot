package com.beanbeanjuice.cafebot.commands.fun.counting;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.commands.SubCommandGroup;
import net.dv8tion.jda.api.Permission;

public class CountingCommand extends Command implements ICommand {

    public CountingCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "counting";
    }

    @Override
    public String getDescription() {
        return "This command has everything to do with counting and stuff!";
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
            new CountingStatisticsSubCommand(cafeBot)
        };
    }

    @Override
    public SubCommandGroup[] getSubCommandGroups() {
        SubCommandGroup editChannelGroup = new SubCommandGroup("channel", "Edit the counting channel.");
        ISubCommand[] editChannelSubCommands = new ISubCommand[] {
                new SetCountingChannelSubCommand(cafeBot),
                new RemoveCountingChannelSubCommand(cafeBot)
        };
        editChannelGroup.addSubCommands(editChannelSubCommands);

        SubCommandGroup editRoleGroup = new SubCommandGroup("role", "Edit the failure role.");
        ISubCommand[] editRoleSubCommands = new ISubCommand[] {
                new SetFailureRoleSubCommand(cafeBot),
                new RemoveFailureRoleSubCommand(cafeBot)
        };
        editRoleGroup.addSubCommands(editRoleSubCommands);

        return new SubCommandGroup[] {
            editChannelGroup, editRoleGroup
        };
    }
}
