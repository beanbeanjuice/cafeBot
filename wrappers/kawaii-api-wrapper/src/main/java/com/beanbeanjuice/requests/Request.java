package com.beanbeanjuice.requests;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

/**
 * A class containing the request's {@link Integer status code} and resulting {@link JsonNode data}.
 *
 * @author beanbeanjuice
 * @since v1.0.0
 */
public class Request {

    @Getter private final Integer statusCode;
    @Getter private final JsonNode data;

    /**
     * Creates a new {@link Request} object.
     * @param statusCode The resulting {@link Integer status code}.
     * @param data The {@link JsonNode data} retrieved from the {@link Request}.
     */
    public Request(final Integer statusCode, final JsonNode data) {
        this.statusCode = statusCode;
        this.data = data;
    }

}
