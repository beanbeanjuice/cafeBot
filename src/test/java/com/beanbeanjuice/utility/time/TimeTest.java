package com.beanbeanjuice.utility.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TimeTest {

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
}