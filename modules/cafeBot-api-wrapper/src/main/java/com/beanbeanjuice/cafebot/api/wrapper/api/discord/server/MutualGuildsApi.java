package com.beanbeanjuice.cafebot.api.wrapper.api.discord.server;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.type.MutualGuild;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MutualGuildsApi extends Api {

    public MutualGuildsApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<List<String>> getMutualGuilds(String userId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/mutual_guilds/%s", userId))
                    .queue()
                    .thenApply((response) -> response.getBody().get("guildIds"))
                    .thenApply(this::parseMutualGuilds);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Integer> addMutualGuilds(List<MutualGuild> newMutualGuilds) {
        List<Map<String, String>> mutualGuilds = new ArrayList<>();
        newMutualGuilds.forEach((mutualGuild) -> mutualGuilds.add(Map.of("userId", mutualGuild.getUserId(), "guildId", mutualGuild.getGuildId())));

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/servers/mutual_guilds/batch")
                    .body(mutualGuilds)
                    .queue()
                    .thenApply((response) -> response.getBody().get("count").asInt());
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Void> deleteMutualGuilds(List<MutualGuild> newMutualGuilds) {
        List<Map<String, String>> mutualGuilds = new ArrayList<>();
        newMutualGuilds.forEach((mutualGuild) -> mutualGuilds.add(Map.of("userId", mutualGuild.getUserId(), "guildId", mutualGuild.getGuildId())));

        try {
            return RequestBuilder.builder()
                    .method(Method.DELETE)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/servers/mutual_guilds/batch")
                    .body(mutualGuilds)
                    .queue()
                    .thenApply((response) -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private List<String> parseMutualGuilds(JsonNode node) {
        ArrayList<String> guildIds = new ArrayList<>();
        for (JsonNode guildId : node) guildIds.add(guildId.asString());
        return guildIds;
    }

}
