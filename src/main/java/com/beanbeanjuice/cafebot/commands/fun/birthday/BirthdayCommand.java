package com.beanbeanjuice.cafebot.commands.fun.birthday;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.commands.SubCommandGroup;
import net.dv8tion.jda.api.Permission;

public class BirthdayCommand extends Command implements ICommand {

    public BirthdayCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "birthday";
    }

    @Override
    public String getDescription() {
        return "Get someone's birthday or change your own!";
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
        return true;
    }

    @Override
    public ISubCommand[] getSubCommands() {
        return new ISubCommand[] {
                new GetBirthdaySubCommand(cafeBot),
        };
    }

    @Override
    public SubCommandGroup[] getSubCommandGroups() {
        ISubCommand[] editBirthdaySubCommands = new ISubCommand[] {
                new SetBirthdaySubCommand(cafeBot),
                new RemoveBirthdaySubCommand(cafeBot)
        };
        SubCommandGroup editBirthdayGroup = new SubCommandGroup("edit", "Edit your birthday!");
        editBirthdayGroup.addSubCommands(editBirthdaySubCommands);

        ISubCommand[] editBirthdayChannelSubCommands = new ISubCommand[] {
                new SetBirthdayChannelSubCommand(cafeBot),
                new RemoveBirthdayChannelSubCommand(cafeBot)
        };
        SubCommandGroup editBirthdayChannelGroup = new SubCommandGroup("edit-channel", "Edit the birthday channel!");
        editBirthdayChannelGroup.addSubCommands(editBirthdayChannelSubCommands);

        return new SubCommandGroup[] { editBirthdayGroup, editBirthdayChannelGroup };
    }

}
