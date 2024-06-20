package com.beanbeanjuice.cafeapi.cafebot.interactions;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.cafebot.interactions.users.Interaction;
import com.beanbeanjuice.cafeapi.exception.api.*;
import com.beanbeanjuice.cafeapi.requests.Request;
import com.beanbeanjuice.cafeapi.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

/**
 * A class used for {@link Interactions} requests in the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class Interactions implements com.beanbeanjuice.cafeapi.api.CafeAPI {

    private String apiKey;

    /**
     * Creates a new {@link Interactions} API module.
     * @param apiKey The {@link String apiKey} used for authorization.
     */
    public Interactions(String apiKey) {
        this.apiKey = apiKey;
    }

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

        for (JsonNode interactionSender : request.getData().get("interactions_sent")) {
            String userID = interactionSender.get("user_id").asText();
            senders.put(userID, parseInteraction(interactionSender));
        }

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
    public Interaction getUserInteractionsSent(String userID)
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
    public Integer getSpecificUserInteractionSentAmount(String userID, InteractionType type)
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
    public Boolean updateSpecificUserInteractionSentAmount(String userID, InteractionType type, int amount)
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
    public boolean createUserInteractionsSent(String userID)
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
    public boolean deleteUserInteractionsSent(String userID)
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

        for (JsonNode interactionReceiver : request.getData().get("interactions_received")) {
            String userID = interactionReceiver.get("user_id").asText();
            receivers.put(userID, parseInteraction(interactionReceiver));
        }

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
    public Interaction getUserInteractionsReceived(String userID)
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
    public Integer getSpecificUserInteractionReceivedAmount(String userID, InteractionType type)
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
    public Boolean updateSpecificUserInteractionReceivedAmount(String userID, InteractionType type, int amount)
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
    public boolean createUserInteractionsReceived(String userID)
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
    public boolean deleteUserInteractionsReceived(String userID)
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
    private Interaction parseInteraction(JsonNode jsonNode) {
        int hugAmount = jsonNode.get(InteractionType.HUG.getType()).asInt();
        int punchAmount = jsonNode.get(InteractionType.PUNCH.getType()).asInt();
        int kissAmount = jsonNode.get(InteractionType.KISS.getType()).asInt();
        int biteAmount = jsonNode.get(InteractionType.BITE.getType()).asInt();
        int blushAmount = jsonNode.get(InteractionType.BLUSH.getType()).asInt();
        int cuddleAmount = jsonNode.get(InteractionType.CUDDLE.getType()).asInt();
        int nomAmount = jsonNode.get(InteractionType.NOM.getType()).asInt();
        int pokeAmount = jsonNode.get(InteractionType.POKE.getType()).asInt();
        int slapAmount = jsonNode.get(InteractionType.SLAP.getType()).asInt();
        int stabAmount = jsonNode.get(InteractionType.STAB.getType()).asInt();
        int hmphAmount = jsonNode.get(InteractionType.HMPH.getType()).asInt();
        int poutAmount = jsonNode.get(InteractionType.POUT.getType()).asInt();
        int throwAmount = jsonNode.get(InteractionType.THROW.getType()).asInt();
        int smileAmount = jsonNode.get(InteractionType.SMILE.getType()).asInt();
        int stareAmount = jsonNode.get(InteractionType.STARE.getType()).asInt();
        int tickleAmount = jsonNode.get(InteractionType.TICKLE.getType()).asInt();
        int rageAmount = jsonNode.get(InteractionType.RAGE.getType()).asInt();
        int yellAmount = jsonNode.get(InteractionType.YELL.getType()).asInt();
        int headpatAmount = jsonNode.get(InteractionType.HEADPAT.getType()).asInt();
        int cryAmount = jsonNode.get(InteractionType.CRY.getType()).asInt();
        int danceAmount = jsonNode.get(InteractionType.DANCE.getType()).asInt();
        int dabAmount = jsonNode.get(InteractionType.DAB.getType()).asInt();
        int bonkAmount = jsonNode.get(InteractionType.BONK.getType()).asInt();
        int sleepAmount = jsonNode.get(InteractionType.SLEEP.getType()).asInt();
        int dieAmount = jsonNode.get(InteractionType.DIE.getType()).asInt();
        int welcomeAmount = jsonNode.get(InteractionType.WELCOME.getType()).asInt();
        int lickAmount = jsonNode.get(InteractionType.LICK.getType()).asInt();
        int shushAmount = jsonNode.get(InteractionType.SHUSH.getType()).asInt();
        int waveAmount = jsonNode.get(InteractionType.WAVE.getType()).asInt();
        int shootAmount = jsonNode.get(InteractionType.SHOOT.getType()).asInt();
        int amazedAmount = jsonNode.get(InteractionType.AMAZED.getType()).asInt();
        int askAmount = jsonNode.get(InteractionType.ASK.getType()).asInt();
        int boopAmount = jsonNode.get(InteractionType.BOOP.getType()).asInt();
        int loveAmount = jsonNode.get(InteractionType.LOVE.getType()).asInt();
        int nosebleedAmount = jsonNode.get(InteractionType.NOSEBLEED.getType()).asInt();
        int okAmount = jsonNode.get(InteractionType.OK.getType()).asInt();
        int uwuAmount = jsonNode.get(InteractionType.UWU.getType()).asInt();
        int winkAmount = jsonNode.get(InteractionType.WINK.getType()).asInt();

        return new Interaction(
                hugAmount, punchAmount, kissAmount,
                biteAmount, blushAmount, cuddleAmount,
                nomAmount, pokeAmount, slapAmount,
                stabAmount, hmphAmount, poutAmount,
                throwAmount, smileAmount, stareAmount,
                tickleAmount, rageAmount, yellAmount,
                headpatAmount, cryAmount, danceAmount,
                dabAmount, bonkAmount, sleepAmount,
                dieAmount, welcomeAmount, lickAmount,
                shushAmount, waveAmount, shootAmount,
                amazedAmount, askAmount, boopAmount,
                loveAmount, nosebleedAmount, okAmount,
                uwuAmount, winkAmount
        );
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
