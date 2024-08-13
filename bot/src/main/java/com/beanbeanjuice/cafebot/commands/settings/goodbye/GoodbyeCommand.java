package com.beanbeanjuice.cafebot.commands.settings.goodbye;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.commands.settings.goodbye.channel.GoodbyeChannelRemoveSubCommand;
import com.beanbeanjuice.cafebot.commands.settings.goodbye.channel.GoodbyeChannelSetSubCommand;
import com.beanbeanjuice.cafebot.commands.settings.goodbye.message.GoodbyeMessageRemoveSubCommand;
import com.beanbeanjuice.cafebot.commands.settings.goodbye.message.GoodbyeMessageSetSubCommand;
import com.beanbeanjuice.cafebot.utility.commands.*;
import net.dv8tion.jda.api.Permission;

public class GoodbyeCommand extends Command implements ICommand {

    public GoodbyeCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "goodbye";
    }

    @Override
    public String getDescription() {
        return "Edit the goodbye settings!";
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
        SubCommandGroup channelGroup = new SubCommandGroup("channel", "Edit the goodbye channel.");
        channelGroup.addSubCommands(new ISubCommand[] {
                new GoodbyeChannelSetSubCommand(cafeBot),
                new GoodbyeChannelRemoveSubCommand(cafeBot)
        });

        SubCommandGroup messageGroup = new SubCommandGroup("message", "Edit the goodbye message.");
        messageGroup.addSubCommands(new ISubCommand[] {
                new GoodbyeMessageSetSubCommand(cafeBot),
                new GoodbyeMessageRemoveSubCommand(cafeBot)
        });

        return new SubCommandGroup[] { channelGroup, messageGroup };
    }

}
