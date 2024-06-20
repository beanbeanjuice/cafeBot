package com.beanbeanjuice.cafeapi.exception.api;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.requests.Request;
import lombok.Getter;

import java.util.Optional;

/**
 * A generic {@link RuntimeException} used for {@link CafeAPI CafeAPI} exceptions.
 *
 * @author beanbeanjuice
 */
public class CafeException extends RuntimeException {

    @Getter private final int statusCode;
    @Getter private final String message;
    private final Request request;

    /**
     * Creates a new {@link CafeException}.
     * @param request The {@link Request} that threw the {@link Exception}.
     */
    public CafeException(Request request) {
        super("Error " + request.getStatusCode() + ": " + request.getData().get("message").asText());

        this.statusCode = request.getStatusCode();
        this.message = request.getData().get("message").asText();
        this.request = request;
    }

    /**
     * Creates a new {@link CafeException}.
     * @param statusCode The {@link Integer statusCode} for the {@link Exception}.
     * @param message The {@link String message} for the {@link Exception}.
     */
    public CafeException(int statusCode, String message) {
        super("Error " + statusCode + ": " + message);

        this.statusCode = statusCode;
        this.message = message;
        this.request = null;
    }

    /**
     * @return The {@link Request} that threw the {@link CafeException}.
     */
    public Optional<Request> getRequest() {
        return Optional.ofNullable(request);
    }

}
