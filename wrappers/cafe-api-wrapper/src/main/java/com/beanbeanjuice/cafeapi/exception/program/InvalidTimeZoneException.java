package com.beanbeanjuice.cafeapi.exception.program;

/**
 * A {@link RuntimeException} for dealing with invalid {@link java.util.TimeZone TimeZone}.
 *
 * @author beanbeanjuice
 */
public class InvalidTimeZoneException extends RuntimeException {

    /**
     * Creates a new {@link InvalidTimeZoneException}.
     * @param message The message to send to the {@link RuntimeException}.
     */
    public InvalidTimeZoneException(String message) {
        super(message);
    }

    /**
     * Creates a new {@link InvalidTimeZoneException}.
     */
    public InvalidTimeZoneException() {
        super("The timezone is invalid!");
    }

}
