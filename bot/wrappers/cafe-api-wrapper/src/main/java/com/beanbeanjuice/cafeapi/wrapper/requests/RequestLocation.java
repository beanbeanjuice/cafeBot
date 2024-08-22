package com.beanbeanjuice.cafeapi.wrapper.requests;

import lombok.Getter;

public enum RequestLocation {

    RELEASE ("https://api.beanbeanjuice.com"),
    BETA ("https://beta-api.beanbeanjuice.com"),
    LOCAL ("http://localhost:5101");

    @Getter private final String URL;

    /**
     * Creates a new static {@link RequestLocation}.
     * @param url The {@link String url} for the {@link RequestLocation}.
     */
    RequestLocation(final String url) {
        this.URL = url;
    }

}
