package com.beanbeanjuice.cafebot.utility.listeners.ai;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PreviousMessage {

    private final String content;
    private final String username;

}
