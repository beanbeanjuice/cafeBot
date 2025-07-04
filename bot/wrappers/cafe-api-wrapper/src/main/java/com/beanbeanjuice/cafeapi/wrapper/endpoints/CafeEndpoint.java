package com.beanbeanjuice.cafeapi.wrapper.endpoints;

public abstract class CafeEndpoint {

    protected static String apiKey;

    public static void updateAPIKey(final String apiKey) {
        CafeEndpoint.apiKey = apiKey;
    }

}
