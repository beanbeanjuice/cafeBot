package com.beanbeanjuice.cafebot.api.wrapper.api.discord.user;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.MenuCategory;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.MenuOrder;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class OrderApi extends Api {

    public OrderApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<MenuOrder[]> getOrders(String userId) {
        return getOrdersHelper(String.format("/api/v4/discord/users/orders/%s", userId));
    }

    public CompletableFuture<MenuOrder[]> getOrdersSent(String userId) {
        return getOrdersHelper(String.format("/api/v4/discord/users/orders/%s/sent", userId));
    }

    public CompletableFuture<MenuOrder[]> getOrdersSent(String userId, MenuCategory category) {
        return getOrdersHelper(String.format("/api/v4/discord/users/orders/%s/sent?category=%s", userId, category.toString()));
    }

    public CompletableFuture<MenuOrder[]> getOrdersReceived(String userId) {
        return getOrdersHelper(String.format("/api/v4/discord/users/orders/%s/received", userId));
    }

    public CompletableFuture<MenuOrder[]> getOrdersReceived(String userId, MenuCategory category) {
        return getOrdersHelper(String.format("/api/v4/discord/users/orders/%s/received?category=%s", userId, category.toString()));
    }

    private CompletableFuture<MenuOrder[]> getOrdersHelper(String route) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(route)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("orders"))
                    .thenApply((ordersNode) -> {
                        List<MenuOrder> menuOrders = new ArrayList<>();

                        for (JsonNode orderNode : ordersNode) {
                            menuOrders.add(parseMenuOrder(orderNode));
                        }

                        return menuOrders.toArray(new MenuOrder[0]);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<MenuOrder> createOrder(String from, String to, int itemId) {
        Map<String, Object> map = new HashMap<>();
        map.put("from", from);
        map.put("to", to);
        map.put("itemId", itemId);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/users/orders")
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("order"))
                    .thenApply(this::parseMenuOrder);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private MenuOrder parseMenuOrder(JsonNode node) {
        int id = node.get("id").asInt();
        int itemId = node.get("itemId").asInt();
        String fromId = node.get("fromId").asString();
        String toId = node.get("toId").asString();
        String createdAt = node.get("createdAt").asString();

        return new MenuOrder(id, itemId, fromId, toId, createdAt);
    }

}
