package com.beanbeanjuice.cafeapi.wrapper.endpoints.counting;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class CountingEndpoint extends CafeEndpoint {

    public CompletableFuture<HashMap<String, CountingInformation>> getAllCountingInformation() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/counting/guilds")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    HashMap<String, CountingInformation> guilds = new HashMap<>();

                    request.getData().get("guilds").forEach((guild) -> {
                        String guildID = guild.get("guild_id").asText();
                        guilds.put(guildID, parseCountingInformation(guild));
                    });

                    return guilds;
                });
    }

    public CompletableFuture<CountingInformation> getGuildCountingInformation(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/counting/guilds/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> parseCountingInformation(request.getData().get("counting_information")));
    }

    public CompletableFuture<Boolean> updateGuildCountingInformation(final String guildID, final int highestNumber, final int lastNumber,
                                                                    final String lastUserID, final String failureRoleID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/counting/guilds/" + guildID)
                .addParameter("highest_number", String.valueOf(highestNumber))
                .addParameter("last_number", String.valueOf(lastNumber))
                .addParameter("last_user_id", lastUserID)
                .addParameter("failure_role_id", failureRoleID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    public CompletableFuture<Boolean> updateGuildCountingInformation(final String guildID, final CountingInformation countingInformation) {
        return updateGuildCountingInformation(
                guildID,
                countingInformation.getHighestNumber(),
                countingInformation.getLastNumber(),
                countingInformation.getLastUserID(),
                countingInformation.getFailureRoleID()
        );
    }

    public CompletableFuture<Boolean> createGuildCountingInformation(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/counting/guilds/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> deleteGuildCountingInformation(final String guildID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/counting/guilds/" + guildID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    private CountingInformation parseCountingInformation(final JsonNode node) {
        int highestNumber = node.get("highest_number").asInt();
        int lastNumber = node.get("last_number").asInt();
        String lastUserID = node.get("last_user_id").asText();
        String failureRoleID = node.get("failure_role_id").asText();

        return new CountingInformation(
                highestNumber,
                lastNumber,
                lastUserID,
                failureRoleID
        );
    }

}
