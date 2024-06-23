package com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.users.Interaction;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A class used for {@link InteractionsEndpoint} requests in the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class InteractionsEndpoint extends CafeEndpoint {

    /**
     * Retrieves all {@link Interaction} senders found in the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with keys of {@link String userID} and a value of {@link Interaction} sent.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public HashMap<String, Interaction> getAllInteractionSenders()
    throws AuthorizationException, ResponseException {
        HashMap<String, Interaction> senders = new HashMap<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/senders")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        request.getData().get("interactions_sent").forEach((sender) -> {
            String userID = sender.get("user_id").asText();
            senders.put(userID, parseInteraction(sender));
        });

        return senders;
    }

    /**
     * Retrieves all {@link Interaction} sent found in the {@link CafeAPI CafeAPI} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return The {@link Interaction} sent for the specified {@link String userID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the specified {@link String userID} is not found in the {@link CafeAPI CafeAPI}.
     */
    public Interaction getUserInteractionsSent(final String userID)
    throws AuthorizationException, ResponseException, NotFoundException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/senders/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return parseInteraction(request.getData().get("interactions_sent"));
    }

    /**
     * Retrieves a specific {@link InteractionType} sent from the specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param type The {@link InteractionType type} of {@link Interaction}.
     * @return The amount of the specified {@link InteractionType} that was sent from the specified {@link String userID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the {@link String userID} is not found in the {@link CafeAPI CafeAPI}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public Integer getSpecificUserInteractionSentAmount(final String userID, final InteractionType type)
    throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/senders/" + userID)
                .addParameter("type", type.toString().toLowerCase())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getData().get(type.getType()).asInt();
    }

    /**
     * Updates the {@link Interaction} sent amount of a specified {@link InteractionType} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param type The specified {@link InteractionType type}.
     * @param amount The specified {@link Integer amount} for the {@link InteractionType}.
     * @return True, if the {@link Interaction} was successfully updated for the {@link String userID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the specified {@link String userID} is not found in the {@link CafeAPI CafeAPI}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public Boolean updateSpecificUserInteractionSentAmount(final String userID, final InteractionType type, final int amount)
    throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/interactions/senders/" + userID)
                .addParameter("type", type.toString().toLowerCase())
                .addParameter("value", String.valueOf(amount))
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Creates a new {@link Interaction} sender.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Interaction} sender was successfully created.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws ConflictException Thrown when the specified {@link String userID} already exists in the {@link CafeAPI CafeAPI}.
     */
    public boolean createUserInteractionsSent(final String userID)
    throws AuthorizationException, ResponseException, ConflictException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/interactions/senders/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes a specified {@link Interaction} sender.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Interaction} sender was successfully deleted.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public boolean deleteUserInteractionsSent(final String userID)
    throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/interactions/senders/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return  request.getStatusCode() == 200;
    }

    // ==============================
    //     INTERACTION RECEIVERS
    // ==============================

    /**
     * Retrieves all {@link Interaction} receivers found in the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with keys of {@link String userID} and a value of {@link Interaction} received.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public HashMap<String, Interaction> getAllInteractionReceivers()
    throws AuthorizationException, ResponseException {
        HashMap<String, Interaction> receivers = new HashMap<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/receivers")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        request.getData().get("interactions_received").forEach((receiver) -> {
            String userID = receiver.get("user_id").asText();
            receivers.put(userID, parseInteraction(receiver));
        });

        return receivers;
    }

    /**
     * Retrieves all {@link Interaction} received found in the {@link CafeAPI CafeAPI} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return The {@link Interaction} received for the specified {@link String userID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the specified {@link String userID} is not found in the {@link CafeAPI CafeAPI}.
     */
    public Interaction getUserInteractionsReceived(final String userID)
    throws AuthorizationException, ResponseException, NotFoundException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/receivers/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return parseInteraction(request.getData().get("interactions_received"));
    }

    /**
     * Retrieves a specific {@link InteractionType} received for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param type The {@link InteractionType type} of {@link Interaction}.
     * @return The amount of the specified {@link InteractionType} that was received for the specified {@link String userID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the {@link String userID} is not found in the {@link CafeAPI CafeAPI}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public Integer getSpecificUserInteractionReceivedAmount(final String userID, final InteractionType type)
    throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/interactions/receivers/" + userID)
                .addParameter("type", type.toString().toLowerCase())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getData().get(type.getType()).asInt();
    }

    /**
     * Updates the {@link Interaction} received amount of a specified {@link InteractionType} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param type The specified {@link InteractionType type}.
     * @param amount The specified {@link Integer amount} for the {@link InteractionType}.
     * @return True, if the {@link Interaction} was successfully updated for the {@link String userID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the specified {@link String userID} is not found in the {@link CafeAPI CafeAPI}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public Boolean updateSpecificUserInteractionReceivedAmount(final String userID, final InteractionType type, final int amount)
    throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/interactions/receivers/" + userID)
                .addParameter("type", type.toString().toLowerCase())
                .addParameter("value", String.valueOf(amount))
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Creates a new {@link Interaction} receiver.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Interaction} receiver was successfully created.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws ConflictException Thrown when the specified {@link String userID} already exists in the {@link CafeAPI CafeAPI}.
     */
    public boolean createUserInteractionsReceived(final String userID)
    throws AuthorizationException, ResponseException, ConflictException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/interactions/receivers/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes a specified {@link Interaction} receiver.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Interaction} receiver was successfully deleted.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public boolean deleteUserInteractionsReceived(final String userID)
    throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/interactions/receivers/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return  request.getStatusCode() == 200;
    }

    /**
     * Parses a {@link JsonNode} for the {@link Interaction}.
     * @param jsonNode The {@link JsonNode} to parse into an {@link Interaction}.
     * @return The parsed {@link Interaction}.
     */
    private Interaction parseInteraction(final JsonNode jsonNode) {
        HashMap<InteractionType, Integer> interactions = new HashMap<>();

        Arrays.stream(InteractionType.values()).forEach((type) -> interactions.put(type, jsonNode.get(type.getType()).asInt()));

        return new Interaction(interactions);
    }

}
