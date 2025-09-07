package com.beanbeanjuice.cafebot.api.wrapper.api.discord;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.MenuCategory;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.MenuItem;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MenuApi extends Api {

    public MenuApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<MenuItem[]> getMenuItems() {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/menu")
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("menu"))
                    .thenApply((itemsNode) -> {
                        List<MenuItem> menuItems = new ArrayList<>();

                        for (JsonNode menuNode : itemsNode) {
                            menuItems.add(parseMenuItem(menuNode));
                        }

                        return menuItems.toArray(new MenuItem[0]);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<MenuItem> getMenuItem(int id) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/menu/%s", id))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("item"))
                    .thenApply(this::parseMenuItem);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private MenuItem parseMenuItem(JsonNode node) {
        int id = node.get("id").asInt();
        MenuCategory category = MenuCategory.valueOf(node.get("category").asString());
        String name = node.get("name").asString();
        String description = node.get("description").asString();
        float price = node.get("price").asFloat();
        String imageUrl = node.get("imageUrl").asString();

        return new MenuItem(id, category, name, description, price, imageUrl);
    }

}
