package com.beanbeanjuice.cafeapi.wrapper.endpoints.welcomes;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

/**
 * A class used for the {@link WelcomesEndpoint} API.
 */
public class WelcomesEndpoint extends CafeEndpoint {

    /**
     * Retrieves an {@link ArrayList} of {@link GuildWelcome} containing all Guild Welcomes in the {@link CafeAPI CafeAPI}.
     * @return The {@link ArrayList} of {@link GuildWelcome}.
     * @throws AuthorizationException Thrown when the api key is unauthorized.
     * @throws ResponseException Thrown when there is a generic server-side exception.
     */
    public ArrayList<GuildWelcome> getAllGuildWelcomes() throws AuthorizationException, ResponseException {
        ArrayList<GuildWelcome> guildWelcomes = new ArrayList<>();

        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/welcomes")
                .setAuthorization(apiKey)
                .build();

        request.getData()
                .get("welcomes")
                .forEach((guildWelcome) -> guildWelcomes.add(parseGuildWelcome(guildWelcome)));

        return guildWelcomes;
    }

    /**
     * Retrieves a {@link GuildWelcome} from the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} to retrieve the {@link GuildWelcome} for.
     * @return The {@link GuildWelcome} retrieved.
     * @throws AuthorizationException Thrown when the API key is invalid.
     * @throws ResponseException Thrown when there is a generic server-side exception.
     * @throws NotFoundException Thrown when the guild ID is not found.
     */
    public GuildWelcome getGuildWelcome(final String guildID)
    throws AuthorizationException, ResponseException, NotFoundException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/welcomes/" + guildID)
                .setAuthorization(apiKey)
                .build();

        JsonNode guildWelcome = request.getData().get("welcome");

        return parseGuildWelcome(guildWelcome);
    }

    /**
     * Updates a {@link GuildWelcome} in the {@link CafeAPI CafeAPI}.
     * @param guildWelcome The new {@link GuildWelcome}.
     * @return True, if updating the {@link GuildWelcome} was successful.
     * @throws AuthorizationException Thrown when the API key is invalid.
     * @throws NotFoundException Thrown when the guild ID is not found.
     * @throws ResponseException Thrown when there is a generic server-side exception.
     */
    public boolean updateGuildWelcome(final GuildWelcome guildWelcome)
    throws AuthorizationException, NotFoundException, ResponseException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/welcomes/" + guildWelcome.getGuildID())
                .addParameter("description", guildWelcome.getDescription().orElse(null))
                .addParameter("thumbnail_url", guildWelcome.getThumbnailURL().orElse(null))
                .addParameter("image_url", guildWelcome.getImageURL().orElse(null))
                .addParameter("message", guildWelcome.getMessage().orElse(null))
                .setAuthorization(apiKey)
                .build();

        return request.getStatusCode() == 200;
    }

    /**
     * Creates a new {@link GuildWelcome} for the {@link CafeAPI CafeAPI}.
     * @param guildWelcome The new {@link GuildWelcome} to add.
     * @return True if the {@link GuildWelcome} was successfully added.
     * @throws AuthorizationException Thrown when the API key is invalid.
     * @throws ConflictException Thrown when the provided guild ID already exists.
     * @throws ResponseException Thrown when there is a generic server-side exception.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean createGuildWelcome(final GuildWelcome guildWelcome)
    throws AuthorizationException, ConflictException, ResponseException, UndefinedVariableException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/welcomes/" + guildWelcome.getGuildID())
                .addParameter("description", guildWelcome.getDescription().orElse(null))
                .addParameter("thumbnail_url", guildWelcome.getThumbnailURL().orElse(null))
                .addParameter("image_url", guildWelcome.getImageURL().orElse(null))
                .addParameter("message", guildWelcome.getMessage().orElse(null))
                .setAuthorization(apiKey)
                .build();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes a {@link GuildWelcome} from the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String} ID of the {@link GuildWelcome} to delete.
     * @return True if successfully deleted.
     * @throws AuthorizationException Thrown when the API key is invalid.
     * @throws ResponseException Thrown when there is a generic server-side exception.
     */
    public boolean deleteGuildWelcome(final String guildID)
    throws AuthorizationException, ResponseException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/welcomes/" + guildID)
                .setAuthorization(apiKey)
                .build();

        return request.getStatusCode() == 200;
    }

    /**
     * Parses a {@link GuildWelcome} from the {@link JsonNode}.
     * @param node The {@link JsonNode} to parse.
     * @return The parsed {@link GuildWelcome}.
     */
    private GuildWelcome parseGuildWelcome(final JsonNode node) {
        String guildID = node.get("guild_id").asText();

        String description = node.get("description").asText();
        String thumbnailURL = node.get("thumbnail_url").asText();
        String imageURL = node.get("image_url").asText();
        String message = node.get("message").asText();

        if (description.equals("null")) description = null;
        if (thumbnailURL.equals("null")) thumbnailURL = null;
        if (imageURL.equals("null")) imageURL = null;
        if (message.equals("null")) message = null;

        return new GuildWelcome(
                guildID,
                description,
                thumbnailURL,
                imageURL,
                message
        );
    }

}
