package com.beanbeanjuice.cafeapi.wrapper.exception.api;

import com.beanbeanjuice.cafeapi.wrapper.requests.Request;

/**
 * A {@link CafeException} used when a {@link Request} responds with a status code of 400.
 *
 * @author beanbeanjuice
 */
public class UndefinedVariableException extends CafeException {

    /**
     * Creates a new {@link UndefinedVariableException}.
     * @param request The {@link Request} that threw the {@link CafeException}.
     */
    public UndefinedVariableException(Request request) {
        super(request);
    }

}
