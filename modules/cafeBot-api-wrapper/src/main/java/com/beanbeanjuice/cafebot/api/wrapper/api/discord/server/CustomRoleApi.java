package com.beanbeanjuice.cafebot.api.wrapper.api.discord.server;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomRoleType;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomRole;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CustomRoleApi extends Api {

    public CustomRoleApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<Map<CustomRoleType, CustomRole>> getCustomRoles(String guildId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/roles/%s", guildId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("customRoles"))
                    .thenApply((rolesNode) -> {
                        HashMap<CustomRoleType, CustomRole> roles = new HashMap<>();

                        for (JsonNode roleNode : rolesNode) {
                            CustomRole role = parseCustomRole(roleNode);
                            roles.put(role.getType(), role);
                        }

                        return roles;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<CustomRole> getCustomRole(String guildId, CustomRoleType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/roles/%s/%s", guildId, type))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("customRole"))
                    .thenApply(this::parseCustomRole);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<CustomRole> setCustomRole(String guildId, CustomRoleType type, String roleId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("roleId", roleId);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/roles/%s/%s", guildId, type))
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("customRole"))
                    .thenApply(this::parseCustomRole);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Void> deleteCustomRole(String guildId, CustomRoleType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.DELETE)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/roles/%s/%s", guildId, type))
                    .queue()
                    .thenApply((res) -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private CustomRole parseCustomRole(JsonNode node) {
        String guildId = node.get("guildId").asString();
        CustomRoleType type = CustomRoleType.valueOf(node.get("type").asString());
        String roleId = node.get("roleId").asString();

        return new CustomRole(guildId, type, roleId);
    }

}
