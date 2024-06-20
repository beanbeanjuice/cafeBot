package com.beanbeanjuice.cafeapi.utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TimeTest {

    @Test
    @DisplayName("Testing UTC Time")
    public void testUTCTime() {
        Time time = new Time();
        String format = "MM-dd-yyyy HH:mm";

        time.setDefaultFormat(format);
        String[] instantTime = Instant.now().toString().split("T");

        assertEquals(instantTime[0], time.format("yyyy-MM-dd"));
        assertTrue(instantTime[1].startsWith(time.format("HH:mm")));
    }

    @Test
    @DisplayName("Testing Throw if Default Time is Null")
    public void testNullDefaultTime() {
        Time time = new Time();
        assertThrows(NullPointerException.class, time::format);
        time.setDefaultFormat("MM-dd-yyyy");
        assertDoesNotThrow(() -> { time.format(); });
    }

    @Test
    @DisplayName("Convert Date")
    public void testUTCConversion() throws ParseException, InterruptedException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Date date = Time.getFullDate("02-02-2020", TimeZone.getTimeZone("UTC"));
        assertEquals("Sun Feb 02 00:00:00 UTC 2020", date.toString());
    }

    @Test
    @DisplayName("Test Valid Timezone")
    public void testValidTimezone() {
        assertFalse(Time.isValidTimeZone("bruh"));
        assertFalse(Time.isValidTimeZone("est"));
        assertTrue(Time.isValidTimeZone("EST"));
    }

    @Test
    @DisplayName("Test Date Passed")
    public void testDatePassing() throws ParseException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        assertTrue(Time.dateHasPassed(Time.getFullDate("02-02-2020", TimeZone.getTimeZone("EST"))));
        assertTrue(Time.isSameDay(new Date()));
        assertFalse(Time.dateHasPassed(new Date(System.currentTimeMillis() + 120)));

        Date birthday = Time.getFullDate("01-6-2020", TimeZone.getTimeZone("EST"));
        assertTrue(Time.dateHasPassed(birthday));
        assertFalse(Time.dateHasPassed(new Date()));

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat all = new SimpleDateFormat("MM-dd-yyyy");
        TimeZone timeZone = TimeZone.getTimeZone("CET");
        monthFormat.setTimeZone(timeZone);
        dayFormat.setTimeZone(timeZone);
        all.setTimeZone(timeZone);

        Date currentDate = new Date();
        String month = monthFormat.format(currentDate);
        int day = Integer.parseInt(dayFormat.format(currentDate)) + 1;  // Current DAY + 1.
        Date date = all.parse(month + "-" + day + "-2020");

        assertTrue(Time.isSameDay(date));
        assertFalse(Time.dateHasPassed(date));
    }

    @Test
    @DisplayName("Test Current Date")
    public void testCurrentDate() throws ParseException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        assertTrue(Time.isSameDay(new Date()));
        assertFalse(Time.isSameDay("02-02-2020", TimeZone.getTimeZone("EST")));
    }

}
