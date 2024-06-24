package com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.*;
import com.beanbeanjuice.cafeapi.wrapper.utility.Time;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * A class used for {@link Birthday} requests to the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class BirthdaysEndpoint extends CafeEndpoint {

    private HashMap<String, Birthday> requestToBirthdayMap(Request request) {
        HashMap<String, Birthday> birthdays = new HashMap<>();

        request.getData().get("birthdays").forEach((birthdayNode) -> {
            String userID = birthdayNode.get("user_id").asText();
            parseBirthday(birthdayNode).ifPresent((birthday -> birthdays.put(userID, birthday)));
        });

        return birthdays;
    }

    /**
     * Retrieves all {@link Birthday} from the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with keys of {@link String userID} and values of {@link Birthday}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public CompletableFuture<HashMap<String, Birthday>> getAllBirthdays() throws AuthorizationException, ResponseException {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/birthdays")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((optionalRequest) -> {
                    if (optionalRequest.isPresent()) return requestToBirthdayMap(optionalRequest.get());
                    throw new CompletionException("Error getting all birthdays. Request is empty.", null);
                });
    }

    /**
     * Retrieves the {@link Birthday} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return The {@link Birthday} for the specified {@link String userID}.
     */
    public CompletableFuture<Optional<Birthday>> getUserBirthday(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/birthdays/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((optionalRequest) -> {
                    if (optionalRequest.isPresent()) return parseBirthday(optionalRequest.get().getData().get("birthday"));
                    throw new CompletionException("Error getting user birthday. Request is empty.", null);
                });
    }

    /**
     * Updates the {@link Birthday} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param birthday The {@link Birthday} for the specified {@link String userID}.
     * @return True, if the {@link Birthday} was successfully updated in the {@link CafeAPI CafeAPI}.
     */
    public CompletableFuture<Boolean> updateUserBirthday(final String userID, final Birthday birthday) {
        BirthdayMonth month = birthday.getMonth();
        int day = birthday.getDay();

        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/birthdays/" + userID)
                .addParameter("birthday", getBirthdayString(month, day))
                .addParameter("time_zone", birthday.getTimeZone().getID())
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((optionalRequest) -> {
                    if (optionalRequest.isPresent()) return optionalRequest.get().getStatusCode() == 200;
                    throw new CompletionException("Error updating user birthday. Request is empty.", null);
                });
    }

    /**
     * Updates the {@link Boolean alreadyMentioned} state for a {@link Birthday}.
     * @param userID The specified {@link String userID}.
     * @param alreadyMentioned The new {@link Boolean alreadyMentioned} state.
     * @return True, if the {@link Birthday} was successfully updated in the {@link CafeAPI CafeAPI}.
     */
    public CompletableFuture<Boolean> updateUserBirthdayMention(final String userID, final boolean alreadyMentioned) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/birthdays/" + userID + "/mention")
                .addParameter("already_mentioned", String.valueOf(alreadyMentioned))
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((optionalRequest) -> {
                    if (optionalRequest.isPresent()) return optionalRequest.get().getStatusCode() == 200;
                    throw new CompletionException("Error updating user mention. Request is empty.", null);
                });
    }

    /**
     * Creates a {@link Birthday} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param birthday The {@link Birthday} specified for the {@link String userID}.
     * @return True, if the {@link Birthday} was successfully created.
     */
    public CompletableFuture<Boolean> createUserBirthday(final String userID, final Birthday birthday) {
        BirthdayMonth month = birthday.getMonth();
        int day = birthday.getDay();

        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/birthdays/" + userID)
                .addParameter("birthday", getBirthdayString(month, day))
                .addParameter("time_zone", birthday.getTimeZone().getID())
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((optionalRequest) -> {
                    if (optionalRequest.isPresent()) return optionalRequest.get().getStatusCode() == 201;
                    throw new CompletionException("Error creating user birthday. Request is empty.", null);
                });
    }

    /**
     * Removes a {@link Birthday} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Birthday} has been successfully removed from the {@link CafeAPI CafeAPI}.
     */
    public CompletableFuture<Boolean> removeUserBirthday(final String userID) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/birthdays/" + userID)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((optionalRequest) -> {
                    if (optionalRequest.isPresent()) return optionalRequest.get().getStatusCode() == 200;
                    throw new CompletionException("Error removing user birthday. Request is empty.", null);
                });
    }

    /**
     * Parses a {@link Birthday} from a {@link JsonNode}.
     * @param birthday The {@link JsonNode} to parse.
     * @return The parsed {@link Birthday}.
     */
    private Optional<Birthday> parseBirthday(final JsonNode birthday) {
        String unformattedDate = birthday.get("birth_date").asText();
        String timeZoneString = birthday.get("time_zone").asText();
        boolean alreadyMentioned = birthday.get("already_mentioned").asBoolean();

        try {
            Date date = Time.getFullDate(unformattedDate + "-2020", TimeZone.getTimeZone(timeZoneString));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return Optional.of(new Birthday(getBirthdayMonth(month), day, timeZoneString, alreadyMentioned));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves the {@link BirthdayMonth} from the {@link Integer index}.
     * @param index The {@link Integer index} of the month. January is 0.
     * @return The {@link BirthdayMonth} from the {@link Integer index}.
     */
    private BirthdayMonth getBirthdayMonth(final int index) {
        for (BirthdayMonth month : BirthdayMonth.values()) {
            if (month.getMonthNumber() == index)
                return month;
        }

        return BirthdayMonth.ERROR;
    }

    /**
     * Parses a number and adds a 0 to the {@link String}.
     * @param number The {@link Integer number} to parse.
     * @return The parsed {@link String number}. Double-digit numbers don't have a 0 added.
     */
    private String parseNumber(final int number) {  // TODO: There MUST be a better way to do this.
        if (number <= 9) return "0" + number;
        return String.valueOf(number);
    }

    /**
     * Gets the birthday {@link String} to send to the {@link CafeAPI CafeAPI}.
     * @param month The {@link BirthdayMonth montH} of the {@link Birthday}.
     * @param day The {@link Integer day} in the {@link BirthdayMonth month} of the {@link Birthday}.
     * @return The birthday {@link String} to send to the {@link CafeAPI CafeAPI}.
     */
    private String getBirthdayString(final BirthdayMonth month, final int day) {
        return parseNumber(month.getMonthNumber()) + "-" + parseNumber(day);
    }

}
