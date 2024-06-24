package com.beanbeanjuice.cafeapi.wrapper.endpoints.counting;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

/**
 * A class used for {@link CountingInformation} requests for the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class CountingEndpoint extends CafeEndpoint {

    /**
     * Retrieves all {@link CountingInformation} in the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with keys of {@link String userID} and values of {@link CountingInformation guildCountingInformation}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public HashMap<String, CountingInformation> getAllCountingInformation()
            throws AuthorizationException, ResponseException {
        HashMap<String, CountingInformation> guilds = new HashMap<>();

        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/counting/guilds")
                .setAuthorization(apiKey)
                .build();

        request.getData().get("guilds").forEach((guild) -> {
            String guildID = guild.get("guild_id").asText();

            guilds.put(guildID, parseCountingInformation(guild));
        });

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
    public CountingInformation getGuildCountingInformation(final String guildID)
            throws AuthorizationException, ResponseException, NotFoundException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/counting/guilds/" + guildID)
                .setAuthorization(apiKey)
                .build();

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
    public Boolean updateGuildCountingInformation(final String guildID, final int highestNumber, final int lastNumber,
                                                  final String lastUserID, final String failureRoleID)
            throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/counting/guilds/" + guildID)
                .addParameter("highest_number", String.valueOf(highestNumber))
                .addParameter("last_number", String.valueOf(lastNumber))
                .addParameter("last_user_id", lastUserID)
                .addParameter("failure_role_id", failureRoleID)
                .setAuthorization(apiKey)
                .build();

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
    public boolean updateGuildCountingInformation(final String guildID, final CountingInformation countingInformation)
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
    public boolean createGuildCountingInformation(final String guildID)
            throws AuthorizationException, ResponseException, ConflictException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/counting/guilds/" + guildID)
                .setAuthorization(apiKey)
                .build();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes the {@link CountingInformation} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @return True, if the {@link CountingInformation} was successfully deleted for the specified {@link String guildID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public boolean deleteGuildCountingInformation(final String guildID)
            throws AuthorizationException, ResponseException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/counting/guilds/" + guildID)
                .setAuthorization(apiKey)
                .build();

        return request.getStatusCode() == 200;
    }

    /**
     * Parses a {@link JsonNode} for its {@link CountingInformation}.
     * @param node The {@link JsonNode node} to parse.
     * @return The parsed {@link CountingInformation}.
     */
    private CountingInformation parseCountingInformation(final JsonNode node) {
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

}
