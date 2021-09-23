package com.beanbeanjuice.utility.time;

import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Utility class for parsing time.
 */
public class Time {

    @NotNull
    private static String pad(@NotNull Integer number, @NotNull Integer minimum) {
        String str = String.valueOf(number);
        boolean neg = str.startsWith("-");

        if (neg) {
            str = str.substring(1);
        }

        if (str.length() < minimum) {
            return ((neg) ? "-" : "") + new String(new char[minimum - str.length()]).replace("\0", "0") + str;
        } else {
            return str;
        }

    }

    @NotNull
    private static String format(@NotNull String input, @NotNull Calendar calendar) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("y", String.valueOf(calendar.get(Calendar.YEAR)));
        fields.put("yy", pad(calendar.get(Calendar.YEAR) - 2000, 2));
        fields.put("yyyy", pad(calendar.get(Calendar.YEAR), 4));

        String Q;
        String QQQQ;

        if (calendar.get(Calendar.MONTH) <= 3) {
            Q = "1";
            QQQQ = "1st quarter";
        } else if (calendar.get(Calendar.MONTH) <= 6) {
            Q = "2";
            QQQQ = "2nd quarter";
        } else if (calendar.get(Calendar.MONTH) <= 9) {
            Q = "3";
            QQQQ = "3rd quarter";
        } else {
            calendar.get(Calendar.MONTH);
            Q = "4";
            QQQQ = "4th quarter";
        }

        fields.put("Q", Q);
        fields.put("QQQ", "Q" + Q);
        fields.put("QQQQ", QQQQ);

        fields.put("M", String.valueOf(calendar.get(Calendar.MONTH)));
        fields.put("MM", pad(calendar.get(Calendar.MONTH) + 1, 2));

        String month;

        switch (calendar.get(Calendar.MONTH)) {
            case (0) -> month = "January";
            case (1) -> month = "February";
            case (2) -> month = "March";
            case (3) -> month = "April";
            case (4) -> month = "May";
            case (5) -> month = "June";
            case (6) -> month = "July";
            case (7) -> month = "August";
            case (8) -> month = "September";
            case (9) -> month = "October";
            case (10) -> month = "November";
            case (11) -> month = "December";
            default -> month = "UNKNOWN";
        }

        fields.put("MMM", month.substring(0, 3));
        fields.put("MMMM", month);
        fields.put("MMMMM", month.split("")[0]);

        fields.put("d", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        fields.put("dd", pad(calendar.get(Calendar.DAY_OF_MONTH), 2));

        String dayOfWeek;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case (1) -> dayOfWeek = "Sunday";
            case (2) -> dayOfWeek = "Monday";
            case (3) -> dayOfWeek = "Tuesday";
            case (4) -> dayOfWeek = "Wednesday";
            case (5) -> dayOfWeek = "Thursday";
            case (6) -> dayOfWeek = "Friday";
            case (7) -> dayOfWeek = "Saturday";
            default -> dayOfWeek = "UNKNOWN";
        }

        fields.put("E", dayOfWeek.substring(0, 3));
        fields.put("EEEE", dayOfWeek);
        fields.put("EEEEE", dayOfWeek.substring(0, 0));
        fields.put("EEEEEE", dayOfWeek.substring(0, 1));

        fields.put("h", String.valueOf(calendar.get(Calendar.HOUR)));
        fields.put("hh", pad(calendar.get(Calendar.HOUR), 2));
        fields.put("H", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        fields.put("HH", pad(calendar.get(Calendar.HOUR_OF_DAY), 2));
        fields.put("a", (calendar.get(Calendar.HOUR_OF_DAY) <= 12) ? "AM" : "PM");

        fields.put("m", String.valueOf(calendar.get(Calendar.MINUTE)));
        fields.put("mm", pad(calendar.get(Calendar.MINUTE), 2));

        fields.put("s", String.valueOf(calendar.get(Calendar.SECOND)));
        fields.put("ss", pad(calendar.get(Calendar.SECOND), 2));
        fields.put("SSS", String.valueOf(calendar.get(Calendar.MILLISECOND)));

        int tzOffset = calendar.getTimeZone().getRawOffset() / 3600000;

        fields.put("zzz", calendar.getTimeZone().getDisplayName().replaceAll("(([A-Z])[a-z]+( ?){3})", "$2"));
        fields.put("zzzz", calendar.getTimeZone().getDisplayName());
        fields.put("Z", ((tzOffset < 0) ? pad(tzOffset, 2) : "+" + tzOffset) + "00");
        fields.put("ZZZZZ", ((tzOffset < 0) ? pad(tzOffset, 2) : "+" + tzOffset) + ":00");
        fields.put("ZZZZ", fields.get("zzz") + fields.get("ZZZZZ"));

        final String[] formatted = {input};

        fields.forEach((field, value) -> {
            formatted[0] = formatted[0].replaceAll(String.format("\\{%s\\}", field), value);
        });

        return formatted[0];
    }

    private Calendar calendar;
    private TimeZone timeZone;

    public Time(@NotNull Calendar calendar) {
        this.calendar = calendar;
        this.timeZone = calendar.getTimeZone();
    }

    public void setTimeZone(@NotNull ZoneId zone) {
        this.timeZone = TimeZone.getTimeZone(zone);
        this.calendar = Calendar.getInstance(this.timeZone);
    }

    public void setTimeZone(@NotNull TimeZone zone) {
        this.timeZone = zone;
        this.calendar = Calendar.getInstance(this.timeZone);
    }

    @NotNull
    public Long toEpochMilliseconds() {
        return calendar.getTimeInMillis();
    }

    @NotNull
    public Long toEpochSeconds() {
        return calendar.toInstant().getEpochSecond();
    }

    @NotNull
    @Override
    public String toString() {
        return format("{E}, {d} {MMM} {yyyy} {HH}:{mm}:{ss} {Z}", this.calendar);
    }

    @NotNull
    public String toString(@NotNull String format) {
        return format(format, this.calendar);
    }

}