package com.beanbeanjuice.cafeapi.wrapper.endpoints.beancoins.users;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * A class used to make {@link DonationUsersEndpoint} requests to the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class DonationUsersEndpoint extends CafeEndpoint {

    public CompletableFuture<HashMap<String, Timestamp>> getAllUserDonationTimes() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/beanCoin/donation_users")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((optionalRequest) -> {
                    HashMap<String, Timestamp> donationUsers = new HashMap<>();

                    if (optionalRequest.isEmpty()) throw new CompletionException("Unable to get donation user times. Request is empty.", null);

                    optionalRequest.get().getData().get("users").forEach((userNode) -> {
                        String userID = userNode.get("user_id").asText();
                        Timestamp timeUntilNextDonation = CafeGeneric.parseTimestampFromAPI(userNode.get("time_until_next_donation").asText()).orElse(null);

                        donationUsers.put(userID, timeUntilNextDonation);
                    });

                    return donationUsers;
                });
    }

    public CompletableFuture<Optional<Timestamp>> getUserDonationTime(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/beanCoin/donation_users/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((optionalRequest) -> {
                    if (optionalRequest.isEmpty()) throw new CompletionException("Unable to get user donation time. Request is empty.", null);

                    return CafeGeneric.parseTimestampFromAPI(optionalRequest.get().getData().get("user").get("time_until_next_donation").asText());
                });
    }

    public CompletableFuture<Boolean> addDonationUser(final String userID, final Timestamp timeUntilNextDonation) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/beanCoin/donation_users/" + userID)
                .addParameter("time_stamp", timeUntilNextDonation.toString())
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((optionalRequest) -> {
                    if (optionalRequest.isEmpty()) throw new CompletionException("Unable to add donation user. Request is empty.", null);

                    return optionalRequest.get().getStatusCode() == 201;
                });
    }

    public CompletableFuture<Boolean> deleteDonationUser(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/beanCoin/donation_users/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApply((optionalRequest) -> {
                    if (optionalRequest.isEmpty()) throw new CompletionException("Unable to delete donation user. Request is empty.", null);

                    return optionalRequest.get().getStatusCode() == 200;
                });
    }

}
