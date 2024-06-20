package com.beanbeanjuice.cafeapi.cafebot.voicebinds;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.exception.api.ResponseException;
import com.beanbeanjuice.cafeapi.exception.api.UndefinedVariableException;
import com.beanbeanjuice.cafeapi.requests.Request;
import com.beanbeanjuice.cafeapi.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;
import com.beanbeanjuice.cafeapi.exception.api.CafeException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used for {@link VoiceChannelBinds} requests in the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class VoiceChannelBinds implements com.beanbeanjuice.cafeapi.api.CafeAPI {

    private String apiKey;

    /**
     * Creates a new {@link VoiceChannelBinds} object.
     * @param apiKey The {@link String apiKey} used for authorization.
     */
    public VoiceChannelBinds(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Gets all {@link VoiceChannelBind} in the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with a {@link String key} of Discord Server IDs and a value of {@link ArrayList} containing {@link VoiceChannelBind}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public HashMap<String, ArrayList<VoiceChannelBind>> getAllVoiceChannelBinds()
    throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/voice_binds")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        HashMap<String, ArrayList<VoiceChannelBind>> voiceBinds = new HashMap<>();

        for (JsonNode bind : request.getData().get("voice_binds")) {
            String guildID = bind.get("guild_id").asText();
            String voiceChannelID = bind.get("voice_channel_id").asText();
            String roleID = bind.get("role_id").asText();

            if (!voiceBinds.containsKey(guildID)) {
                voiceBinds.put(guildID, new ArrayList<>());
            }

            voiceBinds.get(guildID).add(new VoiceChannelBind(voiceChannelID, roleID));
        }

        return voiceBinds;
    }

    /**
     * Gets all {@link VoiceChannelBind} for a specific Discord server.
     * @param guildID The {@link String guildID} of the Discord server.
     * @return An {@link ArrayList} of {@link VoiceChannelBind} that the server has.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public ArrayList<VoiceChannelBind> getGuildVoiceChannelBinds(String guildID)
    throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/voice_binds/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        ArrayList<VoiceChannelBind> voiceChannelBinds = new ArrayList<>();

        for (JsonNode bind : request.getData().get("voice_binds")) {
            String voiceChannelID = bind.get("voice_channel_id").asText();
            String roleID = bind.get("role_id").asText();

            voiceChannelBinds.add(new VoiceChannelBind(voiceChannelID, roleID));
        }

        return voiceChannelBinds;
    }

    /**
     * Adds a {@link VoiceChannelBind} for a Discord server in the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the Discord server.
     * @param voiceChannelBind The {@link VoiceChannelBind} to add to the Discord server.
     * @return True, if the {@link VoiceChannelBind} was successfully created.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     * @throws ConflictException Thrown when the {@link VoiceChannelBind} already exists for that Discord server.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean addVoiceChannelBind(String guildID, VoiceChannelBind voiceChannelBind)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/voice_binds/" + guildID)
                .addParameter("voice_channel_id", voiceChannelBind.getVoiceChannelID())
                .addParameter("role_id", voiceChannelBind.getRoleID())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes a {@link VoiceChannelBind} from the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the Discord server.
     * @param voiceChannelBind The {@link VoiceChannelBind} to delete from the Discord server.
     * @return True, if the {@link VoiceChannelBind} was successfully deleted.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean deleteVoiceChannelBind(String guildID, VoiceChannelBind voiceChannelBind)
    throws AuthorizationException, ResponseException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/voice_binds/" + guildID)
                .addParameter("voice_channel_id", voiceChannelBind.getVoiceChannelID())
                .addParameter("role_id", voiceChannelBind.getRoleID())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    @Override
    public void updateAPIKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
