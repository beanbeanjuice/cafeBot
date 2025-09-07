package com.beanbeanjuice.cafebot.api.wrapper.type;

import lombok.Getter;

import java.time.Instant;

@Getter
public class MenuOrder {

    private final int id;
    private final int itemId;
    private final String fromId;
    private final String toId;
    private final Instant createdAt;

    public MenuOrder(int id, int itemId, String fromId, String toId, String createdAt) {
        this.id = id;
        this.itemId = itemId;
        this.fromId = fromId;
        this.toId = toId;
        this.createdAt = Instant.parse(createdAt);
    }

}
