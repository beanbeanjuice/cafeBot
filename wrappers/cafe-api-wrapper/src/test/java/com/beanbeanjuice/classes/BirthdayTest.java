package com.beanbeanjuice.classes;

import com.beanbeanjuice.cafeapi.cafebot.birthdays.Birthday;
import com.beanbeanjuice.cafeapi.cafebot.birthdays.BirthdayMonth;
import com.beanbeanjuice.cafeapi.exception.program.BirthdayOverfillException;
import com.beanbeanjuice.cafeapi.exception.program.InvalidTimeZoneException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.TimeZone;

public class BirthdayTest {

    @Test
    @DisplayName("Test Birthday Class")
    public void testBirthdayException() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Assertions.assertThrows(InvalidTimeZoneException.class, () -> {
           new Birthday(BirthdayMonth.FEBRUARY, 25, "burger", false);
        });

        Assertions.assertThrows(BirthdayOverfillException.class, () -> {
            new Birthday(BirthdayMonth.FEBRUARY, 30, "EST", false);
        });

        Assertions.assertDoesNotThrow(() -> {
            new Birthday(BirthdayMonth.JANUARY, 1, "UTC", false);
        });
    }

    @Test
    @DisplayName("Test Birthday Format")
    public void testBirthdayFormat() throws ParseException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Birthday birthday = new Birthday(BirthdayMonth.JANUARY, 1, "EST", false);
        Assertions.assertEquals("Wed Jan 01 05:00:00 UTC 2020", birthday.getUTCDate().toString());
    }


}
