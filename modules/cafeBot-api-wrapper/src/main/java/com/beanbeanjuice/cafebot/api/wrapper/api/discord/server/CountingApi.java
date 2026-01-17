package com.beanbeanjuice.cafebot.api.wrapper.api.discord.server;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.CountingStatistics;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CountingApi extends Api {

    public CountingApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<Map<String, CountingStatistics>> getCountingStatistics() {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/servers/counting")
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("countingStatistics"))
                    .thenApply((allStatisticsNode) -> {
                        HashMap<String, CountingStatistics> map = new HashMap<>();

                        for (JsonNode node : allStatisticsNode) {
                            CountingStatistics statistics = parseCountingStatistics(node);

                            map.put(statistics.getGuildId(), statistics);
                        }

                        return map;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<CountingStatistics> getCountingStatistics(String guildId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/counting/%s", guildId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("countingStatistics"))
                    .thenApply(this::parseCountingStatistics);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<CountingStatistics> updateCountingStatistics(String guildId, String userId, int number) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("number", number);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/counting/%s", guildId))
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("countingStatistics"))
                    .thenApply(this::parseCountingStatistics);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private CountingStatistics parseCountingStatistics(JsonNode node) {
        String guildId = node.get("guildId").asString();
        int highestCount = node.get("highestCount").asInt();
        int currentCount = node.get("currentCount").asInt();
        String lastUserId = node.get("lastUser").isNull() ? null : node.get("lastUser").asString();

        return new CountingStatistics(guildId, highestCount, currentCount, lastUserId);
    }

}

