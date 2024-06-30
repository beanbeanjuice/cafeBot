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

    /**
     * @return The description for the actual command.
     */
    String getDescription();

    /**
     * @return The available parameters for the actual command.
     */
    default OptionData[] getOptions() { return new OptionData[0]; }
    default HashMap<String, ArrayList<String>> getAutoComplete() { return null; }

    Permission[] getPermissions();

    /**
     * @return True if you should reply with a message only the user who ran the command can read.
     */
    boolean isEphemeral();

    /**
     * @return True if the {@link ICommand} can only run in NSFW channels.
     */
    boolean isNSFW();

    boolean allowDM();

    default ISubCommand[] getSubCommands() { return new ISubCommand[0]; }
    default SubCommandGroup[] getSubCommandGroups() { return new SubCommandGroup[0]; }

    default boolean isModal() { return false; }
    default Modal getModal() { return null; }

}
