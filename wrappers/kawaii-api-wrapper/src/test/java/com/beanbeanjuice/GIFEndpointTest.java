package com.beanbeanjuice;

import com.beanbeanjuice.kawaiiapi.wrapper.KawaiiAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class GIFEndpointTest {

    @Test
    @DisplayName("Test GIF Endpoint")
    public void testGIFEndpoint() throws ExecutionException, InterruptedException {
        KawaiiAPI kawaiiAPI = new KawaiiAPI("anonymous");

        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").get().isPresent());

        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").get().get().startsWith("https://api.kawaii.red/gif/hug/"));
        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("bruh").get().isEmpty());
    }

    @Test
    @DisplayName("GIF Endpoint Asynchronous Test")
    public void testGIFEndpointAsync() throws InterruptedException {
        KawaiiAPI kawaiiAPI = new KawaiiAPI("anonymous");

        ArrayList<String> links = new ArrayList<>();
        Consumer<Optional<String>> addToLinks = (stringOptional) -> stringOptional.ifPresent(links::add);

        kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);
        kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);
        kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);
        kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);
        kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);
        kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);

        Thread.sleep(3000);

        Assertions.assertEquals(6, links.size());
    }

    @Test
    @DisplayName("GIF Endpoint Synchronously Test")
    public void testGIFEndpointSync() throws InterruptedException, ExecutionException {
        KawaiiAPI kawaiiAPI = new KawaiiAPI("anonymous");

        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").get().isPresent());
        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").get().isPresent());
        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").get().isPresent());
        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").get().isPresent());
        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").get().isPresent());
        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").get().isPresent());
    }

}
