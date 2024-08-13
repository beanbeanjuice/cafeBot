package com.beanbeanjuice.cafeapi.wrapper.endpoints.voicebinds;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class VoiceChannelBindsEndpoint extends CafeEndpoint {

    public CompletableFuture<HashMap<String, ArrayList<VoiceChannelBind>>> getAllVoiceChannelBinds() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/voice_binds")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    HashMap<String, ArrayList<VoiceChannelBind>> voiceBinds = new HashMap<>();

                    request.getData().get("voice_binds").forEach((bind) -> {
                        String guildID = bind.get("guild_id").asText();
                        String voiceChannelID = bind.get("voice_channel_id").asText();
                        String roleID = bind.get("role_id").asText();

                        if (!voiceBinds.containsKey(guildID)) voiceBinds.put(guildID, new ArrayList<>());

                        voiceBinds.get(guildID).add(new VoiceChannelBind(voiceChannelID, roleID));
                    });

                    return voiceBinds;
                });
    }

    public CompletableFuture<ArrayList<VoiceChannelBind>> getGuildVoiceChannelBinds(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/voice_binds/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    ArrayList<VoiceChannelBind> voiceChannelBinds = new ArrayList<>();

                    request.getData().get("voice_binds").forEach((bind) -> {
                        String voiceChannelID = bind.get("voice_channel_id").asText();
                        String roleID = bind.get("role_id").asText();

                        voiceChannelBinds.add(new VoiceChannelBind(voiceChannelID, roleID));
                    });

                    return voiceChannelBinds;
                });
    }

    public CompletableFuture<Boolean> addVoiceChannelBind(final String guildID, final VoiceChannelBind voiceChannelBind) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/voice_binds/" + guildID)
                .addParameter("voice_channel_id", voiceChannelBind.getVoiceChannelID())
                .addParameter("role_id", voiceChannelBind.getRoleID())
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> deleteVoiceChannelBind(final String guildID, final VoiceChannelBind voiceChannelBind) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/voice_binds/" + guildID)
                .addParameter("voice_channel_id", voiceChannelBind.getVoiceChannelID())
                .addParameter("role_id", voiceChannelBind.getRoleID())
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

}
