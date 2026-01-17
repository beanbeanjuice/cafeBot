package com.beanbeanjuice.cafebot.api.wrapper.api.discord.server;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GuildFlag;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.DiscordServer;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GuildApi extends Api {

    public GuildApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<Map<String, DiscordServer>> getDiscordServers() {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/servers/guilds")
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("guilds"))
                    .thenApply((discordServersNode) -> {
                        Map<String, DiscordServer> discordServerMap = new HashMap<>();

                        for (JsonNode discordServerNode : discordServersNode) {
                            DiscordServer discordServer = parseGuild(discordServerNode);
                            discordServerMap.put(discordServer.getId(), discordServer);
                        }

                        return discordServerMap;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<DiscordServer> getDiscordServer(String id) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/guilds/%s", id))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("guild"))
                    .thenApply(this::parseGuild);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Map<GuildFlag, Boolean>> getDiscordServerFlags(String id) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/guilds/%s/flags", id))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("flags"))
                    .thenApply(this::parseFlags);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Void> deleteDiscordServer(String id) {
        try {
            return RequestBuilder.builder()
                    .method(Method.DELETE)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/guilds/%s", id))
                    .queue()
                    .thenApply(response -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<DiscordServer> updateDiscordServer(String id, DiscordServer discordServer) {
        Map<String, Object> map = new HashMap<>();
        map.put("aiEnabled", discordServer.isAiEnabled()); // ! - make sure you check the /ai command

        try {
            return RequestBuilder.builder()
                    .method(Method.PATCH)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/guilds/%s", id))
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("guild"))
                    .thenApply(this::parseGuild);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private Map<GuildFlag, Boolean> parseFlags(JsonNode node) {
        Map<GuildFlag, Boolean> flags = new HashMap<>();

        for (GuildFlag flag : GuildFlag.values()) flags.put(flag, false);

        for (var entry : node.properties()) {
            GuildFlag flag = GuildFlag.valueOf(entry.getKey());
            boolean value = entry.getValue().asBoolean();

            flags.put(flag, value);
        }

        return flags;
    }

    private DiscordServer parseGuild(JsonNode node) {
        String id = node.get("id").asString();
        float balance = node.get("balance").asFloat();
        boolean aiEnabled = node.get("aiEnabled").asBoolean();

        return new DiscordServer(id, balance, aiEnabled);
    }

}
