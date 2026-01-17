package com.beanbeanjuice.cafebot.api.wrapper.api.exception;

import lombok.Getter;
import tools.jackson.databind.JsonNode;

public class ApiRequestException extends RuntimeException{

    @Getter private final int statusCode;
    @Getter private final JsonNode body;

    public ApiRequestException(int statusCode, JsonNode body, String message) {
        super(message);
        this.statusCode = statusCode;
        this.body = body;
    }

}
