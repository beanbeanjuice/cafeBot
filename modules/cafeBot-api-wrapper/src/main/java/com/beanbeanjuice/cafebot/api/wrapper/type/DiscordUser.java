package com.beanbeanjuice.cafebot.api.wrapper.type;

import lombok.Getter;

import java.time.Instant;
import java.util.Optional;

@Getter
public class DiscordUser {

    private final String id;
    private final float balance;
    private final Instant lastServeTime;
    private final Instant lastDonationTime;

    public DiscordUser(String id, float balance, String lastServeTime, String lastDonationTime) {
        this.id = id;
        this.balance = balance;
        this.lastServeTime = lastServeTime == null ? null : Instant.parse(lastServeTime);
        this.lastDonationTime = lastDonationTime == null ? null : Instant.parse(lastDonationTime);
    }

    public Optional<Instant> getLastServeTime() {
        return Optional.ofNullable(lastServeTime);
    }

    public Optional<Instant> getLastDonationTime() {
        return Optional.ofNullable(lastDonationTime);
    }

}
