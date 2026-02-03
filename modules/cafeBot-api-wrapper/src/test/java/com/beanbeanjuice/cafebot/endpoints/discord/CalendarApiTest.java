package com.beanbeanjuice.cafebot.endpoints.discord;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.OwnerType;
import com.beanbeanjuice.cafebot.api.wrapper.type.calendar.Calendar;
import com.beanbeanjuice.cafebot.api.wrapper.type.calendar.PartialCalendar;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CalendarApiTest extends ApiTest {

    private String userId;
    private String guildId;
    private Calendar userCalendar;
    private Calendar guildCalendar;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException {
        userId = generateSnowflake().toString();
        guildId = generateSnowflake().toString();

        PartialCalendar partialUserCalendar = new PartialCalendar(OwnerType.DISCORD_USER, userId, "Test User Calendar", "https://user.example.ics");
        PartialCalendar partialGuildCalendar = new PartialCalendar(OwnerType.GUILD, guildId, "Test Guild Calendar", "https://guild.example.ics");

        userCalendar = cafeAPI.getCalendarApi().createCalendar(partialUserCalendar).get();
        guildCalendar = cafeAPI.getCalendarApi().createCalendar(partialGuildCalendar).get();
    }

    @Test
    @DisplayName("can create user calendar")
    public void canCreateUserCalendar() throws ExecutionException, InterruptedException {
        String tempUserId = generateSnowflake().toString();
        PartialCalendar partialCalendar = new PartialCalendar(OwnerType.DISCORD_USER, tempUserId, "Example User Calendar", "https://some.calendar.ics");

        Calendar calendar = cafeAPI.getCalendarApi().createCalendar(partialCalendar).get();

        Assertions.assertEquals("Example User Calendar", calendar.getName());
        Assertions.assertEquals("https://some.calendar.ics", calendar.getUrl());
        Assertions.assertEquals(OwnerType.DISCORD_USER, calendar.getOwnerType());
        Assertions.assertEquals(tempUserId, calendar.getOwnerId());
    }

    @Test
    @DisplayName("can create guild calendar")
    public void canCreateGuildCalendar() throws ExecutionException, InterruptedException {
        String tempGuildId = generateSnowflake().toString();
        PartialCalendar partialCalendar = new PartialCalendar(OwnerType.GUILD, tempGuildId, "Example Guild Calendar", "https://another.calendar.ics");

        Calendar calendar = cafeAPI.getCalendarApi().createCalendar(partialCalendar).get();

        Assertions.assertEquals("Example Guild Calendar", calendar.getName());
        Assertions.assertEquals("https://another.calendar.ics", calendar.getUrl());
        Assertions.assertEquals(OwnerType.GUILD, calendar.getOwnerType());
        Assertions.assertEquals(tempGuildId, calendar.getOwnerId());
    }

    @Test
    @DisplayName("can get specific user calendar")
    public void canGetSpecificUserCalendar() throws ExecutionException, InterruptedException {
        Calendar calendar = cafeAPI.getCalendarApi().getCalendar(userCalendar.getId()).get();

        Assertions.assertEquals(calendar.getId(), userCalendar.getId());
        Assertions.assertEquals(calendar.getOwnerType(), userCalendar.getOwnerType());
        Assertions.assertEquals(calendar.getOwnerId(), userCalendar.getOwnerId());
        Assertions.assertEquals(calendar.getName(), userCalendar.getName());
        Assertions.assertEquals(calendar.getUrl(), userCalendar.getUrl());
    }

    @Test
    @DisplayName("can get specific guild calendar")
    public void canGetSpecificGuildCalendar() throws ExecutionException, InterruptedException {
        Calendar calendar = cafeAPI.getCalendarApi().getCalendar(guildCalendar.getId()).get();

        Assertions.assertEquals(calendar.getId(), guildCalendar.getId());
        Assertions.assertEquals(calendar.getOwnerType(), guildCalendar.getOwnerType());
        Assertions.assertEquals(calendar.getOwnerId(), guildCalendar.getOwnerId());
        Assertions.assertEquals(calendar.getName(), guildCalendar.getName());
        Assertions.assertEquals(calendar.getUrl(), guildCalendar.getUrl());
    }

    @Test
    @DisplayName("can get multiple user calendars")
    public void canGetMultipleUserCalendars() throws ExecutionException, InterruptedException {
        List<Calendar> calendars = cafeAPI.getCalendarApi().getUserCalendars(userId).get();

        Assertions.assertEquals(1, calendars.size());
        Assertions.assertEquals("Test User Calendar", calendars.get(0).getName());
        Assertions.assertEquals("https://user.example.ics", calendars.get(0).getUrl());
    }

    @Test
    @DisplayName("can get multiple guild calendars")
    public void canGetMultipleGuildCalendars() throws ExecutionException, InterruptedException {
        List<Calendar> calendars = cafeAPI.getCalendarApi().getGuildCalendars(guildId).get();

        Assertions.assertEquals(1, calendars.size());
        Assertions.assertEquals("Test Guild Calendar", calendars.get(0).getName());
        Assertions.assertEquals("https://guild.example.ics", calendars.get(0).getUrl());
    }

    @Test
    @DisplayName("can delete calendar")
    public void canDeleteCalendar() throws ExecutionException, InterruptedException {
        cafeAPI.getCalendarApi().deleteCalendar(userCalendar.getId()).get();

        Assertions.assertThrows(ExecutionException.class, () -> {
            cafeAPI.getCalendarApi().getCalendar(userCalendar.getId()).get();
        });
    }

}
