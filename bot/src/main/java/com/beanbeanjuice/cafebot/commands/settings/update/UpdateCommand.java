package com.beanbeanjuice.cafebot.commands.settings.update;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.commands.settings.update.channel.UpdateChannelRemoveSubCommand;
import com.beanbeanjuice.cafebot.commands.settings.update.channel.UpdateChannelSetSubCommand;
import com.beanbeanjuice.cafebot.utility.commands.*;
import net.dv8tion.jda.api.Permission;

public class UpdateCommand extends Command implements ICommand {

    public UpdateCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "Edit the bot update settings!";
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
                new UpdateStatusSubCommand(cafeBot)
        };
    }

    @Override
    public SubCommandGroup[] getSubCommandGroups() {
        SubCommandGroup channelGroup = new SubCommandGroup("channel", "Edit the update channel.");
        channelGroup.addSubCommands(new ISubCommand[]{
                        new UpdateChannelSetSubCommand(cafeBot),
                        new UpdateChannelRemoveSubCommand(cafeBot)
                }
        );

        return new SubCommandGroup[]{ channelGroup };
    }

}
