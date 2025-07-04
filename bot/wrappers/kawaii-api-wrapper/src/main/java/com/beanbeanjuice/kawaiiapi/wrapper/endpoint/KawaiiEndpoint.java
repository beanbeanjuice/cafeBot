package com.beanbeanjuice.kawaiiapi.wrapper.endpoint;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * An interface used for API requests.
 *
 * @author beanbeanjuice
 * @since 1.0.0
 */
public abstract class KawaiiEndpoint {

    protected static String apiKey;

    /**
     * Retrieve a {@link String link} from a {@link JsonNode}.
     * @param node The node to retrieve the {@link String link} from.
     * @return The {@link Optional<String> URL} link.
     */
    protected Optional<String> getLink(final JsonNode node) {
        if (node.has("response")) return Optional.of(node.get("response").asText());
        return Optional.empty();
    }

    public static void setAPIKey(final String apiKey) {
        KawaiiEndpoint.apiKey = apiKey;
    }

}
