package com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A class used for {@link GuildsEndpoint} requests to the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class GuildsEndpoint extends CafeEndpoint {

    /**
     * Retrieves all {@link GuildInformation} from the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with keys of {@link String guildID} with a value of {@link GuildInformation guildInformation}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public HashMap<String, GuildInformation> getAllGuildInformation()
            throws AuthorizationException, ResponseException {
        HashMap<String, GuildInformation> guilds = new HashMap<>();

        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/guilds")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        for (JsonNode guild : request.getData().get("guilds")) {
            String guildID = guild.get("guild_id").asText();
            guilds.put(guildID, parseGuildInformation(guild));
        }

        return guilds;
    }

    /**
     * Retrieves the {@link GuildInformation} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @return The {@link GuildInformation} associated with the {@link String guildID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown then the specified {@link String guildID} does not exist in the {@link CafeAPI CafeAPI}.
     */
    public GuildInformation getGuildInformation(String guildID)
            throws AuthorizationException, ResponseException, NotFoundException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/guilds/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return parseGuildInformation(request.getData().get("guild"));
    }

    /**
     * Creates a new {@link GuildInformation} in the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} to create.
     * @return True, if the {@link GuildsEndpoint} was successfully created for the specified {@link String guildID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws ConflictException Thrown when the specified {@link String guildID} already exists in the {@link CafeAPI CafeAPI}.
     */
    public boolean createGuildInformation(String guildID)
            throws AuthorizationException, ResponseException, ConflictException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/guilds/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Updates the {@link GuildInformation} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @param type The {@link GuildInformationType type} of value.
     * @param value The {@link String value} to update.
     * @return True, if the {@link GuildInformationType type} was successfully updated to the specified {@link String value} for the {@link String guildID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the specified {@link String guildID} was not found in the {@link CafeAPI CafeAPI}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     * @throws TeaPotException Thrown when a variable is mismatched with its actual value. I.E. - Setting a boolean to "falfse" instead of "false"
     */
    public boolean updateGuildInformation(String guildID, GuildInformationType type, Object value)
            throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException, TeaPotException {

        switch (type){
            case PREFIX, MODERATOR_ROLE_ID, TWITCH_CHANNEL_ID,
                 MUTED_ROLE_ID, LIVE_NOTIFICATIONS_ROLE_ID, UPDATE_CHANNEL_ID,
                 COUNTING_CHANNEL_ID, POLL_CHANNEL_ID, RAFFLE_CHANNEL_ID,
                 BIRTHDAY_CHANNEL_ID, WELCOME_CHANNEL_ID, LOG_CHANNEL_ID,
                 VENTING_CHANNEL_ID, DAILY_CHANNEL_ID -> {
                if (!value.getClass().equals(String.class)) {
                    throw new TeaPotException(value + " is invalid for " + type);
                }
            }

            case NOTIFY_ON_UPDATE, AI_RESPONSE -> {
                if (!value.getClass().equals(Boolean.class)) {
                    throw new TeaPotException(value + " is invalid for " + type);
                }

                if ((Boolean) value) {
                    value = "1";
                } else {
                    value = "0";
                }
            }

        }

        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/guilds/" + guildID)
                .addParameter("type", type.getType())
                .addParameter("value", value.toString())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Deletes a {@link GuildInformation} for a specified {@link String guildID} in the {@link CafeAPI CafeAPI}.
     * @param guildID The specified {@link String guildID}.
     * @return True, if the {@link GuildInformation} was successfully deleted for the specified {@link String guildID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public boolean deleteGuildInformation(String guildID)
            throws AuthorizationException, ResponseException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/guilds/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Parses a {@link JsonNode} for its {@link GuildInformation}.
     * @param guild The {@link JsonNode} to parse.
     * @return The parsed {@link GuildInformation}.
     */
    private GuildInformation parseGuildInformation(JsonNode guild) {
        HashMap<GuildInformationType, String> guildSettings = new HashMap<>();

        Arrays.stream(GuildInformationType.values()).forEach((type) -> {
            if (type == GuildInformationType.AI_RESPONSE || type == GuildInformationType.NOTIFY_ON_UPDATE) {
                guildSettings.put(type, convertToBooleanString(guild.get(type.getType()).asText()));
                return;
            }

            guildSettings.put(type, guild.get(type.getType()).asText());
        });

        return new GuildInformation(guildSettings);
    }

    /**
     * Converts a specified {@link String} to a {@link Boolean}.
     * @param string The specified {@link String}.
     * @return The parsed {@link Boolean}. False, by default, if there was an error.
     */
    private String convertToBooleanString(String string) {
        if (string.equalsIgnoreCase("true") || string.equalsIgnoreCase("1")) return "true";
        else return "false";
    }

}
