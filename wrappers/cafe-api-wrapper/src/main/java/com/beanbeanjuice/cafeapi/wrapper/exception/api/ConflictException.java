package com.beanbeanjuice.cafeapi.wrapper.exception.api;

import com.beanbeanjuice.cafeapi.wrapper.requests.Request;

/**
 * A {@link CafeException} used when a {@link Request} responds with a status code of 409.
 *
 * @author beanbeanjuice
 */
public class ConflictException extends CafeException {

    /**
     * Creates a new {@link ConflictException}.
     * @param request The {@link Request} that threw the {@link CafeException}.
     */
    public ConflictException(Request request) {
        super(request);
    }

}
