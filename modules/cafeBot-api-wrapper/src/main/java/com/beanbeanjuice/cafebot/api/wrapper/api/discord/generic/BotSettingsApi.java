package com.beanbeanjuice.cafebot.api.wrapper.api.discord.generic;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import org.apache.hc.core5.http.Method;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BotSettingsApi extends Api {

    public BotSettingsApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<String> getBotVersion() {
        try {
            return RequestBuilder.builder()
                    .baseUrl(baseUrl)
                    .token(token)
                    .method(Method.GET)
                    .route("/api/v4/bot/settings/version")
                    .queue()
                    .thenApply(basicResponse -> basicResponse.getBody().get("setting").get("value").asString());
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<String> updateBotVersion(String newVersion) {
        Map<String, String> body = new HashMap<>();
        body.put("value", newVersion);

        try {
            return RequestBuilder.builder()
                    .baseUrl(baseUrl)
                    .token(token)
                    .method(Method.PUT)
                    .route("/api/v4/bot/settings/version")
                    .body(body)
                    .queue()
                    .thenApply((response) -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

}
