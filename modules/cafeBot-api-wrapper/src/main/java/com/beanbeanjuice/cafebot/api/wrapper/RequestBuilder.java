package com.beanbeanjuice.cafebot.api.wrapper;

import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import org.apache.hc.client5.http.async.methods.*;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

public class RequestBuilder {

    private String baseUrl;
    private String token;
    private String route;
    private Method method;
    private Object body;

    private static final CloseableHttpAsyncClient CLIENT;

    static {
        CLIENT = HttpAsyncClients.custom().build();
        CLIENT.start();
    }

    public static void shutdown() {
        try {
            CLIENT.close();
        } catch (Exception e) {
            // log if needed
        }
    }

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    public RequestBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public RequestBuilder token(String token) {
        this.token = token;
        return this;
    }

    public RequestBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public RequestBuilder route(String route) {
        this.route = route.replace(" ", "%20");
        return this;
    }

    public RequestBuilder body(Object body) {
        this.body = body;
        return this;
    }

    public CompletableFuture<BasicResponse> queue() throws URISyntaxException {
        if (baseUrl == null) throw new NullPointerException("The baseUrl is missing.");
        if (token == null) throw new NullPointerException("The token is missing");
        if (route == null) throw new NullPointerException("The route is missing.");
        if (method == null) throw new NullPointerException("The method is missing.");

        SimpleHttpRequest request = SimpleRequestBuilder
                .create(method)
                .setHttpHost(HttpHost.create(baseUrl))
                .addHeader("token", token)
                .setPath(route)
                .build();

        JsonMapper mapper = new JsonMapper();
        CompletableFuture<BasicResponse> future = new CompletableFuture<>();

        if (body != null) {
            try {
                String json = mapper.writeValueAsString(body);
                request.setBody(json, ContentType.APPLICATION_JSON);
            } catch (Exception e) {
                future.completeExceptionally(e);
                return future;
            }
        }

        CLIENT.execute(request, new FutureCallback<>() {
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
                                new ApiRequestException(
                                        statusCode,
                                        jsonNode,
                                        String.format("Request failed with status %d: %s",
                                                statusCode,
                                                jsonNode.has("error") ? jsonNode.get("error").toPrettyString() : jsonNode.toPrettyString())
                                )
                        );
                        return;
                    }

                    future.complete(new BasicResponse(statusCode, jsonNode));
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
