package com.beanbeanjuice.cafeapi.generic;

import com.beanbeanjuice.cafeapi.CafeAPI;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.TimeZone;

/**
 * A generic class used for general parsing and other various methods.
 *
 * @author beanbeanjuice
 */
public class CafeGeneric {

    /**
     * Parses a {@link Timestamp} retrieved from the {@link CafeAPI CafeAPI}.
     * @param timestampString The {@link String timestampString} retrieved from the {@link CafeAPI CafeAPI}.
     * @return The parsed {@link Timestamp timestamp} retrieved from the {@link CafeAPI CafeAPI}. UTC Timezone.
     * Null if timestamp was incorrectly entered.
     * @throws IllegalArgumentException - Thrown when the pattern given is invalid.
     */
    public static Optional<Timestamp> parseTimestamp(String timestampString) throws IllegalArgumentException {
        timestampString = timestampString.replace("T", " ").replace("Z", "");
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Sets the timezone to UTC.
            return Optional.of(new Timestamp(simpleDateFormat.parse(timestampString).getTime()));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses a {@link Timestamp} retrieved from the {@link CafeAPI CafeAPI}.
     * @param timestampString The {@link String timestampString} retrieved from the {@link CafeAPI CafeAPI}.
     * @return The parsed {@link Timestamp timestamp} retrieved from the {@link CafeAPI CafeAPI}. UTC Timezone.
     * Null if timestamp was incorrectly entered.
     * @throws IllegalArgumentException - Thrown when the pattern given is invalid.
     */
    public static Optional<Timestamp> parseTimestampFromAPI(String timestampString) throws IllegalArgumentException {
        timestampString = timestampString.replace("T", " ").replace("Z", "");
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return Optional.of(new Timestamp(simpleDateFormat.parse(timestampString).getTime()));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses a {@link Date} retrieved from the {@link CafeAPI CafeAPI}.
     * @param dateString The {@link String dateString} retrieved from the {@link CafeAPI CafeAPI}.
     * @return The parsed {@link Date} retrieved from the {@link CafeAPI CafeAPI}. UTC Timezone.
     * Null if the date was incorrectly entered.
     * @throws IllegalArgumentException - Thrown when the pattern given is invalid.
     */
    public static Optional<Date> parseDateFromAPI(String dateString) throws IllegalArgumentException {
        dateString = dateString.replace("T", " ").replace("Z", "");
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return Optional.of(new Date(simpleDateFormat.parse(dateString).getTime()));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses a {@link Boolean} into its number value.
     * @param bool The {@link Boolean} to parse.
     * @return "1", if true. If there was an error, it will be "0" by default.
     */
    public static String parseBoolean(boolean bool) {
        return (bool) ? "1" : "0";
    }

}
