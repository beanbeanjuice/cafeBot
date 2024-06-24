package com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class GuildsEndpoint extends CafeEndpoint {

    public CompletableFuture<HashMap<String, GuildInformation>> getAllGuildInformation() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/guilds")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((request) -> {
                    HashMap<String, GuildInformation> guilds = new HashMap<>();

                    for (JsonNode guild : request.getData().get("guilds")) {
                        String guildID = guild.get("guild_id").asText();
                        guilds.put(guildID, parseGuildInformation(guild));
                    }

                    return guilds;
                });
    }

    public CompletableFuture<GuildInformation> getGuildInformation(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/guilds/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((request) -> parseGuildInformation(request.getData().get("guild")));
    }

    public CompletableFuture<Boolean> createGuildInformation(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/guilds/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> updateGuildInformation(final String guildID, final GuildInformationType type, Object value) throws TeaPotException {
        switch (type){
            case PREFIX, MODERATOR_ROLE_ID, TWITCH_CHANNEL_ID,
                 MUTED_ROLE_ID, LIVE_NOTIFICATIONS_ROLE_ID, UPDATE_CHANNEL_ID,
                 COUNTING_CHANNEL_ID, POLL_CHANNEL_ID, RAFFLE_CHANNEL_ID,
                 BIRTHDAY_CHANNEL_ID, WELCOME_CHANNEL_ID, LOG_CHANNEL_ID,
                 VENTING_CHANNEL_ID, DAILY_CHANNEL_ID -> {
                if (!value.getClass().equals(String.class)) throw new TeaPotException(value + " is invalid for " + type);
            }

            case NOTIFY_ON_UPDATE, AI_RESPONSE -> {
                if (!value.getClass().equals(Boolean.class)) throw new TeaPotException(value + " is invalid for " + type);
                value = ((Boolean) value) ? "1" : "0";
            }

        }

        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/guilds/" + guildID)
                .addParameter("type", type.getType())
                .addParameter("value", value.toString())
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((request) -> request.getStatusCode() == 200);
    }

    public CompletableFuture<Boolean> deleteGuildInformation(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/guilds/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((request) -> request.getStatusCode() == 200);
    }

    private GuildInformation parseGuildInformation(final JsonNode guild) {
        HashMap<GuildInformationType, String> guildSettings = new HashMap<>();

        Arrays.stream(GuildInformationType.values()).forEach((type) -> {
            if (type == GuildInformationType.AI_RESPONSE || type == GuildInformationType.NOTIFY_ON_UPDATE) {
                guildSettings.put(type, convertToBooleanString(guild.get(type.getType()).asText()));
                return;
            }

            guildSettings.put(type, guild.get(type.getType()).asText());
        });

        return new GuildInformation(guildSettings);
    }

    private String convertToBooleanString(final String string) {
        if (string.equalsIgnoreCase("true") || string.equalsIgnoreCase("1")) return "true";
        else return "false";
    }

}
