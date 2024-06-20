package com.beanbeanjuice.cafebot.utility.command;

import org.jetbrains.annotations.NotNull;

/**
 * An interface extending off of {@link ICommand}.
 * This is used for {@link net.dv8tion.jda.api.interactions.commands.Command.Subcommand Subcommands}.
 *
 * @author beanbeanjuice
 */
public interface ISubCommand extends ICommand {

    /**
     * @return The {@link String name} of the {@link ISubCommand}.
     */
    @NotNull
    String getName();

}
