package com.beanbeanjuice.cafebot.utility.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.ArrayList;
import java.util.HashMap;

public interface ICommand {

    default void handle(SlashCommandInteractionEvent event) { };
    default void handleModal(ModalInteractionEvent event) { };

    String getName();

    String getDescription();

    CommandCategory getCategory();

    default OptionData[] getOptions() { return new OptionData[0]; }
    default HashMap<String, ArrayList<String>> getAutoComplete() { return null; }

    Permission[] getPermissions();

    boolean isEphemeral();

    boolean isNSFW();

    boolean allowDM();

    default ISubCommand[] getSubCommands() { return new ISubCommand[0]; }
    default SubCommandGroup[] getSubCommandGroups() { return new SubCommandGroup[0]; }

    default boolean isModal() { return false; }
    default Modal getModal() { return null; }

}
