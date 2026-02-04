package com.beanbeanjuice.cafebot.utility.handlers.calendar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class CalendarEvent {

    private final String name;
    private final String description;
    private final Instant start;
    private final Instant end;

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

}
