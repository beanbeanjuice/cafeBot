package com.beanbeanjuice.cafeapi.wrapper.api;

/**
 * An interface used for {@link com.beanbeanjuice.cafeapi.wrapper.CafeAPI CafeAPI} Requests.
 *
 * @author beanbeanjuice
 */
public interface ICafeAPI {

    /**
     * Updates the {@link String apiKey}.
     * @param apiKey The new {@link String apiKey}.
     */
    void updateAPIKey(final String apiKey);

}
