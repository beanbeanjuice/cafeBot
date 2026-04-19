package com.beanbeanjuice.cafebot.utility.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public interface ICommand {

    default void handle(SlashCommandInteractionEvent event, CommandContext ctx) { }

    String getName();

    // We want to get the path at runtime because it may be different depending on who sends it.
    String getDescriptionPath();

    CommandCategory getCategory();

    default OptionData[] getOptions() { return new OptionData[0]; }
    default CompletableFuture<HashMap<String, ArrayList<String>>> getAutoComplete(CommandAutoCompleteInteractionEvent event) { return null; }

    Permission[] getPermissions();

    boolean isEphemeral();

    boolean isNSFW();

    boolean allowDM();

    default ISubCommand[] getSubCommands() { return new ISubCommand[0]; }
    default SubCommandGroup[] getSubCommandGroups() { return new SubCommandGroup[0]; }

    default boolean isModal() { return false; }

}
