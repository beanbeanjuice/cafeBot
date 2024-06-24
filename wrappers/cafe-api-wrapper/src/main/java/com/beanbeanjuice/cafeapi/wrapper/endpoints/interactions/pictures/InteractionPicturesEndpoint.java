package com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.pictures;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class InteractionPicturesEndpoint extends CafeEndpoint {

    private final CafeAPI cafeAPI;

    public InteractionPicturesEndpoint(final CafeAPI cafeAPI) {
        this.cafeAPI = cafeAPI;
    }

    public CompletableFuture<Optional<String>> getRandomInteractionPicture(final InteractionType type) {
        Optional<String> potentialString = type.getKawaiiAPIString();

        if (potentialString.isPresent()) return cafeAPI.getKawaiiAPI().getGifEndpoint().getGIF(potentialString.get());

        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interaction_pictures/" + type)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> Optional.of(request.getData().get("url").asText()));
    }

}
