package com.beanbeanjuice.meme.api.wrapper;

import com.beanbeanjuice.meme.api.wrapper.objects.RedditMeme;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

public class MemeAPI {

    private MemeAPI() { } // Should not be able to instantiate.

    public static CompletableFuture<RedditMeme> get(String subreddit) {
        HttpHost host;

        try {
            host = HttpHost.create("https://meme-api.com");
        } catch (URISyntaxException e) {
            return CompletableFuture.failedFuture(e);
        }

        SimpleHttpRequest request = SimpleRequestBuilder
                .create(Method.GET)
                .setHttpHost(host)
                .setPath(String.format("/gimme/%s", subreddit))
                .build();

        CloseableHttpAsyncClient client = HttpAsyncClients.custom().build();
        client.start();

        JsonMapper mapper = new JsonMapper();
        CompletableFuture<RedditMeme> future = new CompletableFuture<>();

        client.execute(request, new FutureCallback<>() {

            @Override
            public void completed(SimpleHttpResponse response) {
                try {
                    int statusCode = response.getCode();
                    String body = response.getBodyText();

                    JsonNode jsonNode;

                    if (statusCode == 204 || body == null || body.isBlank()) {
                        // No content → represent as an empty object or null
                        jsonNode = mapper.createObjectNode(); // or null, depending on your design
                    } else {
                        jsonNode = mapper.readTree(body);
                    }

                    if (statusCode < 200 || statusCode >= 300) {
                        future.completeExceptionally(
                                new IllegalStateException(String.format("Request failed with status %d.", statusCode))
                        );
                    }

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
                } finally {
                    closeClient(client);
                }
            }

            @Override
            public void failed(Exception ex) {
                future.completeExceptionally(ex);
                closeClient(client);
            }

            @Override
            public void cancelled() {
                future.cancel(true);
                closeClient(client);
            }

        });

        return future;
    }

    private static void closeClient(CloseableHttpAsyncClient client) {
        try {
            client.close();
        } catch (Exception e) {
            // Log or ignore
        }
    }

}
