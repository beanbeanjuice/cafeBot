package com.beanbeanjuice.utility.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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

    /**
     * @return The various options available for the {@link ICommand}.
     */
    @NotNull
    ArrayList<CommandOption> getOptions();

    /**
     * @return The {@link CommandCategory} for the {@link ICommand}.
     */
    @NotNull
    CommandCategory getCategoryType();

    @NotNull
    Boolean allowDM();
}
