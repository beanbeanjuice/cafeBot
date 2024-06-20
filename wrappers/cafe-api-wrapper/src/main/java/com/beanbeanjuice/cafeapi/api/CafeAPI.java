package com.beanbeanjuice.cafeapi.api;

/**
 * An interface used for {@link com.beanbeanjuice.cafeapi.CafeAPI CafeAPI} Requests.
 *
 * @author beanbeanjuice
 */
public interface CafeAPI {

    /**
     * Updates the {@link String apiKey}.
     * @param apiKey The new {@link String apiKey}.
     */
    void updateAPIKey(String apiKey);

}
