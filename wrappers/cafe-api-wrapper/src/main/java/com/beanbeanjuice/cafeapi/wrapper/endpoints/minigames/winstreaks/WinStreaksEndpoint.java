package com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

/**
 * A class used for {@link WinStreaksEndpoint} API requests.
 *
 * @author beanbeanjuice
 */
public class WinStreaksEndpoint extends CafeEndpoint {

    /**
     * Retrieves all {@link WinStreak} in the {@link com.beanbeanjuice.cafeapi.wrapper.CafeAPI CafeAPI}.
     * @return The {@link HashMap} with keys of {@link String userID} and a value of {@link WinStreak}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public HashMap<String, WinStreak> getAllWinStreaks()
    throws AuthorizationException, ResponseException {
        HashMap<String, WinStreak> winStreaks = new HashMap<>();

        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/minigames/win_streaks")
                .setAuthorization(apiKey)
                .build();

        request.getData().get("win_streaks").forEach((winStreak) -> {
            String userID = winStreak.get("user_id").asText();
            int ticTacToeWins = winStreak.get("tic_tac_toe").asInt();
            int connectFourWins = winStreak.get("connect_four").asInt();
            winStreaks.put(userID, new WinStreak(ticTacToeWins, connectFourWins));
        });

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
    public WinStreak getUserWinStreak(final String userID)
    throws AuthorizationException, ResponseException, NotFoundException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/minigames/win_streaks/" + userID)
                .setAuthorization(apiKey)
                .build();

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
    public boolean updateUserWinStreak(final String userID, final MinigameType type, final int winstreak)
    throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/minigames/win_streaks/" + userID)
                .addParameter("type", type.getType())
                .addParameter("value", String.valueOf(winstreak))
                .setAuthorization(apiKey)
                .build();

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
    public boolean createUserWinStreak(final String userID)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/minigames/win_streaks/" + userID)
                .setAuthorization(apiKey)
                .build();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes a {@link WinStreak} for a specified Discord user.
     * @param userID The {@link String userID} of the Discord user.
     * @return True, if the {@link WinStreak} was successfully deleted.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public boolean deleteUserWinStreak(final String userID)
    throws AuthorizationException, ResponseException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/minigames/win_streaks/" + userID)
                .setAuthorization(apiKey)
                .build();

        return request.getStatusCode() == 200;
    }

}
