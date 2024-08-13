package com.beanbeanjuice.cafeapi.wrapper.endpoints.raffles;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class RafflesEndpoint extends CafeEndpoint {

    public CompletableFuture<HashMap<String, ArrayList<Raffle>>> getAllRaffles() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/raffles")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    HashMap<String, ArrayList<Raffle>> raffles = new HashMap<>();

                    request.getData().get("raffles").forEach((raffle) -> {
                        String guildID = raffle.get("guild_id").asText();
                        String messageID = raffle.get("message_id").asText();
                        Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(raffle.get("ending_time").asText()).orElse(null);
                        int winnerAmount = raffle.get("winner_amount").asInt();

                        if (!raffles.containsKey(guildID)) raffles.put(guildID, new ArrayList<>());

                        raffles.get(guildID).add(new Raffle(messageID, endingTime, winnerAmount));
                    });

                    return raffles;
                });
    }

    public CompletableFuture<ArrayList<Raffle>> getGuildRaffles(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/raffles/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    ArrayList<Raffle> raffles = new ArrayList<>();

                    request.getData().get("raffles").forEach((raffle) -> {
                        String messageID = raffle.get("message_id").asText();
                        Timestamp endingTime = CafeGeneric.parseTimestampFromAPI(raffle.get("ending_time").asText()).orElse(null);
                        int winnerAmount = raffle.get("winner_amount").asInt();

                        raffles.add(new Raffle(messageID, endingTime, winnerAmount));
                    });

                    return raffles;
                });
    }

    public CompletableFuture<Boolean> createRaffle(final String guildID, final Raffle raffle) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/raffles/" + guildID)
                .addParameter("message_id", raffle.getMessageID())
                .addParameter("ending_time", raffle.getEndingTime().toString())
                .addParameter("winner_amount", String.valueOf(raffle.getWinnerAmount()))
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> deleteRaffle(final String guildID, final Raffle raffle) {
        return deleteRaffle(guildID, raffle.getMessageID());
    }

    public CompletableFuture<Boolean> deleteRaffle(final String guildID, final String messageID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/raffles/" + guildID)
                .addParameter("message_id", messageID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

}
