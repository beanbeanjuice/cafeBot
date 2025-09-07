package com.beanbeanjuice.cafebot.api.wrapper.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MutualGuild {

    private final String userId;
    private final String guildId;

}
