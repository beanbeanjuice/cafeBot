package com.beanbeanjuice.utility.command.usage.types;

import org.jetbrains.annotations.NotNull;

/**
 * An enum used for handling {@link com.beanbeanjuice.utility.command.ICommand ICommand} errors.
 *
 * @author beanbeanjuice
 */
public enum CommandErrorType {

    NUMBER ("A number is required."),
    USER ("A user is required."),
    LINK ("A link is required."),
    SUCCESS ("Successful command."),
    TOO_MANY_ARGUMENTS ("There are too many arguments."),
    NOT_ENOUGH_ARGUMENTS ("There are not enough arguments.");

    private final String description;

    /**
     * Creates a new instance of the {@link CommandErrorType}.
     * @param description The error message.
     */
    CommandErrorType(@NotNull String description) {
        this.description = description;
    }

    /**
     * @return The description of the {@link CommandErrorType}.
     */
    @NotNull
    public String getDescription() {
        return description;
    }

}
