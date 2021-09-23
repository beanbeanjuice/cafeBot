package io.github.beanbeanjuice.cafeapi.beta;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.cafebot.birthdays.BirthdayMonth;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import io.github.beanbeanjuice.cafeapi.exception.TeaPotException;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BirthdayTest {

    @Test
    @DisplayName("Birthdays Test API")
    public void testBirthdayAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the user's birthday doesn't exist before starting.
        Assertions.assertTrue(cafeAPI.birthdays().removeUserBirthday("178272524533104642"));

        // Makes sure the user's birthday cannot be found.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.birthdays().getUserBirthday("178272524533104642"));

        // Makes sure the user's birthday can be created.
        Assertions.assertTrue(cafeAPI.birthdays().createUserBirthday("178272524533104642", BirthdayMonth.DECEMBER, 31));

        // Makes sure the user's birthday cannot be duplicated.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.birthdays().createUserBirthday("178272524533104642", BirthdayMonth.DECEMBER, 20));

        // Makes sure the month is the same.
        Assertions.assertEquals(BirthdayMonth.DECEMBER, cafeAPI.birthdays().getAllBirthdays().get("178272524533104642").getMonth());

        // Makes sure the date is the same.
        Assertions.assertEquals(31, cafeAPI.birthdays().getUserBirthday("178272524533104642").getDay());

        // Makes sure a TeaPotException is thrown when there are more days than in the month.
        Assertions.assertThrows(TeaPotException.class, () -> cafeAPI.birthdays().updateUserBirthday("178272524533104642", BirthdayMonth.FEBRUARY, 30));

        // Makes sure only a valid month can be set
        Assertions.assertThrows(TeaPotException.class, () -> cafeAPI.birthdays().updateUserBirthday("178272524533104642", BirthdayMonth.ERROR, 10));

        // Makes sure the birthday can be changed.
        Assertions.assertTrue(cafeAPI.birthdays().updateUserBirthday("178272524533104642", BirthdayMonth.FEBRUARY, 29));

        // Makes sure the changed month is the same.
        Assertions.assertEquals(BirthdayMonth.FEBRUARY, cafeAPI.birthdays().getUserBirthday("178272524533104642").getMonth());

        // Makes sure the changed day is the same.
        Assertions.assertEquals(29, cafeAPI.birthdays().getUserBirthday("178272524533104642").getDay());

        // Makes sure that alreadyMentioned is false by default.
        Assertions.assertFalse(cafeAPI.birthdays().getUserBirthday("178272524533104642").alreadyMentioned());

        // Makes sure alreadyMentioned can be updated.
        Assertions.assertTrue(cafeAPI.birthdays().updateUserBirthdayMention("178272524533104642", true));

        // Makes sure alreadyMentioned HAS updated.
        Assertions.assertTrue(cafeAPI.birthdays().getUserBirthday("178272524533104642").alreadyMentioned());

        // Makes sure the user's birthday can be removed.
        Assertions.assertTrue(cafeAPI.birthdays().removeUserBirthday("178272524533104642"));

        // Makes sure the user no longer exists.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.birthdays().getUserBirthday("178272524533104642"));
    }

}
