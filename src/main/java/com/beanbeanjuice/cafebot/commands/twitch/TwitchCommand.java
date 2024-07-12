package com.beanbeanjuice.cafebot.commands.twitch;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.commands.twitch.channel.TwitchChannelRemoveSubCommand;
import com.beanbeanjuice.cafebot.commands.twitch.channel.TwitchChannelSetSubCommand;
import com.beanbeanjuice.cafebot.commands.twitch.role.TwitchRoleRemoveSubCommand;
import com.beanbeanjuice.cafebot.commands.twitch.role.TwitchRoleSetSubCommand;
import com.beanbeanjuice.cafebot.commands.twitch.user.TwitchAddUserSubCommand;
import com.beanbeanjuice.cafebot.commands.twitch.user.TwitchListUsersSubCommand;
import com.beanbeanjuice.cafebot.commands.twitch.user.TwitchRemoveUserSubCommand;
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
    public String getDescription() {
        return "Add or remove channels/notification channels.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.TWITCH;
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
    public SubCommandGroup[] getSubCommandGroups() {
        SubCommandGroup userSubCommandGroup = new SubCommandGroup("user", "Add, list, or remove twitch channels.");
        userSubCommandGroup.addSubCommands(new ISubCommand[] {
                new TwitchAddUserSubCommand(cafeBot),
                new TwitchRemoveUserSubCommand(cafeBot),
                new TwitchListUsersSubCommand(cafeBot)
        });

        SubCommandGroup roleSubCommandGroup = new SubCommandGroup("role", "Add or remove the live notifications role.");
        roleSubCommandGroup.addSubCommands(new ISubCommand[] {
                new TwitchRoleSetSubCommand(cafeBot),
                new TwitchRoleRemoveSubCommand(cafeBot)
        });

        SubCommandGroup channelSubCommandGroup = new SubCommandGroup("channel", "Add or remove the live notifications channel.");
        channelSubCommandGroup.addSubCommands(new ISubCommand[] {
                new TwitchChannelSetSubCommand(cafeBot),
                new TwitchChannelRemoveSubCommand(cafeBot)
        });

        return new SubCommandGroup[] { userSubCommandGroup, roleSubCommandGroup, channelSubCommandGroup };
    }

}
