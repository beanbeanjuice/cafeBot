package com.beanbeanjuice.cafeapi.wrapper.endpoints.polls;

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
 * A class used for {@link PollsEndpoint} requests in the {@link CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class PollsEndpoint extends CafeEndpoint {

    /**
     * Retrieves all {@link Poll} from the {@link CafeAPI CafeAPI}.
     * @return The {@link HashMap} of key {@link String guildID} and a value of {@link ArrayList} of {@link Poll}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public HashMap<String, ArrayList<Poll>> getAllPolls()
    throws AuthorizationException, ResponseException {
        HashMap<String, ArrayList<Poll>> polls = new HashMap<>();

        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/polls")
                .setAuthorization(apiKey)
                .build();

        request.getData().get("polls").forEach((poll) -> {
            String guildID = poll.get("guild_id").asText();
            String messageID = poll.get("message_id").asText();
            Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(poll.get("ending_time").asText()).orElse(null);

            if (!polls.containsKey(guildID)) polls.put(guildID, new ArrayList<>());

            polls.get(guildID).add(new Poll(messageID, endingTime));
        });

        return polls;
    }

    /**
     * Retrieves all {@link Poll} from a specific Discord server.
     * @param guildID The {@link String guildID} of the Discord server.
     * @return An {@link ArrayList} of {@link Poll}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public ArrayList<Poll> getGuildPolls(final String guildID)
    throws AuthorizationException, ResponseException {
        ArrayList<Poll> polls = new ArrayList<>();

        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/polls/" + guildID)
                .setAuthorization(apiKey)
                .build();

        request.getData().get("polls").forEach((poll) -> {
            String messageID = poll.get("message_id").asText();
            Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(poll.get("ending_time").asText()).orElse(null);

            polls.add(new Poll(messageID, endingTime));
        });

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
    public boolean createPoll(final String guildID, final Poll poll)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/polls/" + guildID)
                .addParameter("message_id", poll.getMessageID())
                .addParameter("ending_time", poll.getEndingTime().toString())
                .setAuthorization(apiKey)
                .build();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes a {@link Poll} from the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the Discord server.
     * @param poll The {@link Poll} to remove from the {@link CafeAPI CafeAPI}.
     * @return True, if the {@link Poll} was successfully removed from the {@link CafeAPI CafeAPI}.
     */
    public boolean deletePoll(final String guildID, final Poll poll) {
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
    public boolean deletePoll(final String guildID, final String messageID)
    throws AuthorizationException, ResponseException, UndefinedVariableException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/polls/" + guildID)
                .addParameter("message_id", messageID)
                .setAuthorization(apiKey)
                .build();

        return request.getStatusCode() == 200;
    }

}
