package com.beanbeanjuice.cafebot.command.moderation.bind;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.command.ISubCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to bind a {@link net.dv8tion.jda.api.entities.Role Role} to
 * a {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}. Also retrieves the
 * list of these binds.
 *
 * @author beanbeanjuice
 */
public class BindCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) { }

    @NotNull
    @Override
    public String getDescription() {
        return "Bind a voice channel to a role!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bind list` or `/bind general_voice @bruh`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public ArrayList<ISubCommand> getSubCommands() {
        ArrayList<ISubCommand> subCommands = new ArrayList<>();
        subCommands.add(new BindChannelSubCommand());
        subCommands.add(new BindListSubCommand());
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
        permissions.add(Permission.MANAGE_ROLES);
        return permissions;
    }

}
