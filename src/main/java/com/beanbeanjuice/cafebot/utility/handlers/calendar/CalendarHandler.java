package com.beanbeanjuice.cafebot.utility.handlers.calendar;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class CalendarHandler {

    private static List<CalendarEvent> getEvents(String calendarUrl) throws MalformedURLException, URISyntaxException {
        URL url = new URI(calendarUrl).toURL();
        try (InputStream in = url.openStream()) {
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(in);

            Instant now = Instant.now();
            Instant nextWeek = now.plus(7, ChronoUnit.DAYS);

            Period<ZonedDateTime> period = new Period<>(
                    now.atZone(ZoneOffset.UTC),
                    nextWeek.atZone(ZoneOffset.UTC)
            );

            List<CalendarEvent> events = new ArrayList<>();

            for (CalendarComponent component : calendar.getComponents(Component.VEVENT)) {
                VEvent event = (VEvent) component;

                // Skip all-day events (DTSTART is DATE, not DATE-TIME)
                boolean isAllDay = event.getDateTimeStart()
                        .getParameter(Parameter.VALUE)
                        .map(Value.DATE::equals)
                        .orElse(false);
                if (isAllDay) continue;

                Set<Period<ZonedDateTime>> occurrences =
                        event.calculateRecurrenceSet(period);

                for (Period<ZonedDateTime> occurrence : occurrences) {

                    String summary = (event.getSummary() != null) ? event.getSummary().getValue() : null;
                    String description = (event.getDescription() != null) ? event.getDescription().getValue() : null;

                    events.add(new CalendarEvent(summary, description, occurrence.getStart().toInstant(), occurrence.getEnd().toInstant()));
                }
            }

            return events.stream().sorted(Comparator.comparing(CalendarEvent::getStart)).toList();
        } catch (ParserException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCalendarMessage(String calendarUrl, ZoneId zoneId) {
        StringBuilder sb = new StringBuilder();

        List<CalendarEvent> events;

        try {
            events = getEvents(calendarUrl);
        } catch (MalformedURLException | URISyntaxException ex) {
            return "Error Getting Calendar";
        }

        // Group events by local date
        Map<String, List<CalendarEvent>> eventsByDay =
                events.stream()
                        .collect(Collectors.groupingBy(
                                event -> event.getStart()
                                        .atZone(zoneId)
                                        .format(DateTimeFormatter.ofPattern("EEEE (MMMM d, yyyy)")),
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        sb.append("# Calendar (Next 7 Days)\n");

        for (Map.Entry<String, List<CalendarEvent>> entry : eventsByDay.entrySet()) {
            String dayName = entry.getKey();
            List<CalendarEvent> dayEvents = entry.getValue();

            sb.append("## ").append(dayName).append("\n\n");

            for (CalendarEvent event : dayEvents) {
                sb.append("* **").append(event.getName()).append("** - ")
                        .append("<t:").append(event.getStart().getEpochSecond()).append(">")
                        .append(" -> ")
                        .append("<t:").append(event.getEnd().getEpochSecond()).append(">")
                        .append("\n");

                event.getDescription().ifPresent(desc ->
                        sb.append("*").append(desc).append("*")
                );
            }
        }

        return sb.toString();
    }

}
