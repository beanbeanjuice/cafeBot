package com.beanbeanjuice.cafebot.api.wrapper.api.discord.user;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.DiscordUser;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;

public class UserApi extends Api {

    public UserApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<DiscordUser> getUser(String id) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/user/%s", id))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("user"))
                    .thenApply(this::parseUser);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private DiscordUser parseUser(JsonNode node) {
        String id = node.get("id").asString();
        float balance = node.get("balance").asFloat();
        String lastServeTime = node.get("lastServeTime").isNull() ? null : node.get("lastServeTime").asString();
        String lastDonationTime = node.get("lastDonationTime").isNull() ? null : node.get("lastDonationTime").asString();

        return new DiscordUser(id, balance, lastServeTime, lastDonationTime);
    }

}
