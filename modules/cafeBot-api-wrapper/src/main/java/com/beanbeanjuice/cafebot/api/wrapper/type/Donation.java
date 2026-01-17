package com.beanbeanjuice.cafebot.api.wrapper.type;

import lombok.Getter;

import java.time.Instant;

@Getter
public class Donation {

    /**
     * The user ID of the person who sent the {@link Donation}.
     */
    private final String from;

    /**
     * The user ID of the person who received the {@link Donation}.
     */
    private final String to;

    private final int amount;

    /**
     * The ISO 8601 date (e.g. {@code 2025-11-09T18:12:30.000Z})
     */
    private final Instant when;

    public Donation(String from, String to, int amount, String when) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.when = Instant.parse(when);
    }

}
