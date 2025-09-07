package com.beanbeanjuice.cafebot.api.wrapper.api.discord.user;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.InteractionType;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.Interaction;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class InteractionsApi extends Api {

    public InteractionsApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    /**
     * Get an interaction image URL.
     * @param type The {@link InteractionType type}.
     * @return A random image based on the {@link InteractionType type}.
     */
    public CompletableFuture<String> getImage(InteractionType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/interactions/image/%s", type.toString()))
                    .queue()
                    .thenApply((response) -> response.getBody().get("imageUrl").asString());
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    /**
     * @param id The {@link String id} of the user to find interactions for.
     * @return All interactions sent or received by the {@link String id}.
     */
    public CompletableFuture<ArrayList<Interaction>> getInteractions(String id) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/interactions/%s", id))
                    .queue()
                    .thenApply(this::parseInteractions);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    /**
     * @param id The {@link String id} of the user to find interactions for.
     * @param type The {@link InteractionType type} to filter by.
     * @return All interactions of a specific {@link InteractionType type} sent or received by the {@link String id}.
     */
    public CompletableFuture<ArrayList<Interaction>> getInteractions(String id, InteractionType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/interactions/%s?type=%s", id, type.toString()))
                    .queue()
                    .thenApply(this::parseInteractions);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    /**
     * @param id The {@link String id} of the user to find interactions sent for.
     * @return All interactions sent by the {@link String id}.
     */
    public CompletableFuture<ArrayList<Interaction>> getInteractionsSent(String id) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/interactions/%s/sent", id))
                    .queue()
                    .thenApply(this::parseInteractions);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    /**
     * @param id The {@link String id} of the user to find interactions sent for.
     * @param type The {@link InteractionType type} to filter by.
     * @return All interactions of a specific {@link InteractionType} sent by the {@link String id}.
     */
    public CompletableFuture<ArrayList<Interaction>> getInteractionsSent(String id, InteractionType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/interactions/%s/sent?type=%s", id, type.toString()))
                    .queue()
                    .thenApply(this::parseInteractions);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    /**
     * @param id The {@link String id} of the user to find interactions received for.
     * @return All interactions received by the {@link String id}.
     */
    public CompletableFuture<ArrayList<Interaction>> getInteractionsReceived(String id) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/interactions/%s/received", id))
                    .queue()
                    .thenApply(this::parseInteractions);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    /**
     * @param id The {@link String id} of the user to find interactions received for.
     * @param type The {@link InteractionType type} to filter by.
     * @return All interactions of a specific {@link InteractionType} received by the {@link String id}.
     */
    public CompletableFuture<ArrayList<Interaction>> getInteractionsReceived(String id, InteractionType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/interactions/%s/received?type=%s", id, type.toString()))
                    .queue()
                    .thenApply(this::parseInteractions);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private ArrayList<Interaction> parseInteractions(BasicResponse response) {
        JsonNode body = response.getBody();

        ArrayList<Interaction> interactions = new ArrayList<>();
        for (JsonNode node : body.get("interactions")) {
            InteractionType type = InteractionType.valueOf(node.get("type").asString());
            String fromId = node.get("fromId").asString();
            String toId = node.get("toId").asString();
            String when = node.get("createdAt").asString();

            interactions.add(new Interaction(type, fromId, toId, when));
        }

        return interactions;
    }

    /**
     * Creates a new interaction between two users.
     * @param fromId The {@link String id} of the user sending the interaction.
     * @param toId The {@link String id} of the user receiving the interactions.
     * @param type The {@link InteractionType type} of the interaction.
     * @return The newly created {@link Interaction}.
     */
    public CompletableFuture<Interaction> createInteraction(String fromId, String toId, InteractionType type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("from", fromId);
        map.put("to", toId);
        map.put("type", type.toString());

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/users/interactions")
                    .body(map)
                    .queue()
                    .thenApply((response) -> {
                        JsonNode body = response.getBody();

                        int numSent = body.get("from").get("count").asInt();
                        int numReceived = body.get("to").get("count").asInt();
                        String when = body.get("interaction").get("createdAt").asString();

                        return new Interaction(type, fromId, toId, when, numSent, numReceived);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

}
