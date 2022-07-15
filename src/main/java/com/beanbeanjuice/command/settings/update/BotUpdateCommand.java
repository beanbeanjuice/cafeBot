package com.beanbeanjuice.command.settings.update;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.ISubCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to set/remove the update {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}
 * in a specified {@link net.dv8tion.jda.api.entities.Guild Guild} as well as set whether they should
 * be notified when there is an update to the bot.
 *
 * @author beanbeanjuice
 */
public class BotUpdateCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) { }

    @NotNull
    @Override
    public String getDescription() {
        return "Set/Remove the update channel, and set whether the server should be notifed when I update!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bot-update (set/remove)-channel` or `/bot-update notify true/false`";
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
        subCommands.add(new NotifyUpdateSubCommand());
        subCommands.add(new SetUpdateChannelSubCommand());
        subCommands.add(new RemoveUpdateChannelSubCommand());
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
