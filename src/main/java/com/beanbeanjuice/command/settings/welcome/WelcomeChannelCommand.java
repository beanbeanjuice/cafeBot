package com.beanbeanjuice.command.settings.welcome;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.ISubCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ICommand} used for setting/removing the welcome {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}
 * in a {@link net.dv8tion.jda.api.entities.Guild Guild} and/or editing the welcome message.
 */
public class WelcomeChannelCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) { }

    @NotNull
    @Override
    public String getDescription() {
        return "Set/Remove the welcome channel or edit the welcome message!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/welcome-channel set` or `/welcome-channel remove` or `/welcome-channel edit-message`";
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
        subCommands.add(new SetWelcomeChannelSubCommand());
        subCommands.add(new RemoveWelcomeChannelSubCommand());
        subCommands.add(new EditWelcomeMessageSubCommand());
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
