package com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class WinStreaksEndpoint extends CafeEndpoint {

    public CompletableFuture<HashMap<String, WinStreak>> getAllWinStreaks() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/minigames/win_streaks")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    HashMap<String, WinStreak> winStreaks = new HashMap<>();

                    request.getData().get("win_streaks").forEach((winStreak) -> {
                        String userID = winStreak.get("user_id").asText();
                        int ticTacToeWins = winStreak.get("tic_tac_toe").asInt();
                        int connectFourWins = winStreak.get("connect_four").asInt();
                        winStreaks.put(userID, new WinStreak(ticTacToeWins, connectFourWins));
                    });

                    return winStreaks;
                });
    }

    public CompletableFuture<WinStreak> getUserWinStreak(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/minigames/win_streaks/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    JsonNode winStreak = request.getData().get("win_streaks");
                    int ticTacToeWins = winStreak.get("tic_tac_toe").asInt();
                    int connectFourWins = winStreak.get("connect_four").asInt();

                    return new WinStreak(ticTacToeWins, connectFourWins);
                });
    }

    public CompletableFuture<Boolean> updateUserWinStreak(final String userID, final MinigameType type, final int winstreak) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/minigames/win_streaks/" + userID)
                .addParameter("type", type.getType())
                .addParameter("value", String.valueOf(winstreak))
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    public CompletableFuture<Boolean> createUserWinStreak(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/minigames/win_streaks/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> deleteUserWinStreak(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/minigames/win_streaks/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

}
