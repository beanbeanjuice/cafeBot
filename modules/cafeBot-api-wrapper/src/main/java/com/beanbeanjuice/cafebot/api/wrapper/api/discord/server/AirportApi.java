package com.beanbeanjuice.cafebot.api.wrapper.api.discord.server;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.AirportMessageType;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.airport.AirportMessage;
import com.beanbeanjuice.cafebot.api.wrapper.type.airport.PartialAirportMessage;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class AirportApi extends Api {

    public AirportApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<AirportMessage> getAirportMessage(String guildId, AirportMessageType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/airport/%s/%s", guildId, type.toString()))
                    .queue()
                    .thenApply(this::parseAirportMessage);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<AirportMessage> setAirportMessage(String guildId, PartialAirportMessage partialAirportMessage) {
        HashMap<String, Object> map = new HashMap<>();
        partialAirportMessage.getTitle().ifPresent(title -> map.put("title", title));
        partialAirportMessage.getAuthor().ifPresent(author -> map.put("author", author));
        partialAirportMessage.getAuthorUrl().ifPresent(authorUrl -> map.put("authorUrl", authorUrl));
        partialAirportMessage.getImageUrl().ifPresent(imageUrl -> map.put("imageUrl", imageUrl));
        partialAirportMessage.getThumbnailUrl().ifPresent(thumbnailUrl -> map.put("thumbnailUrl", thumbnailUrl));
        partialAirportMessage.getDescription().ifPresent(description -> map.put("description", description));
        partialAirportMessage.getMessage().ifPresent(message -> map.put("message", message));

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/airport/%s/%s", guildId, partialAirportMessage.getType().toString()))
                    .body(map)
                    .queue()
                    .thenApply(this::parseAirportMessage);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Void> deleteAirportMessage(String guildId, AirportMessageType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.DELETE)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/airport/%s/%s", guildId, type.toString()))
                    .queue()
                    .thenApply((res) -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private AirportMessage parseAirportMessage(BasicResponse response) {
        JsonNode body = response.getBody().get("airportMessage");

        String guildId = body.get("guildId").asString();
        AirportMessageType type = AirportMessageType.valueOf(body.get("type").asString());
        String title = body.get("title").isNull() ? null : body.get("title").asString();
        String author = body.get("author").isNull() ? null : body.get("author").asString();
        String authorUrl = body.get("authorUrl").isNull() ? null : body.get("authorUrl").asString();
        String imageUrl = body.get("imageUrl").isNull() ? null : body.get("imageUrl").asString();
        String thumbnailUrl = body.get("thumbnailUrl").isNull() ? null : body.get("thumbnailUrl").asString();
        String description = body.get("description").isNull() ? null : body.get("description").asString();
        String message = body.get("message").isNull() ? null : body.get("message").asString();

        return new AirportMessage(guildId, type, title, author, authorUrl, imageUrl, thumbnailUrl, description, message);
    }

}
