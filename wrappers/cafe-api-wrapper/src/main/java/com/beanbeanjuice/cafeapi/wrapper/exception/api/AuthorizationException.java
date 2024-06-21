package com.beanbeanjuice.cafeapi.wrapper.exception.api;

import com.beanbeanjuice.cafeapi.wrapper.requests.Request;

/**
 * A {@link CafeException} used when a {@link Request Request} responds with a status code of 401.
 *
 * @author beanbeanjuice
 */
public class AuthorizationException extends CafeException {

    /**
     * Creates a new {@link AuthorizationException}.
     * @param request The {@link Request} that threw the {@link CafeException}.
     */
    public AuthorizationException(final Request request) {
        super(request);
    }

}
