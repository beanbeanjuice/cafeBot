package com.beanbeanjuice.cafebot.commands.moderation.polls;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.commands.moderation.polls.channel.PollChannelRemoveSubCommand;
import com.beanbeanjuice.cafebot.commands.moderation.polls.channel.PollChannelSetSubCommand;
import com.beanbeanjuice.cafebot.utility.commands.*;
import net.dv8tion.jda.api.Permission;

public class PollCommand extends Command implements ICommand {

    public PollCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "poll";
    }

    @Override
    public String getDescription() {
        return "Edit the poll channel or create a poll!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MODERATION;
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
                new PollCreateSubCommand(cafeBot)
        };
    }

    @Override
    public SubCommandGroup[] getSubCommandGroups() {
        SubCommandGroup channelGroup = new SubCommandGroup("channel", "Edit the poll channel.");
        channelGroup.addSubCommands(new ISubCommand[] {
                new PollChannelSetSubCommand(cafeBot),
                new PollChannelRemoveSubCommand(cafeBot)
        });

        return ICommand.super.getSubCommandGroups();
    }

}
