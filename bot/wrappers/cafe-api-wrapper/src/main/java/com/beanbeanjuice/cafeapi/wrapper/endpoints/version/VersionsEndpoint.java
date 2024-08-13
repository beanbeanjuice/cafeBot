package com.beanbeanjuice.cafeapi.wrapper.endpoints.version;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.TeaPotException;

import java.util.concurrent.CompletableFuture;

public class VersionsEndpoint extends CafeEndpoint {

    public CompletableFuture<String> getCurrentCafeBotVersion() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/cafeBot")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getData().get("bot_information").get("version").asText());
    }

    public CompletableFuture<Boolean> updateCurrentCafeBotVersion(final String versionNumber) throws TeaPotException {
        if (!versionNumber.startsWith("v")) throw new TeaPotException("Version Number Must Start with 'v'.");

        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/cafeBot")
                .addParameter("version", versionNumber)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

}
