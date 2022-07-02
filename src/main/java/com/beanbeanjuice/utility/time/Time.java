package com.beanbeanjuice.utility.time;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A class used for keeping track of time.
 *
 * @author beanbeanjuice
 */
public class Time {

    private final TimeZone TIME_ZONE;
    private String defaultFormat;

    /**
     * Creates a new {@link Time} object with a specified
     * {@link TimeZone} and {@link String format}.
     * @param timezone The {@link String} of the specified {@link TimeZone}.
     * @param defaultFormat The {@link String} for the specified default {@link String format}.
     */
    public Time(@NotNull String timezone, @NotNull String defaultFormat) {
        TIME_ZONE = TimeZone.getTimeZone(timezone);
        setDefaultFormat(defaultFormat);
    }

    /**
     * Creates a new {@link Time} object with a specified
     * {@link TimeZone}.
     * @param timezone The {@link String} of the specified {@link TimeZone}.
     */
    public Time(@NotNull String timezone) {
        TIME_ZONE = TimeZone.getTimeZone(timezone);
    }

    /**
     * Creates a new {@link Time} object with a
     * UTC {@link TimeZone}.
     */
    public Time() {
        TIME_ZONE = TimeZone.getTimeZone("UTC");
    }

    /**
     * Sets the default {@link String format} for the specified {@link Time} object.
     * @param format The {@link String format} to be set.
     */
    public void setDefaultFormat(@NotNull String format) {
        defaultFormat = format;
    }

    /**
     * Format the current time to a specified {@link String format}.
     * @param format The {@link String format} to return.
     * @return The {@link String formatted} {@link Time}.
     */
    @NotNull
    public String format(@NotNull String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TIME_ZONE);
        return simpleDateFormat.format(new Date());
    }

    /**
     * Format using the default formatting specified.
     * @return The {@link String formatted} {@link Time}.
     * @throws NullPointerException Thrown when there is no default {@link String format}.
     */
    @NotNull
    public String format() throws NullPointerException {
        if (defaultFormat == null)
            throw new NullPointerException("Default format is not specified!");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultFormat);
        simpleDateFormat.setTimeZone(TIME_ZONE);
        return simpleDateFormat.format(new Date());
    }

    /**
     * Compare the difference in time between two {@link Timestamp} objects.
     * @param oldTime The old {@link Timestamp}.
     * @param currentTime The new {@link Timestamp}.
     * @param timestampDifference The {@link TimestampDifference} to choose.
     * @return The difference in time as a {@link Long}.
     */
    @NotNull
    public static Long compareTwoTimeStamps(@NotNull Timestamp oldTime, @NotNull Timestamp currentTime, @NotNull TimestampDifference timestampDifference) {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();
        long diff = milliseconds2 - milliseconds1;

        switch (timestampDifference) {
            case SECONDS -> {
                return diff / 1000;
            }

            case MINUTES -> {
                return diff / (60 * 1000);
            }

            case HOURS -> {
                return diff / (60 * 60 * 1000);
            }

            case DAYS -> {
                return diff / (24 * 60 * 60 * 1000);
            }

            default -> {
                return diff;
            }
        }
    }

}
