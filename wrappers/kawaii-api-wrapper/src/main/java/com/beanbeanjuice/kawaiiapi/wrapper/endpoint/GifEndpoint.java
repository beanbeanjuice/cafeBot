package com.beanbeanjuice.kawaiiapi.wrapper.endpoint;

import com.beanbeanjuice.kawaiiapi.wrapper.api.API;
import com.beanbeanjuice.kawaiiapi.wrapper.requests.Request;
import com.beanbeanjuice.kawaiiapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.kawaiiapi.wrapper.requests.RequestRoute;

import java.util.Optional;

/**
 * A class used for the GIF endpoint.
 *
 * @author beanbeanjuice
 * @since 1.0.0
 */
public class GifEndpoint extends API {

    /**
     * Creates a new {@link GifEndpoint} class.
     * @param apiKey The {@link String apiKey} to make requests.
     */
    public GifEndpoint(final String apiKey) {
        super(apiKey);
    }

    /**
     * Get a random GIF image for the prompt.
     * @param prompt The {@link String prompt} specified.
     * @return The {@link Optional<String> link} to the gif.
     * @see <a href="https://docs.kawaii.red/endpoints/gif">Available Prompts</a>
     */
    public Optional<String> getGIF(final String prompt) {
        Optional<Request> request = new RequestBuilder(RequestRoute.GIF)
                .setAuthorization(apiKey)
                .setParameter(prompt)
                .build();

        if (request.isPresent()) return getLink(request.get().getData());
        return Optional.empty();
    }

}
