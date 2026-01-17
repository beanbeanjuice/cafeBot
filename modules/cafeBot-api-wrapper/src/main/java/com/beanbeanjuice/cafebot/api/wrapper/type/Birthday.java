package com.beanbeanjuice.cafebot.api.wrapper.type;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

@Getter
public class Birthday {

    private final String userId;
    private final int year;
    private final int month;
    private final int day;
    private final ZoneId timeZone;
    private final Instant lastMentionedAt;

    public Birthday(String userId, int year, int month, int day, ZoneId timeZone, @Nullable String lastMentionedAt) {
        this.userId = userId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.timeZone = timeZone;

        this.lastMentionedAt = (lastMentionedAt == null || lastMentionedAt.equals("null") || lastMentionedAt.isBlank()) ? null : Instant.parse(lastMentionedAt);
    }

    public Birthday(String userId, int year, int month, int day, ZoneId timeZone) {
        this.userId = userId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.timeZone = timeZone;

        this.lastMentionedAt = null;
    }

    public Optional<Instant> getLastMentionedAt() {
        return Optional.ofNullable(lastMentionedAt);
    }

}
