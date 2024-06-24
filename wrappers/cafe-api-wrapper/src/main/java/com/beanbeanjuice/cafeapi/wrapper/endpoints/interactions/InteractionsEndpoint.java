package com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.users.Interaction;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class InteractionsEndpoint extends CafeEndpoint {

    public CompletableFuture<HashMap<String, Interaction>> getAllInteractionSenders() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/senders")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    HashMap<String, Interaction> senders = new HashMap<>();

                    request.getData().get("interactions_sent").forEach((sender) -> {
                        String userID = sender.get("user_id").asText();
                        senders.put(userID, parseInteraction(sender));
                    });

                    return senders;
                });

    }

    public CompletableFuture<Interaction> getUserInteractionsSent(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/senders/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> parseInteraction(request.getData().get("interactions_sent")));
    }

    public CompletableFuture<Integer> getSpecificUserInteractionSentAmount(final String userID, final InteractionType type) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/senders/" + userID)
                .addParameter("type", type.toString().toLowerCase())
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getData().get(type.getType()).asInt());
    }

    public CompletableFuture<Boolean> updateSpecificUserInteractionSentAmount(final String userID, final InteractionType type, final int amount) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/interactions/senders/" + userID)
                .addParameter("type", type.toString().toLowerCase())
                .addParameter("value", String.valueOf(amount))
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    public CompletableFuture<Boolean> createUserInteractionsSent(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/interactions/senders/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> deleteUserInteractionsSent(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/interactions/senders/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    // ==============================
    //     INTERACTION RECEIVERS
    // ==============================

    public CompletableFuture<HashMap<String, Interaction>> getAllInteractionReceivers() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/receivers")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    HashMap<String, Interaction> receivers = new HashMap<>();

                    request.getData().get("interactions_received").forEach((receiver) -> {
                        String userID = receiver.get("user_id").asText();
                        receivers.put(userID, parseInteraction(receiver));
                    });

                    return receivers;
                });
    }

    public CompletableFuture<Interaction> getUserInteractionsReceived(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/receivers/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> parseInteraction(request.getData().get("interactions_received")));
    }

    public CompletableFuture<Integer> getSpecificUserInteractionReceivedAmount(final String userID, final InteractionType type) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/receivers/" + userID)
                .addParameter("type", type.toString().toLowerCase())
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getData().get(type.getType()).asInt());
    }

    public CompletableFuture<Boolean> updateSpecificUserInteractionReceivedAmount(final String userID, final InteractionType type, final int amount) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/interactions/receivers/" + userID)
                .addParameter("type", type.toString().toLowerCase())
                .addParameter("value", String.valueOf(amount))
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    public CompletableFuture<Boolean> createUserInteractionsReceived(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/interactions/receivers/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> deleteUserInteractionsReceived(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/interactions/receivers/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    private Interaction parseInteraction(final JsonNode jsonNode) {
        HashMap<InteractionType, Integer> interactions = new HashMap<>();
        Arrays.stream(InteractionType.values()).forEach((type) -> interactions.put(type, jsonNode.get(type.getType()).asInt()));
        return new Interaction(interactions);
    }

}
