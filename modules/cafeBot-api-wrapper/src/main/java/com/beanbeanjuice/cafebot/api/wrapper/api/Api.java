package com.beanbeanjuice.cafebot.api.wrapper.api;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Api {

    protected final String baseUrl;
    protected final String token;

}
