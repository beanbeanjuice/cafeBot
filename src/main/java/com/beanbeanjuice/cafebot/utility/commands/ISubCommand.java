package com.beanbeanjuice.cafebot.utility.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashMap;

public interface ISubCommand {

    void handle(SlashCommandInteractionEvent event);

    String getName();

    String getDescription();

    default OptionData[] getOptions() { return new OptionData[0]; }

    default HashMap<String, ArrayList<String>> getAutoComplete() { return null; }

    default boolean isModal() { return false; }

}
