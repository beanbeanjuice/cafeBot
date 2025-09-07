package com.beanbeanjuice.cafebot.api.wrapper.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServeWord {

    private final int uses;
    private final float reward;
    private final float newBalance;

}
