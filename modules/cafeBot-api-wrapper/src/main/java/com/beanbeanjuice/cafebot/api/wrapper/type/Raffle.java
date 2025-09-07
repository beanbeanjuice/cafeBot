package com.beanbeanjuice.cafebot.api.wrapper.type;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

@Getter
public class Raffle {

    private final int id;
    private final String guildId;
    private final String messageId;

    private final String title;
    private final String description;
    private final int numWinners;
    private final boolean isActive;
    private final Instant endsAt;

    private final String[] submissions;
    private final String[] winners;

    public Raffle(int id, String guildId, @Nullable String messageId, String title, String description, int numWinners, boolean isActive, String endsAt, @Nullable String[] submissions, @Nullable String[] winners) {
        this.id = id;
        this.guildId = guildId;
        this.messageId = messageId;

        this.title = title;
        this.description = description;
        this.numWinners = numWinners;
        this.isActive = isActive;
        this.endsAt = Instant.parse(endsAt);

        this.submissions = submissions;
        this.winners = winners;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(this.description);
    }

}
