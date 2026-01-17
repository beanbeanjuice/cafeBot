package com.beanbeanjuice.cafebot.api.wrapper.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DiscordServer {

    private final String id;
    private final float balance;
    private final boolean aiEnabled;

}
