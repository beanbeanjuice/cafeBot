package com.beanbeanjuice.cafebot.command.settings.raffle;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.command.ISubCommand;
import com.beanbeanjuice.cafebot.utility.section.moderation.raffle.Raffle;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to set/remove the {@link Raffle Raffle} {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 */
public class RaffleChannelCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) { }

    @NotNull
    @Override
    public String getDescription() {
        return "Set or remove the raffle channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/raffle-channel set` or `/raffle-channel remove`";
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
        subCommands.add(new SetRaffleChannelSubCommand());
        subCommands.add(new RemoveRaffleChannelSubCommand());
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
