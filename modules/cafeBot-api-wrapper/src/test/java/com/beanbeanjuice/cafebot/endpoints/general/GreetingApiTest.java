package com.beanbeanjuice.cafebot.endpoints.general;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.CafeAPI;
import com.beanbeanjuice.cafebot.api.wrapper.type.Greeting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GreetingApiTest extends ApiTest {

    @Test
    @DisplayName("can get hello response from server")
    public void testGreetingApi() throws ExecutionException, InterruptedException {
        Greeting greeting = cafeAPI.getGreetingApi().getHello().get();
        Assertions.assertEquals("Hello, world!", greeting.getMessage());
    }

    @Test
    @DisplayName("can get hello wth name response from server")
    public void testGreetingWithNameApi() throws ExecutionException, InterruptedException {
        Greeting greeting = cafeAPI.getGreetingApi().getHello("William").get();
        Assertions.assertEquals("Hello, William!", greeting.getMessage());
    }

    @Test
    @DisplayName("can get protected greeting when authenticated")
    public void testAuthenticatedProtectedGreeting() throws ExecutionException, InterruptedException {
        Greeting greeting = cafeAPI.getGreetingApi().getProtectedHello().get();
        Assertions.assertEquals("Hello, protected!", greeting.getMessage());
    }

    @Test
    @DisplayName("should not get protected greeting when not authenticated")
    public void testUnauthenticatedProtectedGreeting() {
        CafeAPI unauthenticatedApi = new CafeAPI(dotenv.get("CAFEBOT_API_URL"), "");
        CompletableFuture<Greeting> future = unauthenticatedApi.getGreetingApi().getProtectedHello();
        Assertions.assertThrows(ExecutionException.class, future::get);
    }

    @Test
    @DisplayName("can get admin greeting when authenticated")
    public void testAuthenticatedAdminGreeting() throws ExecutionException, InterruptedException {
        Greeting greeting = cafeAPI.getGreetingApi().getAdminHello().get();
        Assertions.assertEquals("Hello, admin!", greeting.getMessage());
    }

    @Test
    @DisplayName("should not get protected greeting when not authenticated")
    public void testUnauthenticatedAdminGreeting() {
        CafeAPI unauthenticatedApi = new CafeAPI(dotenv.get("CAFEBOT_API_URL"), "");
        CompletableFuture<Greeting> future = unauthenticatedApi.getGreetingApi().getAdminHello();
        Assertions.assertThrows(ExecutionException.class, future::get);
    }

    @Test
    @DisplayName("runs multiple greetings concurrently and duration ≈ longest individual")
    public void testConcurrentGreetingsAsyncTiming() throws Exception {
        long start = System.currentTimeMillis();

        // These will hold the completion times
        final long[] helloTime = new long[1];
        final long[] helloNameTime = new long[1];
        final long[] protectedTime = new long[1];
        final long[] adminTime = new long[1];

        CompletableFuture<Greeting> f1 = cafeAPI.getGreetingApi().getHello()
                .thenApply(g -> {
                    helloTime[0] = System.currentTimeMillis() - start;
                    return g;
                });

        CompletableFuture<Greeting> f2 = cafeAPI.getGreetingApi().getHello("William")
                .thenApply(g -> {
                    helloNameTime[0] = System.currentTimeMillis() - start;
                    return g;
                });

        CompletableFuture<Greeting> f3 = cafeAPI.getGreetingApi().getProtectedHello()
                .thenApply(g -> {
                    protectedTime[0] = System.currentTimeMillis() - start;
                    return g;
                });

        CompletableFuture<Greeting> f4 = cafeAPI.getGreetingApi().getAdminHello()
                .thenApply(g -> {
                    adminTime[0] = System.currentTimeMillis() - start;
                    return g;
                });

        // Wait for all to finish
        CompletableFuture.allOf(f1, f2, f3, f4).join();
        long totalDuration = System.currentTimeMillis() - start;

        System.out.printf("Completion times (ms): hello=%d, name=%d, protected=%d, admin=%d%n",
                helloTime[0], helloNameTime[0], protectedTime[0], adminTime[0]);
        System.out.println("Total concurrent duration: " + totalDuration + " ms");

        // Check results
        Assertions.assertEquals("Hello, world!", f1.get().getMessage());
        Assertions.assertEquals("Hello, William!", f2.get().getMessage());
        Assertions.assertEquals("Hello, protected!", f3.get().getMessage());
        Assertions.assertEquals("Hello, admin!", f4.get().getMessage());

        // The total duration should be approximately the max of individual completions
        long longest = Math.max(
                Math.max(helloTime[0], helloNameTime[0]),
                Math.max(protectedTime[0], adminTime[0])
        );

        long tolerance = 100; // small buffer for scheduling
        Assertions.assertTrue(totalDuration <= longest + tolerance,
                String.format("Expected total duration ≈ longest individual (%d ms) ±%d, got %d ms",
                        longest, tolerance, totalDuration));
    }

}
