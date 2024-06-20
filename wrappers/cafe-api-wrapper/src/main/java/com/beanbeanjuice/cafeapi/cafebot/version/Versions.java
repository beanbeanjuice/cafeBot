package com.beanbeanjuice.cafeapi.cafebot.version;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.requests.Request;
import com.beanbeanjuice.cafeapi.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.requests.RequestType;
import com.beanbeanjuice.cafeapi.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.exception.api.ResponseException;
import com.beanbeanjuice.cafeapi.exception.api.TeaPotException;
import com.beanbeanjuice.cafeapi.exception.api.UndefinedVariableException;
import com.beanbeanjuice.cafeapi.exception.api.CafeException;

/**
 * A class used for handling CafeBot {@link Versions} in the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class Versions implements com.beanbeanjuice.cafeapi.api.CafeAPI {

    private String apiKey;

    /**
     * Creates a new {@link Versions} object.
     * @param apiKey The {@link String apiKey} used for authorization.
     */
    public Versions(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retrieves the current {@link String botVersion}.
     * @return The current {@link String botVersion} from the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public String getCurrentCafeBotVersion()
    throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/cafeBot")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return  request.getData().get("bot_information").get("version").asText();
    }

    /**
     * Updates the current {@link String botVersion} in the {@link CafeAPI CafeAPI}.
     * @param versionNumber The {@link String versionNumber} to update it to.
     * @return True, if the {@link String versionNumber} was successfully updated.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     * @throws TeaPotException Thrown when you forget to add "v" to the beginning of the version number.
     */
    public boolean updateCurrentCafeBotVersion(String versionNumber)
    throws AuthorizationException, ResponseException, UndefinedVariableException, TeaPotException {
        if (!versionNumber.startsWith("v")) {
            throw new TeaPotException("Version Number Must Start with 'v'.");
        }

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/cafeBot")
                .addParameter("version", versionNumber)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Updates the {@link String apiKey}.
     * @param apiKey The new {@link String apiKey}.
     */
    @Override
    public void updateAPIKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
