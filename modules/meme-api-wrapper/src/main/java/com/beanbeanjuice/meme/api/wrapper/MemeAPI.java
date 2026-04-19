package com.beanbeanjuice.meme.api.wrapper;

import com.beanbeanjuice.meme.api.wrapper.objects.RedditMeme;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.concurrent.CompletableFuture;

public class MemeAPI {

    private MemeAPI() { } // Should not be able to instantiate.

    // Only one reusable client should exist. Good practice for ASYNC clients.
    private static final CloseableHttpAsyncClient CLIENT = HttpAsyncClients.createDefault();

    static {
        CLIENT.start();
    }

    private static final JsonMapper MAPPER = new JsonMapper();

    public static CompletableFuture<RedditMeme> get(String subreddit) {
        CompletableFuture<RedditMeme> future = new CompletableFuture<>();

        SimpleHttpRequest request = SimpleRequestBuilder
                .get("https://meme-api.com/gimme/" + subreddit)
                .build();

        CLIENT.execute(request, new FutureCallback<>() {

            @Override
            public void completed(SimpleHttpResponse response) {
                try {
                    int statusCode = response.getCode();
                    String body = response.getBodyText();

                    if (statusCode < 200 || statusCode >= 300) {
                        future.completeExceptionally(
                                new IllegalStateException("Request failed: " + statusCode)
                        );
                        return;
                    }

                    JsonNode jsonNode = MAPPER.readTree(body);

                    RedditMeme meme = new RedditMeme(
                            jsonNode.get("title").asString(),
                            jsonNode.get("subreddit").asString(),
                            jsonNode.get("postLink").asString(),
                            jsonNode.get("url").asString(),
                            jsonNode.get("nsfw").asBoolean(),
                            jsonNode.get("author").asString()
                    );

                    future.complete(meme);

                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void failed(Exception ex) {
                future.completeExceptionally(ex);
            }

            @Override
            public void cancelled() {
                future.cancel(true);
            }
        });

        return future;
    }

}
