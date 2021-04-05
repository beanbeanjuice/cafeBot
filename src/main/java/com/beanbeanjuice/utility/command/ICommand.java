package com.beanbeanjuice.utility.command;

import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An interface used for {@link ICommand commands}.
 *
 * @author beanbeanjuice
 */
public interface ICommand {

    /**
     * The main method for running the command.
     * @param ctx The {@link CommandContext} containing various methods.
     * @param args The arguments of the {@link ICommand}.
     * @param user The {@link User} who triggered the {@link GuildMessageReceivedEvent}.
     * @param event The {@link GuildMessageReceivedEvent}.
     */
    void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event);

    /**
     * @return The name of the {@link ICommand}.
     */
    String getName();

    /**
     * @return The {@link ArrayList<String> aliases} for the {@link ICommand}.
     */
    ArrayList<String> getAliases();

    /**
     * @return The description for the {@link ICommand}.
     */
    String getDescription();

    /**
     * @return The {@link Usage} for the {@link ICommand}.
     */
    Usage getUsage();

    /**
     * @return The {@link CategoryType} for the {@link ICommand}.
     */
    CategoryType getCategoryType();

}