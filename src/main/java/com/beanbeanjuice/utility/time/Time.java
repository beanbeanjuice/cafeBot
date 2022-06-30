package com.beanbeanjuice.utility.time;

import org.jetbrains.annotations.NotNull;

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

    public void setDefaultFormat(@NotNull String format) {
        defaultFormat = format;
    }

    @NotNull
    public String format(@NotNull String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TIME_ZONE);
        return simpleDateFormat.format(new Date());
    }

    @NotNull
    public String format() throws NullPointerException {
        if (defaultFormat == null)
            throw new NullPointerException("Default format is not specified!");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultFormat);
        simpleDateFormat.setTimeZone(TIME_ZONE);
        return simpleDateFormat.format(new Date());
    }

}
