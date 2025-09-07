package com.beanbeanjuice.cafebot.api.wrapper.api.discord.server;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.Raffle;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RaffleApi extends Api {

    public RaffleApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<Map<String, List<Raffle>>> getRaffles() {
        return getRaffles(true, true);
    }

    public CompletableFuture<Map<String, List<Raffle>>> getRaffles(boolean isActive, boolean isExpired) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/raffles?active=%s&expired=%s", isActive, isExpired))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("raffles"))
                    .thenApply((rafflesNode) -> {
                        HashMap<String, List<Raffle>> raffles = new HashMap<>();

                        for (JsonNode raffleNode : rafflesNode) {
                            Raffle raffle = parseRaffle(raffleNode);

                            raffles.putIfAbsent(raffle.getGuildId(), new ArrayList<>());
                            raffles.get(raffle.getGuildId()).add(raffle);
                        }

                        return raffles;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<List<Raffle>> getRaffles(String guildId) {
        return getRaffles(guildId, true, true);
    }

    public CompletableFuture<List<Raffle>> getRaffles(String guildId, boolean isActive, boolean isExpired) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/raffles/%s?active=%s&expired=%s", guildId, isActive, isExpired))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("raffles"))
                    .thenApply((rafflesNode) -> {
                        List<Raffle> raffles = new ArrayList<>();

                        for (JsonNode raffleNode : rafflesNode) {
                            raffles.add(parseRaffle(raffleNode));
                        }

                        return raffles;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Raffle> createRaffle(Raffle raffle) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("messageId", raffle.getMessageId());
        map.put("title", raffle.getTitle());
        raffle.getDescription().ifPresent((description) -> map.put("description", description));
        map.put("numWinners", raffle.getNumWinners());
        map.put("endsAt", raffle.getEndsAt().toEpochMilli());

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/raffles/%s", raffle.getGuildId()))
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("raffle"))
                    .thenApply(this::parseRaffle);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Raffle> getRaffle(String guildId, String messageId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/raffles/%s/raffle/%s", guildId, messageId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("raffle"))
                    .thenApply(this::parseRaffle);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Raffle> setSubmission(int raffleId, String userId, boolean status) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/raffles/%s/submit?status=%s", raffleId, status))
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("raffle"))
                    .thenApply(this::parseRaffle);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Raffle> toggleSubmission(int raffleId, String userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/raffles/%s/submit", raffleId))
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("raffle"))
                    .thenApply(this::parseRaffle);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Raffle> closeRaffle(int raffleId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.PATCH)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/raffles/%s", raffleId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply(node -> node.get("raffle"))
                    .thenApply(this::parseRaffle);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Void> deleteRaffle(int raffleId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.DELETE)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/raffles/%s", raffleId))
                    .queue()
                    .thenApply((res) -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private Raffle parseRaffle(JsonNode node) {
        int id = node.get("id").asInt();
        String guildId = node.get("guildId").asString();
        String messageId = node.get("messageId").asString();

        String title = node.get("title").asString();
        String description = node.get("description").isNull() ? null : node.get("description").asString();
        int numWinners = node.get("numWinners").asInt();
        boolean isActive = node.get("active").asBoolean();
        String endsAt = node.get("endsAt").asString();

        List<String> submissions = new ArrayList<>();
        for (JsonNode submissionNode : node.get("submissions")) {
            submissions.add(submissionNode.get("id").asString());
        }

        List<String> winners = new ArrayList<>();
        for (JsonNode winnerNode : node.get("winners")) {
            winners.add(winnerNode.get("id").asString());
        }

        return new Raffle(id, guildId, messageId, title, description, numWinners, isActive, endsAt, submissions.toArray(new String[0]), winners.toArray(new String[0]));
    }

}
