package com.beanbeanjuice.cafebot;

import com.beanbeanjuice.utility.time.Time;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.TimeZone;

public class TimeTest {

    @Test
    @DisplayName("Default Time Test")
    public void testDefaultTime() {
        Time t = new Time(Calendar.getInstance(TimeZone.getDefault()));

        System.out.println(t);
    }

    @Test
    @DisplayName("Custom Time Test")
    public void testCustomTime() {
        Time t = new Time(Calendar.getInstance(TimeZone.getDefault()));

        System.out.println(t.toString("{MM}-{dd}-{yyyy} {HH}:{mm} {ZZZZ}"));
    }

}
