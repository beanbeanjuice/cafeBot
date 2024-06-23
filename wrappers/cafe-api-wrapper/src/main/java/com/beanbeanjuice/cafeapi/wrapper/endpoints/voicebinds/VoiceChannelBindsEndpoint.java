package com.beanbeanjuice.cafeapi.wrapper.endpoints.voicebinds;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ResponseException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.UndefinedVariableException;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used for {@link VoiceChannelBindsEndpoint} requests in the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class VoiceChannelBindsEndpoint extends CafeEndpoint {

    /**
     * Gets all {@link VoiceChannelBind} in the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with a {@link String key} of Discord Server IDs and a value of {@link ArrayList} containing {@link VoiceChannelBind}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public HashMap<String, ArrayList<VoiceChannelBind>> getAllVoiceChannelBinds()
    throws AuthorizationException, ResponseException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/voice_binds")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        HashMap<String, ArrayList<VoiceChannelBind>> voiceBinds = new HashMap<>();

        request.getData().get("voice_binds").forEach((bind) -> {
            String guildID = bind.get("guild_id").asText();
            String voiceChannelID = bind.get("voice_channel_id").asText();
            String roleID = bind.get("role_id").asText();

            if (!voiceBinds.containsKey(guildID)) voiceBinds.put(guildID, new ArrayList<>());

            voiceBinds.get(guildID).add(new VoiceChannelBind(voiceChannelID, roleID));
        });

        return voiceBinds;
    }

    /**
     * Gets all {@link VoiceChannelBind} for a specific Discord server.
     * @param guildID The {@link String guildID} of the Discord server.
     * @return An {@link ArrayList} of {@link VoiceChannelBind} that the server has.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public ArrayList<VoiceChannelBind> getGuildVoiceChannelBinds(final String guildID)
    throws AuthorizationException, ResponseException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/voice_binds/" + guildID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        ArrayList<VoiceChannelBind> voiceChannelBinds = new ArrayList<>();

        request.getData().get("voice_binds").forEach((bind) -> {
            String voiceChannelID = bind.get("voice_channel_id").asText();
            String roleID = bind.get("role_id").asText();

            voiceChannelBinds.add(new VoiceChannelBind(voiceChannelID, roleID));
        });

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
    public boolean addVoiceChannelBind(final String guildID, final VoiceChannelBind voiceChannelBind)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
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
    public boolean deleteVoiceChannelBind(final String guildID, final VoiceChannelBind voiceChannelBind)
    throws AuthorizationException, ResponseException, UndefinedVariableException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/voice_binds/" + guildID)
                .addParameter("voice_channel_id", voiceChannelBind.getVoiceChannelID())
                .addParameter("role_id", voiceChannelBind.getRoleID())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

}
