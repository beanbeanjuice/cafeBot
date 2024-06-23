package com.beanbeanjuice.kawaiiapi.wrapper.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.HttpResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A {@link RequestBuilder} used to build {@link Request} objects.
 *
 * @author beanbeanjuice
 * @since 2.0.0
 */
public class RequestBuilder {

    private String apiURL;

    /**
     * Creates a new {@link RequestBuilder}.
     * @param requestRoute The {@link RequestRoute} of the {@link Request}.
     */
    public RequestBuilder(final RequestRoute requestRoute) {
        apiURL = requestRoute.getLink();
    }

    /**
     * Sets the 'type'.
     * @param value The {@link String value} to set the type to.
     * @return The new {@link RequestBuilder}.
     */
    public RequestBuilder setParameter(final String value) {
        apiURL = apiURL.replace("{type}", value);
        return this;
    }

    /**
     * Sets the API key for the {@link RequestBuilder}.
     * @param apiKey The {@link String api key} for the {@link RequestBuilder}.
     * @return The new {@link RequestBuilder}.
     */
    public RequestBuilder setAuthorization(final String apiKey) {
        apiURL = apiURL.replace("{token}", apiKey);
        return this;
    }

    /**
     * Builds the {@link RequestBuilder}.
     * @return The resulting {@link Optional<Request>}.
     */
    public Optional<Request> build() {
        try {
            SimpleHttpRequest request = SimpleRequestBuilder.get(apiURL).build();
            SimpleHttpResponse response = (SimpleHttpResponse) get(request);

            byte[] content = response.getBody().getBodyBytes();
            return Optional.of(new Request(response.getCode(), new ObjectMapper().readTree(content)));
        } catch (Exception e) {
            Logger.getLogger(RequestBuilder.class.getName()).log(Level.WARNING, "Error queuing request: " + e.getMessage());
            return Optional.empty();
        }
    }

    private HttpResponse get(final SimpleHttpRequest request) throws IOException, ExecutionException, InterruptedException {
        CloseableHttpAsyncClient client = HttpAsyncClients.custom().build();
        client.start();

        Future<SimpleHttpResponse> future = client.execute(request, null);
        HttpResponse response = future.get();

        client.close();
        return response;
    }

}
