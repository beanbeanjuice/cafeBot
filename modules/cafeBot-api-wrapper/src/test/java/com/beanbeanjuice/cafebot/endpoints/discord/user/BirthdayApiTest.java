package com.beanbeanjuice.cafebot.endpoints.discord.user;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.Birthday;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class BirthdayApiTest extends ApiTest {

    @Test
    @DisplayName("can get timezones")
    public void testGettingTimezones() throws ExecutionException, InterruptedException {
        String[] timezones = cafeAPI.getBirthdayApi().getTimezones().get();

        Assertions.assertTrue(timezones.length > 0);
        Assertions.assertTrue(Arrays.stream(timezones).anyMatch(timezone -> timezone.equalsIgnoreCase("PST8PDT")));
    }

    @Test
    @DisplayName("can set birthday")
    public void testSettingTimezone() throws ExecutionException, InterruptedException {
        String userId = generateSnowflake().toString();
        Birthday birthday = new Birthday(userId, 2003, 1, 1, ZoneId.of("PST8PDT"));

        Birthday response = cafeAPI.getBirthdayApi().setBirthday(userId, birthday).get();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(birthday.getYear(), response.getYear());
        Assertions.assertEquals(birthday.getMonth(), response.getMonth());
        Assertions.assertEquals(birthday.getDay(), response.getDay());
        Assertions.assertEquals(birthday.getTimeZone(), response.getTimeZone());
        Assertions.assertTrue(response.getLastMentionedAt().isEmpty());
    }

    @Test
    @DisplayName("can delete birthday")
    public void testDeletingBirthday() throws ExecutionException, InterruptedException {
        String userId = generateSnowflake().toString();

        Birthday birthday = new Birthday(userId, 2003, 1, 1, ZoneId.of("PST8PDT"));
        Birthday response = cafeAPI.getBirthdayApi().setBirthday(userId, birthday).get();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(birthday.getYear(), response.getYear());
        Assertions.assertEquals(birthday.getMonth(), response.getMonth());
        Assertions.assertEquals(birthday.getDay(), response.getDay());
        Assertions.assertEquals(birthday.getTimeZone(), response.getTimeZone());
        Assertions.assertEquals(birthday.getLastMentionedAt(), response.getLastMentionedAt());

        cafeAPI.getBirthdayApi().deleteBirthday(userId).get();

        ExecutionException ex = Assertions.assertThrows(
                ExecutionException.class,
                () -> cafeAPI.getBirthdayApi().getBirthday(userId).get()
        );

        Assertions.assertInstanceOf(ApiRequestException.class, ex.getCause());
    }

    @Test
    @DisplayName("can get birthday")
    public void testGettingBirthday() throws ExecutionException, InterruptedException {
        String userId = generateSnowflake().toString();

        Birthday birthday = new Birthday(userId, 2004, 4, 4, ZoneId.of("PST8PDT"));
        cafeAPI.getBirthdayApi().setBirthday(userId, birthday).get();

        Birthday response = cafeAPI.getBirthdayApi().getBirthday(userId).get();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(birthday.getYear(), response.getYear());
        Assertions.assertEquals(birthday.getMonth(), response.getMonth());
        Assertions.assertEquals(birthday.getDay(), response.getDay());
        Assertions.assertEquals(birthday.getTimeZone(), response.getTimeZone());
    }

    @Test
    @DisplayName("can update birthday mention")
    public void testUpdatingBirthdayMention() throws ExecutionException, InterruptedException {
        String userId = generateSnowflake().toString();

        Birthday birthday = new Birthday(userId, 2004, 4, 4, ZoneId.of("PST8PDT"));
        cafeAPI.getBirthdayApi().setBirthday(userId, birthday).get();

        Birthday birthday2 = cafeAPI.getBirthdayApi().getBirthday(userId).get();
        Assertions.assertTrue(birthday2.getLastMentionedAt().isEmpty());

        cafeAPI.getBirthdayApi().updateBirthdayMention(userId).get();

        Birthday birthday3 = cafeAPI.getBirthdayApi().getBirthday(userId).get();
        Assertions.assertFalse(birthday3.getLastMentionedAt().isEmpty());
    }

    @Test
    @DisplayName("should return birthday occurring this hour in some timezone")
    public void testBirthdayNow() throws ExecutionException, InterruptedException {
        ZoneId targetZone = null;
        LocalDateTime nowUtc = LocalDateTime.now(ZoneOffset.UTC);

        // Iterate over available timezones until we find one where local time is between 0:00 and 0:59
        for (String tzStr : ZoneId.getAvailableZoneIds()) {
            ZoneId zone = ZoneId.of(tzStr);
            LocalTime localTime = LocalDateTime.now(zone).toLocalTime();
            if (localTime.getHour() == 0) {
                targetZone = zone;
                break;
            }
        }

        Assertions.assertNotNull(targetZone, "No suitable timezone found currently at midnight");

        LocalDateTime localNow = LocalDateTime.now(targetZone);

        String user1Id = generateSnowflake().toString();
        String user2Id = generateSnowflake().toString();

        Birthday birthday1 = new Birthday(
                user1Id,
                2004,
                localNow.getMonthValue(),
                localNow.getDayOfMonth(),
                targetZone
        );

        Birthday birthday2 = new Birthday(
                user2Id,
                2004,
                localNow.getMonthValue(),
                localNow.getDayOfMonth(),
                targetZone
        );

        cafeAPI.getBirthdayApi().setBirthday(user1Id, birthday1).get();
        cafeAPI.getBirthdayApi().setBirthday(user2Id, birthday2).get();

        ArrayList<Birthday> birthdaysNow = cafeAPI.getBirthdayApi().getCurrentBirthdays().get();

        Assertions.assertTrue(birthdaysNow.stream().anyMatch((bday) -> bday.getUserId().equals(user1Id)));
        Assertions.assertTrue(birthdaysNow.stream().anyMatch((bday) -> bday.getUserId().equals(user2Id)));
    }

}
