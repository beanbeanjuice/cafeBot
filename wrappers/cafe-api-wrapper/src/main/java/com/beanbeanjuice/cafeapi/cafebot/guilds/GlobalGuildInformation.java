package com.beanbeanjuice.cafeapi.cafebot.guilds;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.exception.api.*;
import com.beanbeanjuice.cafeapi.requests.Request;
import com.beanbeanjuice.cafeapi.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

/**
 * A class used for {@link GlobalGuildInformation} requests to the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class GlobalGuildInformation implements com.beanbeanjuice.cafeapi.api.CafeAPI {

    private String apiKey;

    /**
     * Creates a new {@link GlobalGuildInformation} object.
     * @param apiKey The {@link String apiKey} used for authorization.
     */
    public GlobalGuildInformation(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retrieves all {@link GuildInformation} from the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with keys of {@link String guildID} with a value of {@link GuildInformation guildInformation}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public HashMap<String, GuildInformation> getAllGuildInformation()
            throws AuthorizationException, ResponseException {
        HashMap<String, GuildInformation> guilds = new HashMap<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
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
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/guilds/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return parseGuildInformation(request.getData().get("guild"));
    }

    /**
     * Creates a new {@link GuildInformation} in the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} to create.
     * @return True, if the {@link GlobalGuildInformation} was successfully created for the specified {@link String guildID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws ConflictException Thrown when the specified {@link String guildID} already exists in the {@link CafeAPI CafeAPI}.
     */
    public boolean createGuildInformation(String guildID)
            throws AuthorizationException, ResponseException, ConflictException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
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

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.PATCH)
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
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
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
        String prefix = guild.get(GuildInformationType.PREFIX.getType()).asText();
        String moderatorRoleID = guild.get(GuildInformationType.MODERATOR_ROLE_ID.getType()).asText();
        String twitchChannelID = guild.get(GuildInformationType.TWITCH_CHANNEL_ID.getType()).asText();
        String mutedRoleID = guild.get(GuildInformationType.MUTED_ROLE_ID.getType()).asText();
        String liveNotificationsRoleID = guild.get(GuildInformationType.LIVE_NOTIFICATIONS_ROLE_ID.getType()).asText();
        boolean notifyOnUpdate = convertToBoolean(guild.get(GuildInformationType.NOTIFY_ON_UPDATE.getType()).asText());
        String updateChannelID = guild.get(GuildInformationType.UPDATE_CHANNEL_ID.getType()).asText();
        String countingChannelID = guild.get(GuildInformationType.COUNTING_CHANNEL_ID.getType()).asText();
        String pollChannelID = guild.get(GuildInformationType.POLL_CHANNEL_ID.getType()).asText();
        String raffleChannelID = guild.get(GuildInformationType.RAFFLE_CHANNEL_ID.getType()).asText();
        String birthdayChannelID = guild.get(GuildInformationType.BIRTHDAY_CHANNEL_ID.getType()).asText();
        String welcomeChannelID = guild.get(GuildInformationType.WELCOME_CHANNEL_ID.getType()).asText();
        String goodbyeChannelID = guild.get(GuildInformationType.GOODBYE_CHANNEL_ID.getType()).asText();
        String logChannelID = guild.get(GuildInformationType.LOG_CHANNEL_ID.getType()).asText();
        String ventingChannelID = guild.get(GuildInformationType.VENTING_CHANNEL_ID.getType()).asText();
        boolean aiResponseStatus = convertToBoolean(guild.get(GuildInformationType.AI_RESPONSE.getType()).asText());
        String dailyChannelID = guild.get(GuildInformationType.DAILY_CHANNEL_ID.getType()).asText();

        return new GuildInformation(
                prefix, moderatorRoleID, twitchChannelID,
                mutedRoleID, liveNotificationsRoleID, notifyOnUpdate,
                updateChannelID, countingChannelID, pollChannelID,
                raffleChannelID, birthdayChannelID, welcomeChannelID,
                goodbyeChannelID, logChannelID, ventingChannelID,
                aiResponseStatus, dailyChannelID
        );
    }

    /**
     * Converts a specified {@link String} to a {@link Boolean}.
     * @param string The specified {@link String}.
     * @return The parsed {@link Boolean}. False, by default, if there was an error.
     */
    private boolean convertToBoolean(String string) {
        return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("1");
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
