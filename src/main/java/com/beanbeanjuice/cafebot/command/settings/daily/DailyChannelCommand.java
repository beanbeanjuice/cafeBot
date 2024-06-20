package com.beanbeanjuice.cafebot.command.settings.daily;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.command.ISubCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to set the daily {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} for
 * the {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class DailyChannelCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) { }

    @NotNull
    @Override
    public String getDescription() {
        return "Set or remove the daily channel! This channel resets daily.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/daily-channel set` or `/daily-channel remove`";
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
        subCommands.add(new SetDailyChannelSubCommand());
        subCommands.add(new RemoveDailyChannelSubCommand());
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
