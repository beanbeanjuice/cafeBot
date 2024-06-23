package com.beanbeanjuice.cafeapi.wrapper.requests;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;

/**
 * A static class used for {@link RequestType}.
 *
 * @author beanbeanjuice
 */
public enum RequestType {

    GET,
    POST,
    DELETE,
    PATCH;

    public SimpleHttpRequest getRequest(final String apiURL) {
        switch (this) {
            case GET -> {
                return SimpleRequestBuilder.get(apiURL).build();
            }
            case POST -> {
                return SimpleRequestBuilder.post(apiURL).build();
            }
            case DELETE -> {
                return SimpleRequestBuilder.delete(apiURL).build();
            }
            case PATCH -> {
                return SimpleRequestBuilder.patch(apiURL).build();
            }
            case null, default -> throw new IllegalStateException("Unexpected value: " + this);
        }
    }

}
