package com.beanbeanjuice.cafeapi.model;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Response {

    private final String message;

    public static Map<String, Object> of(String message) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("message", message);
        return bodyMap;
    }

    public static Map<String, Object> error(String message, String errorMessage) {
        return of(String.format(message, errorMessage));
    }

    public static Response message(String message) {
        return new Response(message);
    }

    public Map<String, Object> body(String title, Object body) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("message", this.message);
        bodyMap.put(title, body);
        return bodyMap;
    }

}
