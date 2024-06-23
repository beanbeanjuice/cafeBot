package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays.Birthday;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays.BirthdayMonth;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.TeaPotException;
import com.beanbeanjuice.cafeapi.wrapper.exception.program.BirthdayOverfillException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class BirthdayTest {

    @Test
    @DisplayName("Birthdays Endpoint Test")
    public void testBirthdaysEndpoint() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the user's birthday doesn't exist before starting.
        Assertions.assertTrue(cafeAPI.getBirthdaysEndpoint().removeUserBirthday("178272524533104642"));

        // Makes sure the user's birthday cannot be found.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.getBirthdaysEndpoint().getUserBirthday("178272524533104642"));

        // Makes sure the user's birthday can be created.
        Assertions.assertTrue(cafeAPI.getBirthdaysEndpoint().createUserBirthday("178272524533104642", new Birthday(BirthdayMonth.DECEMBER, 31, "EST", false)));

        // Makes sure the user's birthday cannot be duplicated.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.getBirthdaysEndpoint().createUserBirthday("178272524533104642", new Birthday(BirthdayMonth.DECEMBER, 20, "EST", false)));

        // Makes sure the month is the same.
        Assertions.assertEquals(BirthdayMonth.DECEMBER, cafeAPI.getBirthdaysEndpoint().getAllBirthdays().get("178272524533104642").getMonth());

        // Makes sure the date is the same.
        Assertions.assertTrue(() -> {
            Optional<Birthday> optionalBirthday = cafeAPI.getBirthdaysEndpoint().getUserBirthday("178272524533104642");
            Assertions.assertTrue(optionalBirthday.isPresent());
            return optionalBirthday.get().getDay() == 31;
        });

        // Makes sure a TeaPotException is thrown when there are more days than in the month.
        Assertions.assertThrows(BirthdayOverfillException.class, () -> cafeAPI.getBirthdaysEndpoint().updateUserBirthday("178272524533104642", new Birthday(BirthdayMonth.FEBRUARY, 30, "EST", false)));

        // Makes sure only a valid month can be set
        Assertions.assertThrows(TeaPotException.class, () -> cafeAPI.getBirthdaysEndpoint().updateUserBirthday("178272524533104642", new Birthday(BirthdayMonth.ERROR, 15, "EST", false)));

        // Makes sure the birthday can be changed.
        Assertions.assertTrue(cafeAPI.getBirthdaysEndpoint().updateUserBirthday("178272524533104642", new Birthday(BirthdayMonth.FEBRUARY, 29, "UTC", false)));

        // Makes sure the changed month is the same.
        Assertions.assertTrue(() -> {
            Optional<Birthday> birthday = cafeAPI.getBirthdaysEndpoint().getUserBirthday("178272524533104642");
            Assertions.assertTrue(birthday.isPresent());
            return birthday.get().getMonth() == BirthdayMonth.FEBRUARY;
        });

        // Makes sure the changed day is the same.
        Assertions.assertTrue(() -> {
            Optional<Birthday> optionalBirthday = cafeAPI.getBirthdaysEndpoint().getUserBirthday("178272524533104642");
            Assertions.assertTrue(optionalBirthday.isPresent());
            return optionalBirthday.get().getDay() == 29;
        });

        // Makes sure that alreadyMentioned is false by default.
        Assertions.assertFalse(() -> {
            Optional<Birthday> optionalBirthday = cafeAPI.getBirthdaysEndpoint().getUserBirthday("178272524533104642");
            Assertions.assertTrue(optionalBirthday.isPresent());
            return optionalBirthday.get().isAlreadyMentioned();
        });

        // Makes sure alreadyMentioned can be updated.
        Assertions.assertTrue(cafeAPI.getBirthdaysEndpoint().updateUserBirthdayMention("178272524533104642", true));

        // Makes sure alreadyMentioned HAS updated.
        Assertions.assertTrue(() -> {
            Optional<Birthday> optionalBirthday = cafeAPI.getBirthdaysEndpoint().getUserBirthday("178272524533104642");
            Assertions.assertTrue(optionalBirthday.isPresent());
            return optionalBirthday.get().isAlreadyMentioned();
        });

        // Makes sure the user's birthday can be removed.
        Assertions.assertTrue(cafeAPI.getBirthdaysEndpoint().removeUserBirthday("178272524533104642"));

        // Makes sure the user no longer exists.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.getBirthdaysEndpoint().getUserBirthday("178272524533104642"));
    }

}
