package com.beanbeanjuice.cafebot.api.wrapper.api.discord.generic;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.Greeting;
import org.apache.hc.core5.http.Method;

import java.util.concurrent.CompletableFuture;

public class GreetingApi extends Api {

    public GreetingApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    /**
     * Used for testing API connection only.
     * @return A {@link String} containing "Hello, world!"
     */
    public CompletableFuture<Greeting> getHello() {
        return makeGreetingRequest("/api/v4/hello");
    }

    /**
     * Used for testing API connection only.
     * @param name A {@link String} with a personalized name.
     * @return A {@link String} containing "Hello, {name}!"
     */
    public CompletableFuture<Greeting> getHello(String name) {
        return makeGreetingRequest(String.format("/api/v4/hello?name=%s", name));
    }

    /**
     * Used for testing API connection only.
     * @return A {@link String} containing "Hello, protected!"
     */
    public CompletableFuture<Greeting> getProtectedHello() {
        return makeGreetingRequest("/api/v4/hello/protected");
    }

    /**
     * Used for testing API connection only.
     * @return A {@link String} containing "Hello, admin!"
     */
    public CompletableFuture<Greeting> getAdminHello() {
        return makeGreetingRequest("/api/v4/hello/admin");
    }

    private CompletableFuture<Greeting> makeGreetingRequest(String route) {
        try {
            return RequestBuilder.builder()
                    .baseUrl(baseUrl)
                    .token(token)
                    .method(Method.GET)
                    .route(route)
                    .queue()
                    .thenApply(this::parseGreetingResponse);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private Greeting parseGreetingResponse(BasicResponse basicResponse) {
        return new Greeting(basicResponse.getBody().get("message").asString());
    }

}