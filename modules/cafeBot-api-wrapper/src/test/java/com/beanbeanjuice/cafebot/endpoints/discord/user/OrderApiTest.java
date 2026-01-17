package com.beanbeanjuice.cafebot.endpoints.discord.user;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.MenuCategory;
import com.beanbeanjuice.cafebot.api.wrapper.type.MenuOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class OrderApiTest extends ApiTest {

    private String user1;
    private String user2;

    @BeforeEach
    public void setUp() throws InterruptedException, ExecutionException {
        this.user1 = generateSnowflake().toString();
        this.user2 = generateSnowflake().toString();

        cafeAPI.getOrderApi().createOrder(this.user1, this.user2, 1).get(); // Breakfast
        cafeAPI.getOrderApi().createOrder(this.user1, this.user2, 17).get(); // Sandwich

        cafeAPI.getOrderApi().createOrder(this.user2, this.user1, 1).get();
        cafeAPI.getOrderApi().createOrder(this.user2, this.user1, 1).get();
        cafeAPI.getOrderApi().createOrder(this.user2, this.user1, 17).get();
        cafeAPI.getOrderApi().createOrder(this.user2, this.user1, 17).get();
        cafeAPI.getOrderApi().createOrder(this.user2, this.user1, 17).get();
    }

    @Test
    @DisplayName("can get all orders for a user")
    public void canGetAllOrdersForUser() throws ExecutionException, InterruptedException {
        MenuOrder[] orders = cafeAPI.getOrderApi().getOrders(user1).get();

        Assertions.assertEquals(7, orders.length);
    }

    @Test
    @DisplayName("can get all orders sent for a user")
    public void canGetAllOrdersSentForUser() throws ExecutionException, InterruptedException {
        MenuOrder[] orders = cafeAPI.getOrderApi().getOrdersSent(user1).get();

        Assertions.assertEquals(2, orders.length);
    }

    @Test
    @DisplayName("can get all orders sent for a user of a specific type")
    public void canGetAllOrdersSentForUserOfSpecificType() throws ExecutionException, InterruptedException {
        MenuOrder[] orders = cafeAPI.getOrderApi().getOrdersSent(user1, MenuCategory.SANDWICH).get();

        Assertions.assertEquals(1, orders.length);
    }

    @Test
    @DisplayName("can get all orders received for a user")
    public void canGetAllOrdersReceivedForUser() throws ExecutionException, InterruptedException {
        MenuOrder[] orders = cafeAPI.getOrderApi().getOrdersReceived(user1).get();

        Assertions.assertEquals(5, orders.length);
    }

    @Test
    @DisplayName("can get all orders received for a user of a specific type")
    public void canGetAllOrdersReceivedForUserOfSpecificType() throws ExecutionException, InterruptedException {
        MenuOrder[] orders = cafeAPI.getOrderApi().getOrdersReceived(user1, MenuCategory.SANDWICH).get();

        Assertions.assertEquals(3, orders.length);
    }

    @Test
    @DisplayName("can create an order")
    public void canCreateAnOrder() throws ExecutionException, InterruptedException {
        String user1 = generateSnowflake().toString();
        String user2 = generateSnowflake().toString();
        int itemId = 1;

        MenuOrder order = cafeAPI.getOrderApi().createOrder(user1, user2, itemId).get();

        Assertions.assertNotNull(order);
    }

}
