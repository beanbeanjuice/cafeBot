package com.beanbeanjuice.cafebot.api.wrapper.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VoiceRole {

    private final String guildId;
    private final String roleId;
    private final String channelId;

}
