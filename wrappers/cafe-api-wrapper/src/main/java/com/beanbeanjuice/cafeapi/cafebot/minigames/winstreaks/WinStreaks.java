package com.beanbeanjuice.cafeapi.cafebot.minigames.winstreaks;

import com.beanbeanjuice.cafeapi.api.CafeAPI;
import com.beanbeanjuice.cafeapi.exception.api.*;
import com.beanbeanjuice.cafeapi.requests.Request;
import com.beanbeanjuice.cafeapi.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

/**
 * A class used for {@link WinStreaks} API requests.
 *
 * @author beanbeanjuice
 */
public class WinStreaks implements CafeAPI {

    private String apiKey;

    /**
     * Creates a new {@link WinStreaks} object.
     * @param apiKey The {@link String apiKey} used for authorization.
     */
    public WinStreaks(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retrieves all {@link WinStreak} in the {@link com.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @return The {@link HashMap} with keys of {@link String userID} and a value of {@link WinStreak}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public HashMap<String, WinStreak> getAllWinStreaks()
    throws AuthorizationException, ResponseException {
        HashMap<String, WinStreak> winStreaks = new HashMap<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/minigames/win_streaks")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        for (JsonNode winStreak : request.getData().get("win_streaks")) {
            String userID = winStreak.get("user_id").asText();
            Integer ticTacToeWins = winStreak.get("tic_tac_toe").asInt();
            Integer connectFourWins = winStreak.get("connect_four").asInt();
            winStreaks.put(userID, new WinStreak(ticTacToeWins, connectFourWins));
        }

        return winStreaks;
    }

    /**
     * Retrieves the {@link WinStreak} data for the specified Discord user.
     * @param userID The {@link String userID} of the Discord user.
     * @return The {@link WinStreak} the user has.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the specified {@link String userID}'s {@link WinStreak} does not exist.
     */
    public WinStreak getUserWinStreak(String userID)
    throws AuthorizationException, ResponseException, NotFoundException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/minigames/win_streaks/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        JsonNode winStreak = request.getData().get("win_streaks");
        int ticTacToeWins = winStreak.get("tic_tac_toe").asInt();
        int connectFourWins = winStreak.get("connect_four").asInt();

        return new WinStreak(ticTacToeWins, connectFourWins);
    }

    /**
     * Updates a Discord user's {@link WinStreak}.
     * @param userID The {@link String userID} of the Discord user.
     * @param type The {@link MinigameType type} of {@link WinStreak} to update.
     * @param winstreak The {@link Integer winstreak} of {@link MinigameType type} to set the {@link WinStreak} to.
     * @return True, if the {@link WinStreak} was successfully updated.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the {@link WinStreak} for the specified {@link String userID} does not exist.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean updateUserWinStreak(String userID, MinigameType type, int winstreak)
    throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/minigames/win_streaks/" + userID)
                .addParameter("type", type.getType())
                .addParameter("value", String.valueOf(winstreak))
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Creates a {@link WinStreak} for a Discord user.
     * @param userID The {@link String userID} of the Discord user.
     * @return True, if the {@link WinStreak} was successfully created.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws ConflictException Thrown when the specified {@link String userID} already exists.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean createUserWinStreak(String userID)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/minigames/win_streaks/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes a {@link WinStreak} for a specified Discord user.
     * @param userID The {@link String userID} of the Discord user.
     * @return True, if the {@link WinStreak} was successfully deleted.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public boolean deleteUserWinStreak(String userID)
    throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/minigames/win_streaks/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Updates the {@link String apiKey}.
     * @param apiKey The new {@link String apikey}.
     */
    @Override
    public void updateAPIKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
