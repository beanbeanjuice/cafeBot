package com.beanbeanjuice.cafeapi.requests;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.exception.api.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Optional;

/**
 * A {@link RequestBuilder} used to build {@link Request} objects.
 *
 * @author beanbeanjuice
 */
public class RequestBuilder {

    private final RequestType requestType;
    private String route;
    private final HashMap<String, String> parameters;

    private String apiURL;

    private HttpClient httpClient;
    private URIBuilder uriBuilder;
    private HttpResponse httpResponse;
    private String apiKey;
    private Header authorization;

    /**
     * Creates a new {@link RequestBuilder}.
     * @param requestType The {@link RequestType type} of {@link Request}.
     * @param requestRoute The {@link RequestRoute} of the {@link Request}.
     */
    public RequestBuilder(RequestRoute requestRoute, RequestType requestType) {
        this.requestType = requestType;
        parameters = new HashMap<>();

        apiURL = CafeAPI.getRequestLocation().getURL() + requestRoute.getRoute();
    }

    /**
     * Set's the API route for the {@link RequestBuilder}.
     * @param route The {@link String route} for the {@link RequestBuilder}.
     * @return The new {@link RequestBuilder}.
     */
    public RequestBuilder setRoute(String route) {
        this.route = route;
        return this;
    }

    /**
     * Adds a parameter to the {@link RequestBuilder}.
     * @param key The {@link String key} for the {@link RequestBuilder}.
     * @param value The {@link String value} for the {@link String key} in the {@link RequestBuilder}.
     * @return The new {@link RequestBuilder}.
     */
    public RequestBuilder addParameter(String key, @Nullable String value) {
        parameters.put(key, value);
        return this;
    }

    /**
     * Sets the API key for the {@link RequestBuilder}.
     * @param apiKey The {@link String api key} for the {@link RequestBuilder}.
     * @return The new {@link RequestBuilder}.
     */
    public RequestBuilder setAuthorization(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    /**
     * Builds the {@link RequestBuilder}.
     * @return Null, if there is an error with the route. The resulting {@link Request}.
     */
    public Optional<Request> build() {
        try {
            httpClient = HttpClients.createDefault();
            uriBuilder = new URIBuilder(apiURL + route);
            parameters.forEach((key, value) -> {
                uriBuilder.setParameter(key, value);
            });
            authorization = new BasicHeader("Authorization", apiKey);

            switch (requestType) {
                case GET -> httpResponse = get();
                case POST -> httpResponse = post();
                case PATCH -> httpResponse = patch();
                case DELETE -> httpResponse = delete();
            }

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity httpEntity = httpResponse.getEntity();
            try (InputStream inputStream = httpEntity.getContent()) {
                Request request = new Request(statusCode, new ObjectMapper().readTree(inputStream));

                // Catching Status Codes
                if (request.getStatusCode() == 400) throw new UndefinedVariableException(request);
                if (request.getStatusCode() == 401) throw new AuthorizationException(request);
                if (request.getStatusCode() == 404) throw new NotFoundException(request);
                if (request.getStatusCode() == 409) throw new ConflictException(request);
                if (request.getStatusCode() == 418) throw new TeaPotException(request);
                if (request.getStatusCode() == 500) throw new ResponseException(request);

                return Optional.of(request);
            }
        } catch (URISyntaxException | IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves the {@link HttpResponse} for a {@link RequestType GET} request.
     * @return The {@link RequestType GET} {@link HttpResponse}.
     * @throws URISyntaxException Thrown if there is an issue with the route syntax.
     * @throws IOException Thrown if there is an issue with the data returned.
     */
    private HttpResponse get() throws URISyntaxException, IOException {
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader(authorization);

        return httpClient.execute(httpGet);
    }

    /**
     * Retrieves the {@link HttpResponse} for a {@link RequestType POST} request.
     * @return The {@link RequestType POST} {@link HttpResponse}.
     * @throws URISyntaxException Thrown if there is an issue with the route syntax.
     * @throws IOException Thrown if there is an issue with the data returned.
     */
    private HttpResponse post() throws URISyntaxException, IOException {
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.addHeader(authorization);

        return httpClient.execute(httpPost);
    }

    /**
     * Retrieves the {@link HttpResponse} for a {@link RequestType PATCH} request.
     * @return The {@link RequestType PATCH} {@link HttpResponse}.
     * @throws URISyntaxException Thrown if there is an issue with the route syntax.
     * @throws IOException Thrown if there is an issue with the data returned.
     */
    private HttpResponse patch() throws URISyntaxException, IOException {
        HttpPatch httpPatch = new HttpPatch(uriBuilder.build());
        httpPatch.addHeader(authorization);

        return httpClient.execute(httpPatch);
    }

    /**
     * Retrieves the {@link HttpResponse} for a {@link RequestType DELETE} request.
     * @return The {@link RequestType DELETE} {@link HttpResponse}.
     * @throws URISyntaxException Thrown if there is an issue with the route syntax.
     * @throws IOException Thrown if there is an issue with the data returned.
     */
    private HttpResponse delete() throws URISyntaxException, IOException {
        HttpDelete httpDelete = new HttpDelete(uriBuilder.build());
        httpDelete.addHeader(authorization);

        return httpClient.execute(httpDelete);
    }

    /**
     * @return The {@link String route} for the {@link RequestBuilder}.
     */
    public Optional<String> getRoute() {
        return Optional.ofNullable(route);
    }

}
