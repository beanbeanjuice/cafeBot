package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.cafebot.birthdays.Birthday;
import com.beanbeanjuice.cafeapi.cafebot.birthdays.BirthdayMonth;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.exception.api.TeaPotException;
import com.beanbeanjuice.cafeapi.exception.program.BirthdayOverfillException;
import com.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class BirthdayTest {

    @Test
    @DisplayName("Birthdays Test API")
    public void testBirthdayAPI() throws ParseException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the user's birthday doesn't exist before starting.
        Assertions.assertTrue(cafeAPI.BIRTHDAY.removeUserBirthday("178272524533104642"));

        // Makes sure the user's birthday cannot be found.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.BIRTHDAY.getUserBirthday("178272524533104642"));

        // Makes sure the user's birthday can be created.
        Assertions.assertTrue(cafeAPI.BIRTHDAY.createUserBirthday("178272524533104642", new Birthday(BirthdayMonth.DECEMBER, 31, "EST", false)));

        // Makes sure the user's birthday cannot be duplicated.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.BIRTHDAY.createUserBirthday("178272524533104642", new Birthday(BirthdayMonth.DECEMBER, 20, "EST", false)));

        // Makes sure the month is the same.
        Assertions.assertEquals(BirthdayMonth.DECEMBER, cafeAPI.BIRTHDAY.getAllBirthdays().get("178272524533104642").getMonth());

        // Makes sure the date is the same.
        Assertions.assertEquals(31, cafeAPI.BIRTHDAY.getUserBirthday("178272524533104642").getDay());

        // Makes sure a TeaPotException is thrown when there are more days than in the month.
        Assertions.assertThrows(BirthdayOverfillException.class, () -> cafeAPI.BIRTHDAY.updateUserBirthday("178272524533104642", new Birthday(BirthdayMonth.FEBRUARY, 30, "EST", false)));

        // Makes sure only a valid month can be set
        Assertions.assertThrows(TeaPotException.class, () -> cafeAPI.BIRTHDAY.updateUserBirthday("178272524533104642", new Birthday(BirthdayMonth.ERROR, 15, "EST", false)));

        // Makes sure the birthday can be changed.
        Assertions.assertTrue(cafeAPI.BIRTHDAY.updateUserBirthday("178272524533104642", new Birthday(BirthdayMonth.FEBRUARY, 29, "UTC", false)));

        // Makes sure the changed month is the same.
        Assertions.assertEquals(BirthdayMonth.FEBRUARY, cafeAPI.BIRTHDAY.getUserBirthday("178272524533104642").getMonth());

        // Makes sure the changed day is the same.
        Assertions.assertEquals(29, cafeAPI.BIRTHDAY.getUserBirthday("178272524533104642").getDay());

        // Makes sure that alreadyMentioned is false by default.
        Assertions.assertFalse(cafeAPI.BIRTHDAY.getUserBirthday("178272524533104642").alreadyMentioned());

        // Makes sure alreadyMentioned can be updated.
        Assertions.assertTrue(cafeAPI.BIRTHDAY.updateUserBirthdayMention("178272524533104642", true));

        // Makes sure alreadyMentioned HAS updated.
        Assertions.assertTrue(cafeAPI.BIRTHDAY.getUserBirthday("178272524533104642").alreadyMentioned());

        // Makes sure the user's birthday can be removed.
        Assertions.assertTrue(cafeAPI.BIRTHDAY.removeUserBirthday("178272524533104642"));

        // Makes sure the user no longer exists.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.BIRTHDAY.getUserBirthday("178272524533104642"));
    }

}
