package com.beanbeanjuice.cafeapi.cafebot.beancoins.users;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.exception.api.*;
import com.beanbeanjuice.cafeapi.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.requests.Request;
import com.beanbeanjuice.cafeapi.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.requests.RequestType;
import com.fasterxml.jackson.databind.JsonNode;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Optional;

/**
 * A class used to make {@link DonationUsers} requests to the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class DonationUsers implements com.beanbeanjuice.cafeapi.api.CafeAPI {

    private String apiKey;

    /**
     * Creates a new {@link DonationUsers} object.
     * @param apiKey The {@link String apiKey} used for authorization.
     */
    public DonationUsers(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retrieves all {@link Timestamp} from the {@link CafeAPI CafeAPI} containing when a specified {@link String userID} can be donated to again.
     * @return A {@link HashMap} with keys of {@link String userID} and values of {@link Timestamp timeUntilNextDonation}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public HashMap<String, Timestamp> getAllUserDonationTimes()
    throws AuthorizationException, ResponseException {
        HashMap<String, Timestamp> donationUsers = new HashMap<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/beanCoin/donation_users")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        for (JsonNode user : request.getData().get("users")) {
            String userID = user.get("user_id").asText();
            Timestamp timeUntilNextDonation = CafeGeneric.parseTimestampFromAPI(user.get("time_until_next_donation").asText()).orElse(null);

            donationUsers.put(userID, timeUntilNextDonation);
        }

        return donationUsers;
    }

    /**
     * Retrieves the {@link Timestamp timeUntilNextDonation} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return The {@link Timestamp timeUntilNextDonation} for the specified {@link String userID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the {@link Timestamp timeUntilNextDonation} does not exist for the specified {@link String userID}.
     */
    public Optional<Timestamp> getUserDonationTime(String userID)
    throws AuthorizationException, ResponseException, NotFoundException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/beanCoin/donation_users/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return CafeGeneric.parseTimestampFromAPI(request.getData().get("user").get("time_until_next_donation").asText());
    }

    /**
     * Creates a new {@link Timestamp timeUntilNextDonation} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param timeUntilNextDonation The new {@link Timestamp timeUntilNextDonation}.
     * @return True, if the {@link Timestamp timeUntilNextDonation} was successfully created in the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws ConflictException Thrown when the {@link Timestamp timeUntilNextDonation} already exists for the specified {@link String userID}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public Boolean addDonationUser(String userID, Timestamp timeUntilNextDonation)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/beanCoin/donation_users/" + userID)
                .addParameter("time_stamp", timeUntilNextDonation.toString())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Deletes a {@link Timestamp timeUntilNextDonation} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Timestamp timeUntilNextDonation} was successfully deleted for the specified {@link String userID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public Boolean deleteDonationUser(String userID)
    throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/beanCoin/donation_users/" + userID)
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
