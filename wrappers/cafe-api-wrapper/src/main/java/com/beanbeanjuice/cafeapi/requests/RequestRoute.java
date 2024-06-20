package com.beanbeanjuice.cafeapi.requests;

import lombok.Getter;

/**
 * A class used for {@link RequestRoute} objects.
 *
 * @author beanbeanjuice
 */
public enum RequestRoute {

    CAFE("/cafe/api/v1"),
    CAFEBOT("/cafeBot/api/v1");

    @Getter private final String route;

    /**
     * Creates a new {@link RequestRoute} static object.
     * @param route The {@link String route} of the {@link Request}.
     */
    RequestRoute(String route) {
        this.route = route;
    }

}
