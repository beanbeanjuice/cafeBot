package com.beanbeanjuice.cafebot.api.wrapper.api.discord.server;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.VoiceRole;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VoiceRoleApi extends Api {

    public VoiceRoleApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<List<VoiceRole>> getVoiceRoles(String guildId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/voice_roles/%s", guildId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("voiceRoles"))
                    .thenApply((nodes) -> {
                        ArrayList<VoiceRole> voiceRoles = new ArrayList<>();

                        for (JsonNode node : nodes) {
                            voiceRoles.add(parseVoiceRole(node));
                        }

                        return voiceRoles;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<VoiceRole> createVoiceRole(String guildId, String channelId, String roleId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("roleId", roleId);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/voice_roles/%s", guildId))
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("voiceRole"))
                    .thenApply(this::parseVoiceRole);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Void> deleteVoiceRole(String guildId, String channelId, String roleId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("roleId", roleId);

        try {
            return RequestBuilder.builder()
                    .method(Method.DELETE)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/voice_roles/%s", guildId))
                    .body(map)
                    .queue()
                    .thenApply(node -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private VoiceRole parseVoiceRole(JsonNode node) {
        String guildId = node.get("guildId").asString();
        String roleId = node.get("roleId").asString();
        String channelId = node.get("channelId").asString();

        return new VoiceRole(guildId, roleId, channelId);
    }

}
