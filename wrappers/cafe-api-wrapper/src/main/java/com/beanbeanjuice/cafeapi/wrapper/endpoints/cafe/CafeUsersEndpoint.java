package com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class CafeUsersEndpoint extends CafeEndpoint {

    public CompletableFuture<ArrayList<CafeUser>> getAllCafeUsers() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/cafe/users")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    ArrayList<CafeUser> cafeUsers = new ArrayList<>();
                    request.getData().get("users").forEach((user) -> cafeUsers.add(parseCafeUser(user)));
                    return cafeUsers;
                });
    }

    public CompletableFuture<CafeUser> getCafeUser(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/cafe/users/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> parseCafeUser(request.getData().get("cafe_user")));
    }

    public CompletableFuture<Boolean> updateCafeUser(final String userID, final CafeType type, @Nullable final Object value)
    throws TeaPotException {

        if (!(type.equals(CafeType.LAST_SERVING_TIME) && value == null)) {
            switch (type) {
                case BEAN_COINS -> {
                    if (!(value instanceof Double)) throw new TeaPotException("Type Specified Must be a Double");
                }

                case LAST_SERVING_TIME -> {
                    if (!(value instanceof Timestamp)) throw new TeaPotException("Type Specified Must be a Timestamp");
                }

                case ORDERS_BOUGHT, ORDERS_RECEIVED -> {
                    if (!(value instanceof Integer)) throw new TeaPotException("Type Specified Must be an Integer");
                }
            }
        }

        RequestBuilder requestBuilder = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/cafe/users/" + userID)
                .addParameter("type", type.getType())
                .setAuthorization(apiKey);

        if (value == null) requestBuilder.addParameter("value", "null");
        else requestBuilder.addParameter("value", value.toString());

        return requestBuilder
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    public CompletableFuture<Boolean> createCafeUser(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/cafe/users/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<Boolean> deleteCafeUser(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/cafe/users/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

    private CafeUser parseCafeUser(final JsonNode node) {
        String userID = node.get("user_id").asText();
        double beanCoins = node.get("bean_coins").asDouble();
        Timestamp timestamp = CafeGeneric.parseTimestampFromAPI(node.get("last_serving_time").asText()).orElse(null);
        int ordersBought = node.get("orders_bought").asInt();
        int ordersReceived = node.get("orders_received").asInt();

        return new CafeUser(userID, beanCoins, timestamp, ordersBought, ordersReceived);
    }

}
