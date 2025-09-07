package com.beanbeanjuice.cafebot.api.wrapper.type;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomChannel {

    private final String guildId;
    private final CustomChannelType type;
    private final String channelId;

}
