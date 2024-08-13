package com.beanbeanjuice.cafeapi.wrapper.exception.program;

import com.beanbeanjuice.cafeapi.wrapper.exception.api.TeaPotException;

/**
 * A {@link RuntimeException} for dealing with invalid {@link java.util.TimeZone TimeZone}.
 *
 * @author beanbeanjuice
 */
public class InvalidTimeZoneException extends TeaPotException {

    /**
     * Creates a new {@link InvalidTimeZoneException}.
     * @param message The message to send to the {@link TeaPotException}.
     */
    public InvalidTimeZoneException(final String message) {
        super(message);
    }

    /**
     * Creates a new {@link InvalidTimeZoneException}.
     */
    public InvalidTimeZoneException() {
        super("The timezone is invalid!");
    }

}
