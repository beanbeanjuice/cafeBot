package com.beanbeanjuice.utility;

import org.jetbrains.annotations.NotNull;

/**
 * A general class used for everything.
 *
 * @author beanbeanjuice
 */
public class Helper {

    /**
     * Shorten the message to a certain limit.
     * @param message The contents of the message.
     * @param limit The limit of the new message.
     * @return The new, limited message.
     */
    @NotNull
    public static String shortenToLimit(@NotNull String message, @NotNull Integer limit) {
        message = message.replace("\"", "\\\"");
        if (message.length() >= limit) {
            return message.substring(0, limit - 3) + "...";
        }

        return message;
    }

}
