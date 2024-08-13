package com.beanbeanjuice.kawaiiapi.wrapper.endpoint;

import com.beanbeanjuice.kawaiiapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.kawaiiapi.wrapper.requests.RequestRoute;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
