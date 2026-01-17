package com.beanbeanjuice.cafebot.api.wrapper.api.discord.user;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameStatusType;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameType;
import com.beanbeanjuice.cafebot.api.wrapper.type.Game;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class GamesApi extends Api {

    public GamesApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<Game> getGame(int gameId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/games/id/%s", gameId))
                    .queue()
                    .thenApply((basicResponse) -> {
                        JsonNode gameNode = basicResponse.getBody().get("game");
                        return parseGame(gameNode);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<ArrayList<Game>> getGames(String userId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/games/%s", userId))
                    .queue()
                    .thenApply((basicResponse) -> {
                        JsonNode gamesNode = basicResponse.getBody().get("games");
                        return parseGames(gamesNode);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<ArrayList<Game>> getGames(String userId, GameType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/games/%s/%s", userId, type.toString()))
                    .queue()
                    .thenApply((basicResponse) -> {
                        JsonNode gamesNode = basicResponse.getBody().get("games");
                        return parseGames(gamesNode);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Game> createGame(GameType type, int wager, String[] playerIds) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("players", playerIds);
        map.put("wager", wager);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/games/%s", type.toString()))
                    .body(map)
                    .queue()
                    .thenApply((basicResponse) -> {
                        JsonNode gameNode = basicResponse.getBody().get("game");
                        return parseGame(gameNode);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Game> updateGame(int gameId, GameStatusType status, String[] winnerIds) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("winners", winnerIds);

        try {
            return RequestBuilder.builder()
                    .method(Method.PATCH)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/games/id/%s", gameId))
                    .body(map)
                    .queue()
                    .thenApply((basicResponse) -> {
                        JsonNode gameNode = basicResponse.getBody().get("game");
                        return parseGame(gameNode);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private Game parseGame(JsonNode gameNode) {
        ArrayList<String> players = new ArrayList<>();
        ArrayList<String> winners = new ArrayList<>();

        for (JsonNode playerNode : gameNode.get("players")) {
            players.add(playerNode.get("id").asString());
        }

        for (JsonNode winnersNode : gameNode.get("winners")) {
            winners.add(winnersNode.get("id").asString());
        }

        int id = gameNode.get("id").asInt();
        GameType type = GameType.valueOf(gameNode.get("type").asString());
        GameStatusType gameStatus = GameStatusType.valueOf(gameNode.get("status").asString());
        int wager = gameNode.get("wager").asInt();
        int pool = gameNode.get("pool").asInt();

        return new Game(id, type, gameStatus, players.toArray(new String[0]), winners.toArray(new String[0]), wager, pool);
    }

    private ArrayList<Game> parseGames(JsonNode gamesNode) {
        ArrayList<Game> games = new ArrayList<>();
        for (JsonNode gameNode : gamesNode) {
            games.add(parseGame(gameNode));
        }
        return games;
    }

}
