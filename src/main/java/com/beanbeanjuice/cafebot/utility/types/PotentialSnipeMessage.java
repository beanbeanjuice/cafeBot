package com.beanbeanjuice.cafebot.utility.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class PotentialSnipeMessage {

    private final String channelId;
    private final User user;
    private final String message;
    private final Instant createdAt;

}
