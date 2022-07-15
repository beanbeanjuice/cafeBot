package com.beanbeanjuice.command.twitch;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.ISubCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to add or remove a twitch channel from being notified when live in the
 * specified {@link net.dv8tion.jda.api.entities.Guild Guild}. This can also be used to get all
 * the twitch channels.
 *
 * @author beanbeanjuice
 */
public class TwitchChannelCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) { }

    @NotNull
    @Override
    public String getDescription() {
        return "Add or remove a twitch channel from being notified when live in this server!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/twitch-channel add beanbeanjuice` or `/twitch-channel remove beanbeanjuice` or `/twitch-channel list`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.TWITCH;
    }

    @NotNull
    @Override
    public ArrayList<ISubCommand> getSubCommands() {
        ArrayList<ISubCommand> subCommands = new ArrayList<>();
        subCommands.add(new TwitchChannelAddSubCommand());
        subCommands.add(new TwitchChannelRemoveSubCommand());
        subCommands.add(new TwitchChannelListSubCommand());
        return subCommands;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

    @Nullable
    @Override
    public ArrayList<Permission> getPermissions() {
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MANAGE_SERVER);
        return permissions;
    }

}
