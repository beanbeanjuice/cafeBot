package com.beanbeanjuice.cafeapi.cafebot.twitches;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.requests.Request;
import com.beanbeanjuice.cafeapi.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.requests.RequestType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.beanbeanjuice.cafeapi.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.exception.api.ResponseException;
import com.beanbeanjuice.cafeapi.exception.api.UndefinedVariableException;
import com.beanbeanjuice.cafeapi.exception.api.CafeException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used for the Twitch API for the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class GuildTwitches implements com.beanbeanjuice.cafeapi.api.CafeAPI {

    private String apiKey;

    /**
     * Creates a new {@link GuildTwitches} object.
     * @param apiKey The {@link String apiKey} used for authorization.
     */
    public GuildTwitches(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retrieves all Twitch channels from the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with keys of {@link String guildID} and a value of {@link ArrayList} of {@link String twitchChannelName}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public HashMap<String, ArrayList<String>> getAllTwitches()
    throws AuthorizationException, ResponseException {
        HashMap<String, ArrayList<String>> twitches = new HashMap<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/guilds/twitch")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        for (JsonNode guildTwitch : request.getData().get("guilds_twitch")) {
            String guildID = guildTwitch.get("guild_id").asText();
            String twitchChannel = guildTwitch.get("twitch_channel").asText();

            if (!twitches.containsKey(guildID)) {
                twitches.put(guildID, new ArrayList<>());
            }

            twitches.get(guildID).add(twitchChannel);
        }

        return twitches;
    }

    /**
     * Retrieves all Twitch {@link String twitchChannelName} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @return The {@link ArrayList} of {@link String twitchChannelName}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public ArrayList<String> getGuildTwitches(String guildID)
    throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/guilds/twitch/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        try {
            return new ObjectMapper().readValue(request.getData().get("twitch_channels").toString(), ArrayList.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Adds a {@link String twitchChannelName} to the {@link CafeAPI CafeAPI} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @param twitchChannelName The {@link String twitchChannelName} to add.
     * @return True, if the {@link String twitchChannelName} was successfully added.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     * @throws ConflictException Thrown when the {@link String twitchChannelName} already exists for the specified {@link String guildID}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean addGuildTwitch(String guildID, String twitchChannelName)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/guilds/twitch/" + guildID)
                .addParameter("twitch_channel", twitchChannelName)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Removes a {@link String twitchChannelName} from the {@link CafeAPI CafeAPI} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @param twitchChannelName The {@link String twitchChannelName} to add.
     * @return True, if the {@link String twitchChannelName} was successfully removed.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean removeGuildTwitch(String guildID, String twitchChannelName)
    throws AuthorizationException, ResponseException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/guilds/twitch/" + guildID)
                .addParameter("twitch_channel", twitchChannelName)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Updates the {@link String apikey}.
     * @param apiKey The new {@link String apiKey}.
     */
    @Override
    public void updateAPIKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
