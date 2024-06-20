package com.beanbeanjuice.cafeapi.wrapper.cafebot.interactions.pictures;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.cafebot.interactions.users.Interaction;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.cafebot.interactions.InteractionType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ResponseException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.TeaPotException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;

import java.util.Optional;

/**
 * A class used to retrieve random pictures from the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 * @since 1.3.1
 */
public class InteractionPictures implements com.beanbeanjuice.cafeapi.wrapper.api.CafeAPI {

    private String apiKey;
    private final CafeAPI cafeAPI;

    /**
     * Creates a new {@link InteractionPictures} object.
     * @param apiKey The {@link String apiKey} used for authorization.
     */
    public InteractionPictures(String apiKey, CafeAPI cafeAPI) {
        this.apiKey = apiKey;
        this.cafeAPI = cafeAPI;
    }

    /**
     * Retrieves a random {@link String interactionURL} for a specified {@link InteractionType}.
     * @param type The {@link InteractionType} specified.
     * @return The {@link String url} to the {@link Interaction Interaction} image.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     * @throws TeaPotException Thrown when an invalid {@link InteractionType} is entered.
     */
    public String getRandomInteractionPicture(InteractionType type)
    throws AuthorizationException, ResponseException, TeaPotException {
        Optional<String> potentialString = type.getKawaiiAPIString();

        if (potentialString.isPresent()) return cafeAPI.KAWAII_API.GIF.getGIF(potentialString.get()).orElseThrow();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interaction_pictures/" + type)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getData().get("url").asText();
    }

    /**
     * Updates the {@link String apiKey}.
     * @param apiKey The new {@link String apiKey}.
     */
    @Override
    public void updateAPIKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
