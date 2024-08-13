package com.beanbeanjuice;

import com.beanbeanjuice.kawaiiapi.wrapper.KawaiiAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
    public void testGIFEndpointAsync() {
        KawaiiAPI kawaiiAPI = new KawaiiAPI("anonymous");

        ArrayList<String> links = new ArrayList<>();
        Consumer<Optional<String>> addToLinks = (stringOptional) -> stringOptional.ifPresent(links::add);

        CompletableFuture<Void> hugFuture1 = kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);
        CompletableFuture<Void> hugFuture2 = kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);
        CompletableFuture<Void> hugFuture3 = kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);
        CompletableFuture<Void> hugFuture4 = kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);
        CompletableFuture<Void> hugFuture5 = kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);
        CompletableFuture<Void> hugFuture6 = kawaiiAPI.getGifEndpoint().getGIF("hug").thenAcceptAsync(addToLinks);

        hugFuture1.join();
        hugFuture2.join();
        hugFuture3.join();
        hugFuture4.join();
        hugFuture5.join();
        hugFuture6.join();

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
