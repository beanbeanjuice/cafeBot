package com.beanbeanjuice.kawaiiapi.wrapper;

import com.beanbeanjuice.kawaiiapi.wrapper.endpoint.GifEndpoint;

/**
 * A class used for the {@link KawaiiAPI}.
 *
 * @author beanbeanjuice
 * @since 1.1.1
 */
public class KawaiiAPI {

    public GifEndpoint GIF;

    /**
     * Create a new {@link KawaiiAPI} object.
     * @param token The {@link String token} for keeping track of statistics.
     * @see <a href="https://docs.kawaii.red/tutorials/token">Token</a>
     * @see <a href="https://docs.kawaii.red/">Kawaii API Documentation</a>
     */
    public KawaiiAPI(final String token) {
        initialiseEndPoints(token);
    }

    /**
     * Create a new, anonymous, {@link KawaiiAPI} object.
     * @see <a href="https://docs.kawaii.red/tutorials/token">Token</a>
     * @see <a href="https://docs.kawaii.red/">Kawaii API Documentation</a>
     */
    public KawaiiAPI() {
        initialiseEndPoints("anonymous");
    }

    private void initialiseEndPoints(final String token) {
        GIF = new GifEndpoint(token);
    }

}
