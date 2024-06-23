package com.beanbeanjuice.cafeapi.wrapper.endpoints.raffles;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ResponseException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.UndefinedVariableException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used for requests about {@link RafflesEndpoint} in the {@link CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class RafflesEndpoint extends CafeEndpoint {

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

        request.getData().get("raffles").forEach((raffle) -> {
            String guildID = raffle.get("guild_id").asText();
            String messageID = raffle.get("message_id").asText();
            Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(raffle.get("ending_time").asText()).orElse(null);
            Integer winnerAmount = raffle.get("winner_amount").asInt();

            if (!raffles.containsKey(guildID)) raffles.put(guildID, new ArrayList<>());

            raffles.get(guildID).add(new Raffle(messageID, endingTime, winnerAmount));
        });

        return raffles;
    }

    /**
     * Retrieves all {@link Raffle Raffles} from a specified Discord server.
     * @param guildID The {@link String guildID} of the Discord server.
     * @return The {@link ArrayList} of {@link Raffle} in the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public ArrayList<Raffle> getGuildRaffles(final String guildID)
    throws AuthorizationException, ResponseException {
        ArrayList<Raffle> raffles = new ArrayList<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/raffles/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        request.getData().get("raffles").forEach((raffle) -> {
            String messageID = raffle.get("message_id").asText();
            Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(raffle.get("ending_time").asText()).orElse(null);
            Integer winnerAmount = raffle.get("winner_amount").asInt();

            raffles.add(new Raffle(messageID, endingTime, winnerAmount));
        });

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
    public boolean createRaffle(final String guildID, final Raffle raffle)
    throws AuthorizationException, ResponseException, UndefinedVariableException, ConflictException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/raffles/" + guildID)
                .addParameter("message_id", raffle.getMessageID())
                .addParameter("ending_time", raffle.getEndingTime().toString())
                .addParameter("winner_amount", String.valueOf(raffle.getWinnerAmount()))
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
    public boolean deleteRaffle(final String guildID, final Raffle raffle)
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
    public boolean deleteRaffle(final String guildID, final String messageID)
    throws AuthorizationException, ResponseException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/raffles/" + guildID)
                .addParameter("message_id", messageID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

}
