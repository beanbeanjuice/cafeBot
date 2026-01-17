package com.beanbeanjuice.cafebot.utility.handlers;

import com.beanbeanjuice.cafebot.utility.types.PotentialSnipeMessage;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SnipeHandler {

    // key=messageId, value=actual message
    private final Map<String, PotentialSnipeMessage> potentialMessages = new HashMap<>();

    // key=channelId, value=actual message
    private final Map<String, PotentialSnipeMessage> snipeMessages = new HashMap<>();

    public void addPotentialMessage(String messageId, PotentialSnipeMessage message) {
        potentialMessages.put(messageId, message);
    }

    public void convertToSnipe(String messageId) {
        PotentialSnipeMessage snipeMessage = potentialMessages.remove(messageId);
        if (snipeMessage == null) return;
        snipeMessages.put(snipeMessage.getChannelId(), snipeMessage);
    }

    public Optional<PotentialSnipeMessage> popLastMessage(String channelId) {
        return Optional.ofNullable(snipeMessages.remove(channelId));
    }

    public void removeOldMessages() {
        synchronized (potentialMessages) {
            var temp = potentialMessages.entrySet().stream().filter((entry) -> {
                PotentialSnipeMessage message = entry.getValue();

                Instant cutoff = Instant.now().minus(10, ChronoUnit.MINUTES);

                return !message.getCreatedAt().isBefore(cutoff);
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            potentialMessages.clear();
            potentialMessages.putAll(temp);
        }

        synchronized (snipeMessages) {
            var temp = snipeMessages.entrySet().stream().filter((entry) -> {
                PotentialSnipeMessage message = entry.getValue();

                Instant cutoff = Instant.now().minus(10, ChronoUnit.MINUTES);

                return !message.getCreatedAt().isBefore(cutoff);
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            snipeMessages.clear();
            snipeMessages.putAll(temp);
        }
    }

}
