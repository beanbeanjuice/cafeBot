package com.beanbeanjuice.cafebot.utility.handlers;

import java.util.HashMap;
import java.util.Optional;

public class ConfessionHandler {

    private final HashMap<String, String> confessions = new HashMap<>(); // <MESSAGE_ID, USER_ID>

    // We are purposefully not doing any logging here, as confessions should be as anonymous as possible.
    // However, we still need some form of moderation.
    public void addConfession(String messageId, String userId) {
        confessions.put(messageId, userId);
    }

    public Optional<String> getUserId(String messageId) {
        return Optional.ofNullable(confessions.get(messageId));
    }

}
