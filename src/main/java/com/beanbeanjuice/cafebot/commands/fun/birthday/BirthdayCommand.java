package com.beanbeanjuice.cafebot.commands.fun.birthday;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.commands.fun.birthday.channel.BirthdayChannelRemoveSubCommand;
import com.beanbeanjuice.cafebot.commands.fun.birthday.channel.BirthdayChannelSetSubCommand;
import com.beanbeanjuice.cafebot.commands.fun.birthday.self.BirthdayRemoveSubCommand;
import com.beanbeanjuice.cafebot.commands.fun.birthday.self.BirthdaySetSubCommand;
import com.beanbeanjuice.cafebot.utility.commands.*;
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
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
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
                new BirthdayGetSubCommand(cafeBot),
                new BirthdaySetSubCommand(cafeBot),
                new BirthdayRemoveSubCommand(cafeBot)
        };
    }

    @Override
    public SubCommandGroup[] getSubCommandGroups() {
        SubCommandGroup editBirthdayChannelGroup = new SubCommandGroup("channel", "Edit the birthday channel!");
        editBirthdayChannelGroup.addSubCommands(new ISubCommand[] {
                new BirthdayChannelSetSubCommand(cafeBot),
                new BirthdayChannelRemoveSubCommand(cafeBot)
        });

        return new SubCommandGroup[] { editBirthdayChannelGroup };
    }

}
