package com.beanbeanjuice.utility.command.usage;

import com.beanbeanjuice.utility.command.usage.types.CommandType;
import org.jetbrains.annotations.NotNull;

/**
 * A usage class for handling the types.
 *
 * @author beanbeanjuice
 */
public class CommandUsage {

    private String name;
    private boolean isRequired;
    private CommandType type;

    /**
     * Creates a new instance of the {@link CommandUsage} object.
     * @param name The name of the {@link CommandUsage}.
     * @param isRequired Whether or not the {@link CommandUsage} is required.
     * @param type The {@link CommandType} of the {@link CommandUsage}.
     */
    public CommandUsage(@NotNull String name, @NotNull Boolean isRequired, @NotNull CommandType type) {
        this.name = name;
        this.isRequired = isRequired;
        this.type = type;
    }

    /**
     * @return The name of the {@link CommandUsage}.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @return Whether or not the {@link CommandUsage} is required.
     */
    @NotNull
    public Boolean isRequired() {
        return isRequired;
    }

    /**
     * @return The {@link CommandType} of the {@link CommandUsage}.
     */
    @NotNull
    public CommandType getType() {
        return type;
    }

}
