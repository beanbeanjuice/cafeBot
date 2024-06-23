package com.beanbeanjuice;

import com.beanbeanjuice.kawaiiapi.wrapper.KawaiiAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GIFEndpointTest {

    @Test
    @DisplayName("Test Gif Endpoint")
    public void testGifEndpoint() {
        KawaiiAPI kawaiiAPI = new KawaiiAPI("anonymous");

        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").isPresent());

        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").get().startsWith("https://api.kawaii.red/gif/hug/"));
        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("bruh").isEmpty());
    }

}
