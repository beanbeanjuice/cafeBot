package com.beanbeanjuice.command.settings.counting;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.ISubCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * A {@link ICommand} used for dealing with the {@link com.beanbeanjuice.cafeapi.cafebot.counting.CountingInformation CountingInformation}
 * for the {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class CountingChannelCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) { }  // Empty

    @NotNull
    @Override
    public String getDescription() {
        return "Set or remove the counting channel.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/counting-channel set` or `/counting-channel remove`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SETTINGS;
    }

    @NotNull
    @Override
    public ArrayList<ISubCommand> getSubCommands() {
        ArrayList<ISubCommand> subCommands = new ArrayList<>();
        subCommands.add(new SetCountingChannelSubCommand());
        subCommands.add(new RemoveCountingChannelSubCommand());
        subCommands.add(new CountingChannelFailureRoleSubCommand());
        return subCommands;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
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
