package com.beanbeanjuice.cafeapi.wrapper.endpoints.twitches;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ResponseException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.UndefinedVariableException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class used for the Twitch API for the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class TwitchEndpoint extends CafeEndpoint {

    /**
     * Retrieves all Twitch channels from the {@link CafeAPI}.
     * @return A {@link HashMap} with keys of {@link String guildID} and a value of {@link ArrayList} of {@link String twitchChannelName}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public HashMap<String, ArrayList<String>> getAllTwitches()
    throws AuthorizationException, ResponseException {
        HashMap<String, ArrayList<String>> twitches = new HashMap<>();

        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/guilds/twitch")
                .setAuthorization(apiKey)
                .build();

        request.getData().get("guilds_twitch").forEach((guildTwitch) -> {
            String guildID = guildTwitch.get("guild_id").asText();
            String twitchChannel = guildTwitch.get("twitch_channel").asText();

            if (!twitches.containsKey(guildID)) twitches.put(guildID, new ArrayList<>());

            twitches.get(guildID).add(twitchChannel);
        });

        return twitches;
    }

    /**
     * Retrieves all Twitch {@link String twitchChannelName} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @return The {@link ArrayList} of {@link String twitchChannelName}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public ArrayList<String> getGuildTwitches(final String guildID)
    throws AuthorizationException, ResponseException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/guilds/twitch/" + guildID)
                .setAuthorization(apiKey)
                .build();

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                    request.getData().get("twitch_channels").toString(),
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class)
            );
        } catch (JsonProcessingException e) {
            Logger.getLogger(TwitchEndpoint.class.getName()).log(Level.SEVERE, "There was an error processing the json node: " + e.getMessage());
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
    public boolean addGuildTwitch(final String guildID, final String twitchChannelName)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/guilds/twitch/" + guildID)
                .addParameter("twitch_channel", twitchChannelName)
                .setAuthorization(apiKey)
                .build();

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
    public boolean removeGuildTwitch(final String guildID, final String twitchChannelName)
    throws AuthorizationException, ResponseException, UndefinedVariableException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/guilds/twitch/" + guildID)
                .addParameter("twitch_channel", twitchChannelName)
                .setAuthorization(apiKey)
                .build();

        return request.getStatusCode() == 200;
    }

}
