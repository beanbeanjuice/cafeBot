package com.beanbeanjuice.kawaiiapi.wrapper.endpoint;

import com.beanbeanjuice.kawaiiapi.wrapper.requests.Request;
import com.beanbeanjuice.kawaiiapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.kawaiiapi.wrapper.requests.RequestRoute;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * A class used for the GIF endpoint.
 *
 * @author beanbeanjuice
 * @since 1.0.0
 */
public class GifEndpoint extends KawaiiEndpoint {

    public CompletableFuture<Optional<String>> getGIF(final String prompt) {
        return RequestBuilder.create(RequestRoute.GIF)
                .setAuthorization(apiKey)
                .setParameter(prompt)
                .buildAsync()
                .thenApplyAsync((request -> {
                    if (request.isPresent()) return getLink(request.get().getData());
                    return Optional.empty();
                }));
    }

}
