package com.beanbeanjuice.cafebot.api.wrapper.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.JsonNode;

@Getter
@RequiredArgsConstructor
public class BasicResponse {

    private final int statusCode;
    private final JsonNode body;

}
