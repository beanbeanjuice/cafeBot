package com.beanbeanjuice.cafeapi.wrapper.endpoints.twitches;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TwitchEndpoint extends CafeEndpoint {

    public CompletableFuture<HashMap<String, ArrayList<String>>> getAllTwitches() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/guilds/twitch")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    HashMap<String, ArrayList<String>> twitches = new HashMap<>();
                    request.getData().get("guilds_twitch").forEach((guildTwitch) -> {
                        String guildID = guildTwitch.get("guild_id").asText();
                        String twitchChannel = guildTwitch.get("twitch_channel").asText();

                        if (!twitches.containsKey(guildID)) twitches.put(guildID, new ArrayList<>());

                        twitches.get(guildID).add(twitchChannel);
                    });

                    return twitches;
                });
    }

    public CompletableFuture<ArrayList<String>> getGuildTwitches(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/guilds/twitch/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(
                                request.getData().get("twitch_channels").toString(),
                                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class)
                        );
                    } catch (JsonProcessingException e) {
                        Logger.getLogger(TwitchEndpoint.class.getName()).log(Level.SEVERE, "There was an error processing the json node: " + e.getMessage());
                        throw new CompletionException(e);
                    }
                });
    }

    public CompletableFuture<Boolean> addGuildTwitch(final String guildID, final String twitchChannelName) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/guilds/twitch/" + guildID)
                .addParameter("twitch_channel", twitchChannelName)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> removeGuildTwitch(final String guildID, final String twitchChannelName) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/guilds/twitch/" + guildID)
                .addParameter("twitch_channel", twitchChannelName)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

}
