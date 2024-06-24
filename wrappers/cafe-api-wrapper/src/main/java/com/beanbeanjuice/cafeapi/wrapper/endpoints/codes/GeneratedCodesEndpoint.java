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

    public CompletableFuture<Boolean> updateUserGeneratedCode(final String userID, final String newCode) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/codes/" + userID)
                .addParameter("generated_code", newCode)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((request) -> request.getStatusCode() == 200);
    }

    public CompletableFuture<Boolean> createUserGeneratedCode(final String userID, final String newCode) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/codes/" + userID)
                .addParameter("generated_code", newCode)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> deleteUserGeneratedCode(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/codes/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((request) -> request.getStatusCode() == 200);
    }

}
