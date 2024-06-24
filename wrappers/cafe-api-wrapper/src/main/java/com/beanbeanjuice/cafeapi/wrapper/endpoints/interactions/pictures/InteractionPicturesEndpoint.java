package com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.pictures;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.users.Interaction;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ResponseException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.TeaPotException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * A class used to retrieve random pictures from the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 * @since 1.3.1
 */
public class InteractionPicturesEndpoint extends CafeEndpoint {

    private final CafeAPI cafeAPI;

    /**
     * Creates a new {@link InteractionPicturesEndpoint} object.
     * @param cafeAPI The baseline API.
     */
    public InteractionPicturesEndpoint(final CafeAPI cafeAPI) {
        this.cafeAPI = cafeAPI;
    }

    /**
     * Retrieves a random {@link String interactionURL} for a specified {@link InteractionType}.
     *
     * @param type The {@link InteractionType} specified.
     * @return A {@link CompletableFuture} containing an {@link Optional} {@link String url}.
     */
    public CompletableFuture<Optional<String>> getRandomInteractionPicture(final InteractionType type) {
        Optional<String> potentialString = type.getKawaiiAPIString();

        if (potentialString.isPresent()) return cafeAPI.getKawaiiAPI().getGifEndpoint().getGIF(potentialString.get());

        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interaction_pictures/" + type)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((optionalRequest) -> {
                    if (optionalRequest.isPresent()) return Optional.of(optionalRequest.get().getData().get("url").asText());
                    throw new CompletionException("Unable to get a random interaction picture. Request is empty.", null);
                });
    }

}
