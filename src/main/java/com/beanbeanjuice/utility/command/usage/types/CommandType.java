package com.beanbeanjuice.utility.command.usage.types;

import org.jetbrains.annotations.NotNull;

/**
 * An enum used for handling {@link com.beanbeanjuice.utility.command.ICommand ICommand} types.
 *
 * @author beanbeanjuice
 */
public enum CommandType {

    NUMBER ("NUMBER"),
    USER ("USER"),
    TEXT ("TEXT"),
    LINK ("LINK"),
    ROLE ("ROLE"),
    DATE ("DATE"),
    TEXTCHANNEL ("TEXT CHANNEL ID"),
    SENTENCE ("SENTENCE"); // This must be the last one added.

    private final String description;

    /**
     * Creates a new instance of the {@link CommandType}.
     * @param description The message associated with the {@link CommandType}.
     */
    CommandType(String description) {
        this.description = description;
    }

    /**
     * @return The description of the {@link CommandType}.
     */
    @NotNull
    public String getDescription() {
        return description;
    }

}