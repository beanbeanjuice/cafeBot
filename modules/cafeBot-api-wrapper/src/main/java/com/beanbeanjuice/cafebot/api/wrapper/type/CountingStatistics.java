package com.beanbeanjuice.cafebot.api.wrapper.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CountingStatistics {

    @Getter final String guildId;
    @Getter private final int highestCount;
    @Getter private final int currentCount;
    private final String lastUserId;

    public Optional<String> getLastUserId() {
        return Optional.ofNullable(lastUserId);
    }

}
