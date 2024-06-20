package com.beanbeanjuice.cafeapi.exception.api;

import com.beanbeanjuice.cafeapi.requests.Request;

/**
 * A {@link CafeException} used when a {@link Request} responds with a status code of 500.
 *
 * @author beanbeanjuice
 */
public class ResponseException extends CafeException {

    /**
     * Creates a new {@link ResponseException}.
     * @param request The {@link Request} that threw the {@link CafeException}.
     */
    public ResponseException(Request request) {
        super(request);
    }

}
