package com.beanbeanjuice.cafebot.api.wrapper.api.discord.user;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.type.ServeWord;
import org.apache.hc.core5.http.Method;

import java.util.concurrent.CompletableFuture;

public class ServeWordsApi extends Api {

    public ServeWordsApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    /**
     * Serve a word in the API.
     * @param userId The {@link String id} of the user who served the word.
     * @param word The {@link String} word to serve.
     * @return The newly served {@link ServeWord word}.
     */
    public CompletableFuture<ServeWord> serveWord(String userId, String word) {
        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/words/%s?word=%s", userId, word))
                    .queue()
                    .thenApply((response) -> {
                        int uses = response.getBody().get("word").get("uses").asInt();
                        float reward = response.getBody().get("reward").asFloat();
                        float newBalance = response.getBody().get("user").get("balance").asFloat();

                        return new ServeWord(uses, reward, newBalance);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }
}
