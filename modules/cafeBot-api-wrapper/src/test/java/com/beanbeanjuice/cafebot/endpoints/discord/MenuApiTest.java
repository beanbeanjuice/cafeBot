package com.beanbeanjuice.cafebot.endpoints.discord;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.type.MenuItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class MenuApiTest extends ApiTest {

    @Test
    @DisplayName("can get all menu items")
    public void getAllMenuItems() throws ExecutionException, InterruptedException {
        MenuItem[] items = cafeAPI.getMenuApi().getMenuItems().get();

        Assertions.assertTrue(items.length > 0);
    }

    @Test
    @DisplayName("can get specific menu items")
    public void getMenuItem() throws ExecutionException, InterruptedException {
        MenuItem item = cafeAPI.getMenuApi().getMenuItem(1).get();

        Assertions.assertEquals("Normal Pancakes", item.getName());
    }

}
