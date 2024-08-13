package com.beanbeanjuice.cafeapi.wrapper.endpoints.polls;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class PollsEndpoint extends CafeEndpoint {

    public CompletableFuture<HashMap<String, ArrayList<Poll>>> getAllPolls() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/polls")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    HashMap<String, ArrayList<Poll>> polls = new HashMap<>();

                    request.getData().get("polls").forEach((poll) -> {
                        String guildID = poll.get("guild_id").asText();
                        String messageID = poll.get("message_id").asText();
                        Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(poll.get("ending_time").asText()).orElse(null);

                        if (!polls.containsKey(guildID)) polls.put(guildID, new ArrayList<>());

                        polls.get(guildID).add(new Poll(messageID, endingTime));
                    });

                    return polls;
                });
    }

    public CompletableFuture<ArrayList<Poll>> getGuildPolls(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/polls/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    ArrayList<Poll> polls = new ArrayList<>();

                    request.getData().get("polls").forEach((poll) -> {
                        String messageID = poll.get("message_id").asText();
                        Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(poll.get("ending_time").asText()).orElse(null);

                        polls.add(new Poll(messageID, endingTime));
                    });

                    return polls;
                });
    }

    public CompletableFuture<Boolean> createPoll(final String guildID, final Poll poll) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/polls/" + guildID)
                .addParameter("message_id", poll.getMessageID())
                .addParameter("ending_time", poll.getEndingTime().toString())
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> deletePoll(final String guildID, final Poll poll) {
        return deletePoll(guildID, poll.getMessageID());
    }

    public CompletableFuture<Boolean> deletePoll(final String guildID, final String messageID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/polls/" + guildID)
                .addParameter("message_id", messageID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

}
