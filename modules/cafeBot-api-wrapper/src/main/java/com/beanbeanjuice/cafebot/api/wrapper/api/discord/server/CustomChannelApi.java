package com.beanbeanjuice.cafebot.api.wrapper.api.discord.server;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomChannel;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CustomChannelApi extends Api {

    public CustomChannelApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<Map<String, CustomChannel>> getCustomChannels(CustomChannelType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/channels/type/%s", type))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("customChannels"))
                    .thenApply((channelsNode) -> {
                        Map<String, CustomChannel> channels = new HashMap<>();

                        for (JsonNode channelNode : channelsNode) {
                            CustomChannel channel = parseCustomChannel(channelNode);
                            channels.put(channel.getGuildId(), channel);
                        }

                        return channels;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Map<CustomChannelType, CustomChannel>> getCustomChannels(String guildId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/channels/%s", guildId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("customChannels"))
                    .thenApply((channelsNode) -> {
                        HashMap<CustomChannelType, CustomChannel> channels = new HashMap<>();

                        for (JsonNode channelNode : channelsNode) {
                            CustomChannel channel = parseCustomChannel(channelNode);
                            channels.put(channel.getType(), channel);
                        }

                        return channels;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<CustomChannel> getCustomChannel(String guildId, CustomChannelType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/channels/%s/%s", guildId, type))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("customChannel"))
                    .thenApply(this::parseCustomChannel);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<CustomChannel> setCustomChannel(String guildId, CustomChannelType type, String channelId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/channels/%s/%s", guildId, type))
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("customChannel"))
                    .thenApply(this::parseCustomChannel);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Void> deleteCustomChannel(String guildId, CustomChannelType type) {
        try {
            return RequestBuilder.builder()
                    .method(Method.DELETE)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/channels/%s/%s", guildId, type))
                    .queue()
                    .thenApply((res) -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private CustomChannel parseCustomChannel(JsonNode node) {
        String guildId = node.get("guildId").asString();
        CustomChannelType type = CustomChannelType.valueOf(node.get("type").asString());
        String channelId = node.get("channelId").asString();

        return new CustomChannel(guildId, type, channelId);
    }

}
