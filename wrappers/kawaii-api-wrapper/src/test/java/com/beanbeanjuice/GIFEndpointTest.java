package com.beanbeanjuice;

import com.beanbeanjuice.kawaiiapi.wrapper.KawaiiAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Consumer;

public class GIFEndpointTest {

    @Test
    @DisplayName("Test GIF Endpoint")
    public void testGIFEndpoint() {
        KawaiiAPI kawaiiAPI = new KawaiiAPI("anonymous");

        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").isPresent());

        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("hug").get().startsWith("https://api.kawaii.red/gif/hug/"));
        Assertions.assertTrue(kawaiiAPI.getGifEndpoint().getGIF("bruh").isEmpty());
    }

    @Test
    @DisplayName("GIF Endpoint Asynchronous Test")
    public void testGIFEndpointAsync() throws InterruptedException {
        KawaiiAPI kawaiiAPI = new KawaiiAPI("anonymous");

        Consumer<Optional<String>> print = (stringOptional) -> stringOptional.ifPresent(System.out::println);
        Consumer<Exception> printError = (exception) -> System.out.println(exception.getMessage());

        System.out.println("Running.");
        kawaiiAPI.getGifEndpoint().getGIFAsync("hug", print, printError);
        kawaiiAPI.getGifEndpoint().getGIFAsync("hug", print, printError);
        kawaiiAPI.getGifEndpoint().getGIFAsync("hug", print, printError);
        kawaiiAPI.getGifEndpoint().getGIFAsync("hug", print, printError);
        kawaiiAPI.getGifEndpoint().getGIFAsync("hug", print, printError);
        kawaiiAPI.getGifEndpoint().getGIFAsync("hug", print, printError);
        System.out.println("Finished running.");

        Thread.sleep(3000);
    }

    @Test
    @DisplayName("GIF Endpoint Synchronously Test")
    public void testGIFEndpointSync() {
        KawaiiAPI kawaiiAPI = new KawaiiAPI("anonymous");

        System.out.println("Running.");
        kawaiiAPI.getGifEndpoint().getGIF("hug").ifPresent(System.out::println);
        kawaiiAPI.getGifEndpoint().getGIF("hug").ifPresent(System.out::println);
        kawaiiAPI.getGifEndpoint().getGIF("hug").ifPresent(System.out::println);
        kawaiiAPI.getGifEndpoint().getGIF("hug").ifPresent(System.out::println);
        kawaiiAPI.getGifEndpoint().getGIF("hug").ifPresent(System.out::println);
        kawaiiAPI.getGifEndpoint().getGIF("hug").ifPresent(System.out::println);
        System.out.println("Finished running.");

    }

}
