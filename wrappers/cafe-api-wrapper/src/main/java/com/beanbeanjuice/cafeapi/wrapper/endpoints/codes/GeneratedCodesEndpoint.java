package com.beanbeanjuice.cafeapi.wrapper.endpoints.codes;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.*;

import java.util.HashMap;

/**
 * A class used for handling {@link GeneratedCodesEndpoint} {@link Request Requests} for the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class GeneratedCodesEndpoint extends CafeEndpoint {

    /**
     * Retrieves all {@link String generatedCode} from the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with keys of {@link String userID} and values of {@link String generatedCode}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public HashMap<String, String> getAllGeneratedCodes()
    throws AuthorizationException, ResponseException {
        HashMap<String, String> codes = new HashMap<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/codes")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        request.getData().get("users").forEach((user) -> codes.put(user.get("user_id").asText(), user.get("generated_code").asText()));

        return codes;
    }

    /**
     * Retrieves a {@link String generatedCode} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return The {@link String generatedCode} for the {@link String userID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when a {@link String generatedCode} for the specified {@link String userID} does not exist.
     */
    public String getUserGeneratedCode(final String userID)
    throws AuthorizationException, ResponseException, NotFoundException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/codes/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getData().get("generated_code").asText();
    }

    /**
     * Updates a {@link String generatedCode} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param newCode The new {@link String generatedCode}.
     * @return True, if the {@link String generatedCode} was successfully updated in the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when a {@link String generatedCode} does not exist for the specified {@link String userID}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean updateUserGeneratedCode(final String userID, final String newCode)
    throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/codes/" + userID)
                .addParameter("generated_code", newCode)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Creates a {@link String generatedCode} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param newCode The new {@link String generatedCode} to create for the {@link String userID}.
     * @return True, if the {@link String generatedCode} was successfully created for the {@link String userID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws ConflictException Thrown when the {@link String generatedCode} already exists for the specified {@link String userID}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public Boolean createUserGeneratedCode(final String userID, final String newCode)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/codes/" + userID)
                .addParameter("generated_code", newCode)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes a {@link String generatedCode} from a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link String generatedCode} was successfully deleted.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public Boolean deleteUserGeneratedCode(final String userID)
    throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/codes/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

}
