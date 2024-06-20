package com.beanbeanjuice.cafeapi.cafebot.counting;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.exception.api.*;
import com.beanbeanjuice.cafeapi.requests.Request;
import com.beanbeanjuice.cafeapi.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

/**
 * A class used for {@link CountingInformation} requests for the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class GlobalCountingInformation implements com.beanbeanjuice.cafeapi.api.CafeAPI {

    private String apiKey;

    /**
     * Creates a new {@link GlobalCountingInformation object}.
     * @param apiKey The {@link String apiKey} used for authorization.
     */
    public GlobalCountingInformation(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retrieves all {@link CountingInformation} in the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with keys of {@link String userID} and values of {@link CountingInformation guildCountingInformation}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public HashMap<String, CountingInformation> getAllCountingInformation()
            throws AuthorizationException, ResponseException {
        HashMap<String, CountingInformation> guilds = new HashMap<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/counting/guilds")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        for (JsonNode guild : request.getData().get("guilds")) {
            String guildID = guild.get("guild_id").asText();

            guilds.put(guildID, parseCountingInformation(guild));
        }

        return guilds;
    }

    /**
     * Retrieves the {@link CountingInformation} for a specified {@link String guildID}.
     * @param guildID THe specified {@link String guildID}.
     * @return The {@link CountingInformation} for the specified {@link String guildID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the {@link CountingInformation} does not exist for the specified {@link String guildID}.
     */
    public CountingInformation getGuildCountingInformation(String guildID)
            throws AuthorizationException, ResponseException, NotFoundException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/counting/guilds/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return parseCountingInformation(request.getData().get("counting_information"));
    }

    /**
     * Updates the {@link CountingInformation} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @param highestNumber The new {@link Integer highestNumber}.
     * @param lastNumber The new {@link Integer lastNumber}.
     * @param lastUserID The new {@link String lastuserID}.
     * @return True, if the {@link CountingInformation} was successfully updated for the specified {@link String guildID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the {@link CountingInformation} was not found for the specified {@link String guildID}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public Boolean updateGuildCountingInformation(String guildID, int highestNumber, int lastNumber,
                                                  String lastUserID, String failureRoleID)
            throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/counting/guilds/" + guildID)
                .addParameter("highest_number", String.valueOf(highestNumber))
                .addParameter("last_number", String.valueOf(lastNumber))
                .addParameter("last_user_id", lastUserID)
                .addParameter("failure_role_id", failureRoleID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Updated the {@link CountingInformation} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @param countingInformation The new {@link CountingInformation}.
     * @return True, if the {@link CountingInformation} was successfully updated for the specified {@link String guildID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the {@link CountingInformation} was not found for the specified {@link String guildID}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean updateGuildCountingInformation(String guildID, CountingInformation countingInformation)
            throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        return updateGuildCountingInformation(
                guildID,
                countingInformation.getHighestNumber(),
                countingInformation.getLastNumber(),
                countingInformation.getLastUserID(),
                countingInformation.getFailureRoleID()
        );
    }

    /**
     * Creates a new {@link CountingInformation} for the specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @return True, if the {@link CountingInformation} was successfully created for the specified {@link String guildID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws ConflictException Thrown when the {@link CountingInformation} already exists for the specified {@link String guildID}.
     */
    public boolean createGuildCountingInformation(String guildID)
            throws AuthorizationException, ResponseException, ConflictException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/counting/guilds/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes the {@link CountingInformation} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @return True, if the {@link CountingInformation} was successfully deleted for the specified {@link String guildID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public boolean deleteGuildCountingInformation(String guildID)
            throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/counting/guilds/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Parses a {@link JsonNode} for its {@link CountingInformation}.
     * @param node The {@link JsonNode node} to parse.
     * @return The parsed {@link CountingInformation}.
     */
    private CountingInformation parseCountingInformation(JsonNode node) {
        int highestNumber = node.get("highest_number").asInt();
        int lastNumber = node.get("last_number").asInt();
        String lastUserID = node.get("last_user_id").asText();
        String failureRoleID = node.get("failure_role_id").asText();

        return new CountingInformation(
                highestNumber,
                lastNumber,
                lastUserID,
                failureRoleID
        );
    }

    /**
     * Updates the {@link String apiKey}.
     * @param apiKey The new {@link String apiKey}.
     */
    @Override
    public void updateAPIKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
