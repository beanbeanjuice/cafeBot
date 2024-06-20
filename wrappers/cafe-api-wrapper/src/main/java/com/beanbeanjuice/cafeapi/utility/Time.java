package com.beanbeanjuice.cafeapi.utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A class used for keeping track of time.
 *
 * @author beanbeanjuice
 * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">SimpleDateTime Formatting</a>
 * @see <a href="https://garygregory.wordpress.com/2013/06/18/what-are-the-java-timezone-ids/">Timezone IDs</a>
 * @see <a href="https://stackoverflow.com/questions/2517709/comparing-two-java-util-dates-to-see-if-they-are-in-the-same-day">Calendar Help</a>
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
    public Time(String timezone, String defaultFormat) {
        TIME_ZONE = TimeZone.getTimeZone(timezone);
        setDefaultFormat(defaultFormat);
    }

    /**
     * Creates a new {@link Time} object with a specified
     * {@link TimeZone}.
     * @param timezone The {@link String} of the specified {@link TimeZone}.
     */
    public Time(String timezone) {
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
    public void setDefaultFormat(String format) {
        defaultFormat = format;
    }

    /**
     * Format the current time to a specified {@link String format}.
     * @param format The {@link String format} to return.
     * @return The {@link String formatted} {@link Time}.
     */
    public String format(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TIME_ZONE);
        return simpleDateFormat.format(new Date());
    }

    /**
     * Format using the default formatting specified.
     * @return The {@link String formatted} {@link Time}.
     * @throws NullPointerException Thrown when there is no default {@link String format}.
     */
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
    public static Long compareTwoTimeStamps(Timestamp oldTime, Timestamp currentTime, TimestampDifference timestampDifference) {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();
        long diff = milliseconds2 - milliseconds1;

        return switch (timestampDifference) {
            case SECONDS -> diff / 1000;
            case MINUTES -> diff / (60 * 1000);
            case HOURS -> diff / (60 * 60 * 1000);
            case DAYS -> diff / (24 * 60 * 60 * 1000);
            default -> diff;
        };
    }

    /**
     * Format a {@link Timestamp}.
     * @param timestamp The {@link Timestamp} to format.
     * @param format The formatting {@link String}.
     * @return The formatted {@link String}.
     */
    public static String format(Timestamp timestamp, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(timestamp);
    }

    /**
     *
     * @param dateString The {@link String} of the {@link Date}. (MM-dd-yyyy)
     * @param timeZone The {@link TimeZone} of the {@link Date}.
     * @return The formatted {@link Date}.
     * @throws ParseException Thrown if there was an error parsing the {@link Date}.
     */
    public static Date getFullDate(String dateString, TimeZone timeZone) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        format.setTimeZone(timeZone);
        return format.parse(dateString);
    }

    /**
     * Check if a specified {@link String timeZoneString} is a valid {@link TimeZone}.
     * @param timezoneString The {@link String timeZoneString} to check.
     * @return True, if the specified {@link String timeZoneString} is a valid {@link TimeZone}.
     */
    public static boolean isValidTimeZone(String timezoneString) {
        return Set.of(TimeZone.getAvailableIDs()).contains(timezoneString);
    }

    /**
     * Check if a {@link Date} has passed the current {@link Date}.
     * @param date The {@link Date} to check for.
     * @return True, if the {@link Date} has passed the current {@link Date}.
     * @throws ParseException Thrown if there was an error parsing the {@link Date}.
     */
    public static boolean dateHasPassed(Date date) throws ParseException {
        Calendar checkCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        checkCalendar.setTime(date);
        calendar.setTime(new Date());

        // Setting both years to 2020. (2020 has a leap-year)
        checkCalendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.YEAR, 2020);

        return calendar.after(checkCalendar);
    }

    /**
     * Check if the {@link String} of a {@link Date} has passed the current {@link Date}.
     * @param dateString The {@link String} of a {@link Date}.
     * @param timeZone The {@link TimeZone} of the {@link Date}.
     * @return True, if the input {@link String} of a {@link Date} has passed.
     * @throws ParseException Thrown if there was an error parsing the {@link String} of the {@link Date}.
     */
    public static boolean dateHasPassed(String dateString, TimeZone timeZone) throws ParseException {
        return dateHasPassed(getFullDate(dateString, timeZone));
    }

    /**
     * Check if a {@link Date} is the current {@link Date}.
     * @param date The {@link Date} to check for.
     * @return True, if the {@link Date} is the current {@link Date}.
     * @throws ParseException Thrown if there was an error parsing the {@link Date}.
     */
    public static boolean isSameDay(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        SimpleDateFormat yearFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date currentDate = new Date();

        // 2020 is a leap year
        date = yearFormat.parse(format.format(date) + "-2020");
        currentDate = yearFormat.parse(format.format(currentDate) + "-2020");

        // Means they are the same.
        return !currentDate.after(date) && !currentDate.before(date);
    }

    /**
     * Check if the {@link String} of a {@link Date} is the current {@link Date}.
     * @param dateString The {@link String} of a {@link Date}.
     * @param timeZone The {@link TimeZone} of the {@link Date}.
     * @return True, if the input {@link String} of a {@link Date} is the date.
     * @throws ParseException Thrown if there was an error parsing the {@link String} of the {@link Date}.
     */
    public static boolean isSameDay(String dateString, TimeZone timeZone) throws ParseException {
        return isSameDay(getFullDate(dateString, timeZone));
    }

}
