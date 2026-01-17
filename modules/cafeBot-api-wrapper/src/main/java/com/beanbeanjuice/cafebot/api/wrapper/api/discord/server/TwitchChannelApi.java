package com.beanbeanjuice.cafebot.api.wrapper.api.discord.server;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class TwitchChannelApi extends Api {

    public TwitchChannelApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<ArrayList<String>> getGuilds(String twitchUsername) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/twitch/channel/%s", twitchUsername))
                    .queue()
                    .thenApply((basicResponse) -> {
                        JsonNode channelsNode = basicResponse.getBody().get("guildIds");

                        ArrayList<String> guildIds = new ArrayList<>();

                        for (JsonNode node : channelsNode) {
                            guildIds.add(node.asString());
                        }

                        return guildIds;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    /**
     * Gets the mapping of Twitch channels to guilds.
     *
     * @return a {@link java.util.Map} where:
     *         <ul>
     *             <li>the key is the Twitch channel name.</li>
     *             <li>the value is an {@link ArrayList} of guild names the channel is in.</li>
     *         </ul>
     */
    public CompletableFuture<HashMap<String, ArrayList<String>>> getChannels() {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/servers/twitch")
                    .queue()
                    .thenApply((basicResponse) -> {
                        JsonNode channelsNode = basicResponse.getBody().get("twitchChannels");

                        HashMap<String, ArrayList<String>> channels = new HashMap<>(); // channelName, list of guilds

                        for (JsonNode channelNode : channelsNode) {
                            String channelName = channelNode.get("channel").asString();

                            channels.putIfAbsent(channelName, new ArrayList<>());

                            for (JsonNode guildNode : channelNode.get("guilds")) {
                                channels.get(channelName).add(guildNode.asString());
                            }
                        }

                        return channels;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<ArrayList<String>> getChannels(String guildId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/twitch/%s", guildId))
                    .queue()
                    .thenApply(this::parseChannelsForGuild);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<ArrayList<String>> addChannel(String guildId, String twitchUsername) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channel", twitchUsername);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/twitch/%s", guildId))
                    .body(map)
                    .queue()
                    .thenApply(this::parseChannelsForGuild);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Void> deleteChannel(String guildId, String twitchUsername) {
        try {
            return RequestBuilder.builder()
                    .method(Method.DELETE)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/twitch/%s/%s", guildId, twitchUsername))
                    .queue()
                    .thenApply((basicResponse) -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private ArrayList<String> parseChannelsForGuild(BasicResponse response) {
        JsonNode channelsNode = response.getBody().get("twitchChannels");

        ArrayList<String> channels = new ArrayList<>();

        for (JsonNode channelNode : channelsNode) {
            channels.add(channelNode.asString());
        }

        return channels;
    }

}
