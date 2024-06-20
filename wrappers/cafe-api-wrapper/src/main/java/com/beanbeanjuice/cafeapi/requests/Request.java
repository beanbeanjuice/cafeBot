package com.beanbeanjuice.cafeapi.requests;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

/**
 * A class containing the request's {@link Integer status code} and resulting {@link JsonNode data}.
 *
 * @author beanbeanjuice
 */
public class Request {

    @Getter final int statusCode;
    @Getter private final JsonNode data;

    /**
     * Creates a new {@link Request} object.
     * @param statusCode The resulting {@link Integer status code}.
     * @param data The {@link JsonNode data} retrieved from the {@link Request}.
     */
    public Request(int statusCode, JsonNode data) {
        this.statusCode = statusCode;
        this.data = data;
    }

}
