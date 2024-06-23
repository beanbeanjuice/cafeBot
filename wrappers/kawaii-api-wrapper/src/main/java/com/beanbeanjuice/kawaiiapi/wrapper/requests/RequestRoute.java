package com.beanbeanjuice.kawaiiapi.wrapper.requests;

/**
 * A static class for the endpoints of the Kawaii API.
 *
 * @author beanbeanjuice
 * @since 1.0.0
 * @see <a href="https://docs.kawaii.red/request-structure">Kawaii API Endpoints</a>
 */
public enum RequestRoute {

    TEXT("txt"),
    IMAGE("png"),
    GIF("gif"),
    STATS("stats");

    private final String API_LINK = "https://kawaii.red/api/{endpoint}/{type}/token={token}";
    private final String endpoint;

    /**
     * Creates a new {@link RequestRoute} object.
     * @param endpoint The {@link String endpoint} link.
     */
    RequestRoute(final String endpoint) { this.endpoint = endpoint; }

    /**
     * @return The {@link String link} for the endpoint.
     */
    public String getLink() {
        return API_LINK.replace("{endpoint}", endpoint);
    }

}
