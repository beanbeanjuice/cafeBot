package com.beanbeanjuice.cafeapi.wrapper.endpoints.codes;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class GeneratedCodesEndpoint extends CafeEndpoint {

    public CompletableFuture<HashMap<String, String>> getAllGeneratedCodes() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/codes")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    HashMap<String, String> codes = new HashMap<>();
                    request.getData().get("users").forEach((user) -> codes.put(user.get("user_id").asText(), user.get("generated_code").asText()));
                    return codes;
                });
    }

    public CompletableFuture<String> getUserGeneratedCode(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/codes/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getData().get("generated_code").asText());
    }

    public CompletableFuture<String> updateUserGeneratedCodeIfExists(final String userID) {
        return this.getUserGeneratedCode(userID)
                .thenComposeAsync(existingCode -> {
                    if (existingCode != null) return this.updateUserGeneratedCode(userID);
                    else return this.createUserGeneratedCode(userID);
                }).exceptionallyComposeAsync((e) -> this.createUserGeneratedCode(userID));
    }

    public CompletableFuture<String> updateUserGeneratedCode(final String userID) {
        String code = generateRandomCode();

        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/codes/" + userID)
                .addParameter("generated_code", code)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> code);
    }

    public CompletableFuture<String> createUserGeneratedCode(final String userID) {
        String code = generateRandomCode();
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/codes/" + userID)
                .addParameter("generated_code", code)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> code);
    }

    public CompletableFuture<Boolean> deleteUserGeneratedCode(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/codes/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    private String generateRandomCode() {
        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(32);

        for (int i = 0; i < 32; i++) {
            int index = (int) (validCharacters.length() * Math.random());
            sb.append(validCharacters.charAt(index));
        }

        return sb.toString();
    }

}
