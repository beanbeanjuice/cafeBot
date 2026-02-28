package com.beanbeanjuice.cafebot.utility.commands;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ISubCommand {

    void handle(SlashCommandInteractionEvent event, CommandContext ctx);

    String getName();

    String getDescriptionPath();

    default OptionData[] getOptions() { return new OptionData[0]; }

    default CompletableFuture<HashMap<String, List<String>>> getAutoComplete(CommandAutoCompleteInteractionEvent event) { return null; }

    default boolean isModal() { return false; }

}
