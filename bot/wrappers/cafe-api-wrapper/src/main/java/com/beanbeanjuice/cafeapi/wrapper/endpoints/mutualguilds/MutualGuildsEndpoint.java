package com.beanbeanjuice.cafeapi.wrapper.endpoints.mutualguilds;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MutualGuildsEndpoint extends CafeEndpoint {

    public CompletableFuture<List<String>> getMutualGuilds(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/mutual_guilds/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    ArrayNode arrayNode = (ArrayNode) request.getData().get("mutual_guilds");
                    List<String> mutualGuilds = new ArrayList<>();

                    if(arrayNode.isArray()) {
                        for(JsonNode jsonNode : arrayNode) {
                            mutualGuilds.add(jsonNode.asText());
                        }
                    }

                    return mutualGuilds;
                });
    }

}
