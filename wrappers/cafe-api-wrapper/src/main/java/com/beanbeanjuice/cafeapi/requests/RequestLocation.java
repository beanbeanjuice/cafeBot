package com.beanbeanjuice.cafeapi.requests;

import lombok.Getter;

public enum RequestLocation {

    RELEASE ("http://beanbeanjuice.com:5100"),
    BETA ("http://beanbeanjuice.com:5101"),
    LOCAL ("http://localhost:5101");

    @Getter private final String URL;

    /**
     * Creates a new static {@link RequestLocation}.
     * @param url The {@link String url} for the {@link RequestLocation}.
     */
    RequestLocation(String url) {
        this.URL = url;
    }

}
