package com.beanbeanjuice.cafeapi.cafebot.polls;

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
 * A class used for {@link Polls} requests in the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class Polls implements com.beanbeanjuice.cafeapi.api.CafeAPI {

    private String apiKey;

    /**
     * Creates a new {@link Polls} object.
     * @param apiKey The {@link String apiKey} used for authorization.
     */
    public Polls(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retrieves all {@link Poll} from the {@link CafeAPI CafeAPI}.
     * @return The {@link HashMap} of key {@link String guildID} and a value of {@link ArrayList} of {@link Poll}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public HashMap<String, ArrayList<Poll>> getAllPolls()
    throws AuthorizationException, ResponseException {
        HashMap<String, ArrayList<Poll>> polls = new HashMap<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/polls")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        for (JsonNode poll : request.getData().get("polls")) {
            String guildID = poll.get("guild_id").asText();
            String messageID = poll.get("message_id").asText();
            Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(poll.get("ending_time").asText()).orElse(null);

            if (!polls.containsKey(guildID)) {
                polls.put(guildID, new ArrayList<>());
            }

            polls.get(guildID).add(new Poll(messageID, endingTime));
        }

        return polls;
    }

    /**
     * Retrieves all {@link Poll} from a specific Discord server.
     * @param guildID The {@link String guildID} of the Discord server.
     * @return An {@link ArrayList} of {@link Poll}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public ArrayList<Poll> getGuildPolls(String guildID)
    throws AuthorizationException, ResponseException {
        ArrayList<Poll> polls = new ArrayList<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/polls/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        for (JsonNode poll : request.getData().get("polls")) {
            String messageID = poll.get("message_id").asText();
            Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(poll.get("ending_time").asText()).orElse(null);

            polls.add(new Poll(messageID, endingTime));
        }

        return polls;
    }

    /**
     * Creates a {@link Poll} in the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the Discord server.
     * @param poll The {@link Poll} to add to the {@link CafeAPI CafeAPI}.
     * @return True, if the {@link Poll} was successfully added to the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     * @throws ConflictException Thrown when the {@link Poll} already exists for the specified {@link String guildID}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean createPoll(String guildID, Poll poll)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/polls/" + guildID)
                .addParameter("message_id", poll.getMessageID())
                .addParameter("ending_time", poll.getEndingTime().toString())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes a {@link Poll} from the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the Discord server.
     * @param poll The {@link Poll} to remove from the {@link CafeAPI CafeAPI}.
     * @return True, if the {@link Poll} was successfully removed from the {@link CafeAPI CafeAPI}.
     */
    public boolean deletePoll(String guildID, Poll poll) {
        return deletePoll(guildID, poll.getMessageID());
    }

    /**
     * Deletes a {@link Poll} from the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the Discord server.
     * @param messageID The {@link String messageID} of the {@link Poll} to remove from the {@link CafeAPI CafeAPI}.
     * @return True, if the {@link Poll} was successfully removed from the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean deletePoll(String guildID, String messageID)
    throws AuthorizationException, ResponseException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/polls/" + guildID)
                .addParameter("message_id", messageID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
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
