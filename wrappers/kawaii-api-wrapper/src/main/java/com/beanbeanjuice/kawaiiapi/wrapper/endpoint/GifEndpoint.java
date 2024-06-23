package com.beanbeanjuice.kawaiiapi.wrapper.endpoint;

import com.beanbeanjuice.kawaiiapi.wrapper.requests.Request;
import com.beanbeanjuice.kawaiiapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.kawaiiapi.wrapper.requests.RequestRoute;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A class used for the GIF endpoint.
 *
 * @author beanbeanjuice
 * @since 1.0.0
 */
public class GifEndpoint extends KawaiiEndpoint {

    /**
     * Get a random GIF image for the prompt.
     * @param prompt The {@link String prompt} specified.
     * @return The {@link Optional<String> link} to the gif.
     * @see <a href="https://docs.kawaii.red/endpoints/gif">Available Prompts</a>
     */
    public Optional<String> getGIF(final String prompt) {
        Optional<Request> request = RequestBuilder.create(RequestRoute.GIF)
                .setAuthorization(apiKey)
                .setParameter(prompt)
                .build();

        if (request.isPresent()) return getLink(request.get().getData());
        return Optional.empty();
    }

    public void getGIFAsync(final String prompt, final Consumer<Optional<String>> successFunction, final Consumer<Exception> errorFunction) {
        RequestBuilder.create(RequestRoute.GIF)
                .setAuthorization(apiKey)
                .setParameter(prompt)
                .onSuccess((request) -> successFunction.accept(getLink(request.getData())))
                .onError(errorFunction)
                .buildAsync();
    }

    public void getGIFAsync(final String prompt, final Consumer<Optional<String>> successFunction) {
        RequestBuilder.create(RequestRoute.GIF)
                .setAuthorization(apiKey)
                .setParameter(prompt)
                .onSuccess((request) -> successFunction.accept(getLink(request.getData())))
                .buildAsync();
    }

}
