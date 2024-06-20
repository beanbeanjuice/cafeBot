package com.beanbeanjuice.cafeapi.exception.api;

import com.beanbeanjuice.cafeapi.requests.Request;

/**
 * A {@link CafeException} used when a {@link Request} responds with a status code of 404.
 *
 * @author beanbeanjuice
 */
public class NotFoundException extends CafeException {

    /**
     * Creates a new {@link NotFoundException}.
     * @param request The {@link Request} that threw the {@link CafeException}.
     */
    public NotFoundException(Request request) {
        super(request);
    }

}
