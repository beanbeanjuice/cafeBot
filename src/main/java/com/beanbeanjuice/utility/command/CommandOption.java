package com.beanbeanjuice.utility.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

/**
 * Options for various subcommands for the {@link ICommand}.
 *
 * @author beanbeanjuice
 */
public class CommandOption {

    private final OptionType OPTION_TYPE;
    private final String NAME;
    private final String DESCRIPTION;
    private final Boolean REQUIRED;
    private final Boolean AUTOCOMPLETE;

    /**
     * Creates a new {@link CommandOption} object.
     * @param optionType The {@link OptionType} for the specified {@link CommandOption}.
     * @param name The {@link String name} for the specified {@link CommandOption}.
     * @param description The {@link String description} for the specified {@link CommandOption}.
     * @param required True, if the {@link CommandOption} is required.
     * @param autocomplete True, if the {@link CommandOption} should autocomplete.
     */
    public CommandOption(@NotNull OptionType optionType, @NotNull String name, @NotNull String description,
                         @NotNull Boolean required, @NotNull Boolean autocomplete) {
        OPTION_TYPE = optionType;
        NAME = name;
        DESCRIPTION = description;
        REQUIRED = required;
        AUTOCOMPLETE = autocomplete;
    }

    @NotNull
    public OptionType getOptionType() { return OPTION_TYPE; }

    @NotNull
    public String getName() { return NAME; }

    @NotNull
    public String getDescription() { return DESCRIPTION; }

    @NotNull
    public Boolean isRequired() { return REQUIRED; }

    @NotNull
    public Boolean hasAutoComplete() { return AUTOCOMPLETE; }

}
