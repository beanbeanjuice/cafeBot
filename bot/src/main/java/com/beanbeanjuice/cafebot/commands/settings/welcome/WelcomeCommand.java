package com.beanbeanjuice.cafebot.commands.settings.welcome;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.commands.settings.welcome.channel.WelcomeChannelRemoveSubCommand;
import com.beanbeanjuice.cafebot.commands.settings.welcome.channel.WelcomeChannelSetSubCommand;
import com.beanbeanjuice.cafebot.commands.settings.welcome.message.WelcomeMessageRemoveSubCommand;
import com.beanbeanjuice.cafebot.commands.settings.welcome.message.WelcomeMessageSetSubCommand;
import com.beanbeanjuice.cafebot.utility.commands.*;
import net.dv8tion.jda.api.Permission;

public class WelcomeCommand extends Command implements ICommand {

    public WelcomeCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "welcome";
    }

    @Override
    public String getDescription() {
        return "Edit the welcome settings!";
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
    public SubCommandGroup[] getSubCommandGroups() {
        SubCommandGroup channelGroup = new SubCommandGroup("channel", "Edit the welcome channel.");
        channelGroup.addSubCommands(new ISubCommand[] {
                new WelcomeChannelSetSubCommand(cafeBot),
                new WelcomeChannelRemoveSubCommand(cafeBot)
        });

        SubCommandGroup messageGroup = new SubCommandGroup("message", "Edit the welcome message.");
        messageGroup.addSubCommands(new ISubCommand[] {
                new WelcomeMessageSetSubCommand(cafeBot),
                new WelcomeMessageRemoveSubCommand(cafeBot)
        });

        return new SubCommandGroup[] { channelGroup, messageGroup };
    }
}
