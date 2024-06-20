package com.beanbeanjuice.cafeapi.exception.api;

import com.beanbeanjuice.cafeapi.requests.Request;

/**
 * An {@link CafeException} that is thrown when mismatched variables are present.
 * For example, trying to input a boolean value as "falfse" instead of "false".
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/418">418: I'm A Teapot</a>
 *
 * @author beanbeanjuice
 */
public class TeaPotException extends CafeException {

    /**
     * Creates a new {@link TeaPotException}.
     * @param request The {@link Request} that threw the {@link CafeException}.
     */
    public TeaPotException(Request request) {
        super(request);
    }

    /**
     * Creates a new {@link TeaPotException}.
     * @param message The {@link String} message to send.
     */
    public TeaPotException(String message) {
        super(413, message);
    }

}
