package com.beanbeanjuice.cafeapi.wrapper.endpoints.goodbyes;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class GoodbyesEndpoint extends CafeEndpoint {

    public CompletableFuture<ArrayList<GuildGoodbye>> getAllGuildGoodbyes() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/goodbyes")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    ArrayList<GuildGoodbye> guildGoodbyes = new ArrayList<>();
                    request.getData().get("goodbyes").forEach((guildGoodbye) -> guildGoodbyes.add(parseGuildGoodbye(guildGoodbye)));
                    return guildGoodbyes;
                });
    }

    public CompletableFuture<GuildGoodbye> getGuildGoodbye(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/goodbyes/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> parseGuildGoodbye(request.getData().get("goodbye")));
    }

    public CompletableFuture<Boolean> updateGuildGoodbye(final GuildGoodbye guildGoodbye) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/goodbyes/" + guildGoodbye.getGuildID())
                .addParameter("description", guildGoodbye.getDescription().orElse(null))
                .addParameter("thumbnail_url", guildGoodbye.getThumbnailURL().orElse(null))
                .addParameter("image_url", guildGoodbye.getImageURL().orElse(null))
                .addParameter("message", guildGoodbye.getMessage().orElse(null))
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    public CompletableFuture<Boolean> createGuildGoodbye(final GuildGoodbye guildGoodbye) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/goodbyes/" + guildGoodbye.getGuildID())
                .addParameter("description", guildGoodbye.getDescription().orElse(null))
                .addParameter("thumbnail_url", guildGoodbye.getThumbnailURL().orElse(null))
                .addParameter("image_url", guildGoodbye.getImageURL().orElse(null))
                .addParameter("message", guildGoodbye.getMessage().orElse(null))
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> deleteGuildGoodbye(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/goodbyes/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    private GuildGoodbye parseGuildGoodbye(final JsonNode node) {
        String guildID = node.get("guild_id").asText();

        String description = node.get("description").asText();
        String thumbnailURL = node.get("thumbnail_url").asText();
        String imageURL = node.get("image_url").asText();
        String message = node.get("message").asText();

        if (description.equals("null")) description = null;
        if (thumbnailURL.equals("null")) thumbnailURL = null;
        if (imageURL.equals("null")) imageURL = null;
        if (message.equals("null")) message = null;

        return new GuildGoodbye(
                guildID,
                description,
                thumbnailURL,
                imageURL,
                message
        );
    }

}
