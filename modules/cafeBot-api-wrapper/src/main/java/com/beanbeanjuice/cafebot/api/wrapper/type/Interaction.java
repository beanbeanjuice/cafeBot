package com.beanbeanjuice.cafebot.api.wrapper.type;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.InteractionType;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Interaction {

    private final InteractionType type;
    private final String fromId;
    private final String toId;
    private final Instant when;
    private final int numSentFrom;
    private final int numSentTo;

    public Interaction(InteractionType type, String fromId, String toId, String when) {
        this.type = type;
        this.fromId = fromId;
        this.toId = toId;
        this.when = Instant.parse(when);

        this.numSentFrom = 0;
        this.numSentTo = 0;
    }

    public Interaction(InteractionType type, String fromId, String toId, String when, int numSentFrom, int numSentTo) {
        this.type = type;
        this.fromId = fromId;
        this.toId = toId;
        this.when = Instant.parse(when);

        this.numSentFrom = numSentFrom;
        this.numSentTo = numSentTo;
    }

}
