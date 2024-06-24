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
import java.util.concurrent.CompletableFuture;

public class WelcomesEndpoint extends CafeEndpoint {

    public CompletableFuture<ArrayList<GuildWelcome>> getAllGuildWelcomes() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/welcomes")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    ArrayList<GuildWelcome> guildWelcomes = new ArrayList<>();

                    request.getData()
                            .get("welcomes")
                            .forEach((guildWelcome) -> guildWelcomes.add(parseGuildWelcome(guildWelcome)));

                    return guildWelcomes;
                });
    }

    public CompletableFuture<GuildWelcome> getGuildWelcome(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/welcomes/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((request) -> parseGuildWelcome(request.getData().get("welcome")));
    }

    public CompletableFuture<Boolean> updateGuildWelcome(final GuildWelcome guildWelcome) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/welcomes/" + guildWelcome.getGuildID())
                .addParameter("description", guildWelcome.getDescription().orElse(null))
                .addParameter("thumbnail_url", guildWelcome.getThumbnailURL().orElse(null))
                .addParameter("image_url", guildWelcome.getImageURL().orElse(null))
                .addParameter("message", guildWelcome.getMessage().orElse(null))
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    public CompletableFuture<Boolean> createGuildWelcome(final GuildWelcome guildWelcome) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/welcomes/" + guildWelcome.getGuildID())
                .addParameter("description", guildWelcome.getDescription().orElse(null))
                .addParameter("thumbnail_url", guildWelcome.getThumbnailURL().orElse(null))
                .addParameter("image_url", guildWelcome.getImageURL().orElse(null))
                .addParameter("message", guildWelcome.getMessage().orElse(null))
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> deleteGuildWelcome(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/welcomes/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

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
