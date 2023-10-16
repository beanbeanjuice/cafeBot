package com.beanbeanjuice.utility.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An interface used for {@link ICommand commands}.
 *
 * @author beanbeanjuice
 */
public interface ICommand {

    /**
     * The main method for running the command.
     * @param event The {@link SlashCommandInteractionEvent}.
     */
    void handle(@NotNull SlashCommandInteractionEvent event);

    default void handleModal(@NotNull ModalInteractionEvent event) {
        return;
    };

    /**
     * @return The description for the {@link ICommand}.
     */
    @NotNull
    String getDescription();

    /**
     * @return An example of how to use the {@link ICommand}.
     */
    @NotNull
    String exampleUsage();

    @NotNull
    default CommandType getType() {
        return CommandType.NORMAL;
    }

    /**
     * @return The various options available for the {@link ICommand}.
     */
    @NotNull
    default ArrayList<OptionData> getOptions() {
        return new ArrayList<>();
    }

    /**
     * @return The {@link CommandCategory} for the {@link ICommand}.
     */
    @NotNull
    CommandCategory getCategoryType();

    /**
     * @return The {@link ArrayList<ISubCommand>} for the specified {@link ICommand}.
     */
    @NotNull
    default ArrayList<ISubCommand> getSubCommands() {
        return new ArrayList<>();
    }

    /**
     * Runs the {@link ISubCommand} for the specified {@link ICommand}.
     * @param subCommandName The {@link String name} of the {@link ISubCommand}.
     * @param event The {@link SlashCommandInteractionEvent event} that triggered the {@link ICommand}.
     */
    default void runSubCommand(@NotNull String subCommandName, @NotNull SlashCommandInteractionEvent event) {
        for (ISubCommand subCommand : getSubCommands()) {
            if (subCommand.getName().equals(subCommandName)) {
                subCommand.handle(event);
            }
        }
    }

    /**
     * @return True, if this command is allowed to be run in a DM.
     */
    @NotNull
    default Boolean allowDM() {
        return false;
    }

    /**
     * @return True, if this command should be hidden from others.
     */
    @NotNull
    default Boolean isHidden() {
        return false;
    }

    /**
     * @return The permissions members need to use this.
     */
    @Nullable
    default ArrayList<Permission> getPermissions() {
        return null;
    }

    /**
     * @return The {@link HashMap<String, ArrayList<String>>}.
     */
    @Nullable
    default HashMap<String, ArrayList<String>> getAutoComplete() {
        return null;
    }

}
