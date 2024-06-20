package com.beanbeanjuice.cafeapi.cafebot.raffles;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.requests.Request;
import com.beanbeanjuice.cafeapi.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;
import com.beanbeanjuice.cafeapi.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.exception.api.ResponseException;
import com.beanbeanjuice.cafeapi.exception.api.UndefinedVariableException;
import com.beanbeanjuice.cafeapi.exception.api.CafeException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used for requests about {@link Raffles} in the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class Raffles implements com.beanbeanjuice.cafeapi.api.CafeAPI {

    private String apiKey;

    /**
     * Creates the {@link Raffles} module for the {@link CafeAPI CafeAPI}.
     * @param apiKey The authorization {@link String apiKey}.
     */
    public Raffles(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retrieves all {@link Raffle Raffles} in the {@link CafeAPI CafeAPI}.
     * @return The {@link HashMap} with keys of {@link String guildID} and a value of {@link ArrayList} containing {@link Raffle Raffles}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public HashMap<String, ArrayList<Raffle>> getAllRaffles()
    throws AuthorizationException, ResponseException {
        HashMap<String, ArrayList<Raffle>> raffles = new HashMap<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/raffles")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        for (JsonNode raffle : request.getData().get("raffles")) {
            String guildID = raffle.get("guild_id").asText();
            String messageID = raffle.get("message_id").asText();
            Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(raffle.get("ending_time").asText()).orElse(null);
            Integer winnerAmount = raffle.get("winner_amount").asInt();

            if (!raffles.containsKey(guildID)) {
                raffles.put(guildID, new ArrayList<>());
            }

            raffles.get(guildID).add(new Raffle(messageID, endingTime, winnerAmount));
        }

        return raffles;
    }

    /**
     * Retrieves all {@link Raffle Raffles} from a specified Discord server.
     * @param guildID The {@link String guildID} of the Discord server.
     * @return The {@link ArrayList} of {@link Raffle} in the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public ArrayList<Raffle> getGuildRaffles(String guildID)
    throws AuthorizationException, ResponseException {
        ArrayList<Raffle> raffles = new ArrayList<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/raffles/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        for (JsonNode raffle : request.getData().get("raffles")) {
            String messageID = raffle.get("message_id").asText();
            Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(raffle.get("ending_time").asText()).orElse(null);
            Integer winnerAmount = raffle.get("winner_amount").asInt();

            raffles.add(new Raffle(messageID, endingTime, winnerAmount));
        }

        return raffles;
    }

    /**
     * Creates a new {@link Raffle} in the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the {@link Raffle}.
     * @param raffle The {@link Raffle} to add to the {@link CafeAPI CafeAPI}.
     * @return True, if the {@link Raffle} was successfully created.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     * @throws ConflictException Thrown when the {@link Raffle} already exists for the specified {@link String guildID}.
     */
    public boolean createRaffle(String guildID, Raffle raffle)
    throws AuthorizationException, ResponseException, UndefinedVariableException, ConflictException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/raffles/" + guildID)
                .addParameter("message_id", raffle.getMessageID())
                .addParameter("ending_time", raffle.getEndingTime().toString())
                .addParameter("winner_amount", raffle.getWinnerAmount().toString())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes a {@link Raffle} from the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the {@link Raffle}.
     * @param raffle The {@link Raffle} to delete from the {@link CafeAPI CafeAPI}.
     * @return True, if the {@link Raffle} was successfully removed from the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean deleteRaffle(String guildID, Raffle raffle)
    throws AuthorizationException, ResponseException, UndefinedVariableException {
        return deleteRaffle(guildID, raffle.getMessageID());
    }

    /**
     * Deletes a {@link Raffle} from the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the {@link Raffle}.
     * @param messageID The {@link String messageID} of the {@link Raffle}.
     * @return True, if the {@link Raffle} was successfully removed from the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean deleteRaffle(String guildID, String messageID)
    throws AuthorizationException, ResponseException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/raffles/" + guildID)
                .addParameter("message_id", messageID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Updates the {@link String apiKey}.
     * @param apiKey The {@link String apiKey} to update the current one to.
     */
    @Override
    public void updateAPIKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
