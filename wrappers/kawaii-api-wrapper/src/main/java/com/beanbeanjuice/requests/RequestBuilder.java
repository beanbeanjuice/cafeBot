package com.beanbeanjuice.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * A {@link RequestBuilder} used to build {@link Request} objects.
 *
 * @author beanbeanjuice
 */
public class RequestBuilder {

    private String apiURL;

    private HttpClient httpClient;
    private URIBuilder uriBuilder;

    /**
     * Creates a new {@link RequestBuilder}.
     * @param requestRoute The {@link RequestRoute} of the {@link Request}.
     */
    public RequestBuilder(RequestRoute requestRoute) {
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
            httpClient = HttpClients.createDefault();
            uriBuilder = new URIBuilder(apiURL);

            HttpResponse httpResponse = get();

            Integer statusCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity httpEntity = httpResponse.getEntity();
            try (InputStream inputStream = httpEntity.getContent()) {
                return Optional.of(new Request(statusCode, new ObjectMapper().readTree(inputStream)));
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();  // TODO: Better logging.
            return Optional.empty();
        }
    }

    private HttpResponse get() throws URISyntaxException, IOException {
        return httpClient.execute(new HttpGet(uriBuilder.build()));
    }

}
