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

import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;

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
    public HashMap<String, Birthday> getAllBirthdays() throws AuthorizationException, ResponseException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/birthdays")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return requestToBirthdayMap(request);
    }

    public void getAllBirthdaysAsync(Consumer<HashMap<String, Birthday>> successFunction, Consumer<Exception> errorFunction) {
        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/birthdays")
                .setAuthorization(apiKey)
                .onSuccess((request) -> successFunction.accept(requestToBirthdayMap(request)))
                .onError(errorFunction)
                .buildAsync();
    }

    public void getAllBirthdaysAsync(Consumer<HashMap<String, Birthday>> successFunction) {
        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/birthdays")
                .setAuthorization(apiKey)
                .buildAsync((request) -> successFunction.accept(requestToBirthdayMap(request)));
    }

    /**
     * Retrieves the {@link Birthday} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return The {@link Birthday} for the specified {@link String userID}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the {@link Birthday} for the specified {@link String userID} does not exist.
     * @throws ParseException Thrown when there was an error parsing the {@link Birthday}.
     */
    public Optional<Birthday> getUserBirthday(final String userID)
            throws AuthorizationException, ResponseException, NotFoundException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/birthdays/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return parseBirthday(request.getData().get("birthday"));
    }

    public void getUserBirthdayAsync(final String userID, final Consumer<Birthday> successFunction, final Consumer<Exception> errorFunction) {
        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/birthdays/" + userID)
                .setAuthorization(apiKey)
                .onSuccess((request) -> parseBirthday(request.getData().get("birthday")).ifPresentOrElse(
                        successFunction,
                        () -> errorFunction.accept(new ParseException("There was an error parsing the birthday.", 0))
                ))
                .onError(errorFunction)
                .buildAsync();
    }

    public void getUserBirthdayAsync(final String userID, final Consumer<Birthday> successFunction) {
        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/birthdays/" + userID)
                .setAuthorization(apiKey)
                .buildAsync((request) -> parseBirthday(request.getData().get("birthday")).ifPresent(successFunction));
    }

    /**
     * Updates the {@link Birthday} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param birthday The {@link Birthday} for the specified {@link String userID}.
     * @return True, if the {@link Birthday} was successfully updated in the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the {@link Birthday} for the specified {@link String userID} does not exist.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     * @throws TeaPotException Thrown when the {@link BirthdayMonth month} or {@link Integer day} is invalid.
     */
    public boolean updateUserBirthday(final String userID, final Birthday birthday)
    throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        BirthdayMonth month = birthday.getMonth();
        int day = birthday.getDay();

        // TODO: Check for invalid days/month.
//        // Checking for days in a month.
//        if (month.getDaysInMonth() < day)
//            throw new TeaPotException("There are only " + month.getDaysInMonth() + " days in " + month + ", not " + day + ".");
//
//        // Checking if the month is valid.
//        if (month == BirthdayMonth.ERROR)
//            throw new TeaPotException(month + " is an invalid month.");

        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/birthdays/" + userID)
                .addParameter("birthday", getBirthdayString(month, day))
                .addParameter("time_zone", birthday.getTimeZone().getID())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    public void updateUserBirthdayAsync(final String userID, final Birthday birthday, final Runnable successFunction, final Consumer<Exception> errorFunction) {
        BirthdayMonth month = birthday.getMonth();
        int day = birthday.getDay();

        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/birthdays/" + userID)
                .addParameter("birthday", getBirthdayString(month, day))
                .addParameter("time_zone", birthday.getTimeZone().getID())
                .setAuthorization(apiKey)
                .onSuccess((request) -> successFunction.run())
                .onError(errorFunction)
                .buildAsync();
    }

    public void updateUserBirthdayAsync(final String userID, final Birthday birthday, final Runnable successFunction) {
        BirthdayMonth month = birthday.getMonth();
        int day = birthday.getDay();

        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/birthdays/" + userID)
                .addParameter("birthday", getBirthdayString(month, day))
                .addParameter("time_zone", birthday.getTimeZone().getID())
                .setAuthorization(apiKey)
                .buildAsync((request) -> successFunction.run());
    }

    /**
     * Updates the {@link Boolean alreadyMentioned} state for a {@link Birthday}.
     * @param userID The specified {@link String userID}.
     * @param alreadyMentioned The new {@link Boolean alreadyMentioned} state.
     * @return True, if the {@link Birthday} was successfully updated in the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws NotFoundException Thrown when the {@link Birthday} does not exist for the specified {@link String userID}.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean updateUserBirthdayMention(final String userID, final boolean alreadyMentioned)
    throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/birthdays/" + userID + "/mention")
                .addParameter("already_mentioned", String.valueOf(alreadyMentioned))
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    public void updateUserBirthdayMentionAsync(final String userID, final boolean alreadyMentioned, Runnable successFunction, Consumer<Exception> errorFunction) {
        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/birthdays/" + userID + "/mention")
                .addParameter("already_mentioned", String.valueOf(alreadyMentioned))
                .setAuthorization(apiKey)
                .onSuccess((request) -> successFunction.run())
                .onError(errorFunction)
                .buildAsync();
    }

    public void updateUserBirthdayMentionAsync(final String userID, final boolean alreadyMentioned, Runnable successFunction) {
        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/birthdays/" + userID + "/mention")
                .addParameter("already_mentioned", String.valueOf(alreadyMentioned))
                .setAuthorization(apiKey)
                .buildAsync((request) -> successFunction.run());
    }

    /**
     * Creates a {@link Birthday} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param birthday The {@link Birthday} specified for the {@link String userID}.
     * @return True, if the {@link Birthday} was successfully created.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws ConflictException Thrown when the {@link Birthday} for the specified {@link String userID} already exists.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     * @throws TeaPotException Thrown when the {@link BirthdayMonth month} or {@link Integer day} is invalid.
     */
    public boolean createUserBirthday(final String userID, final Birthday birthday)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException, TeaPotException {
        BirthdayMonth month = birthday.getMonth();
        int day = birthday.getDay();

        // TODO: Check if this error checking is required.
//        // Checking for days in a month.
//        if (month.getDaysInMonth() < day)
//            throw new TeaPotException("There are only " + month.getDaysInMonth() + " days in " + month + ", not " + day + ".");
//
//        // Checking if the month is valid.
//        if (month == BirthdayMonth.ERROR)
//            throw new TeaPotException(month + " is an invalid month.");

        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/birthdays/" + userID)
                .addParameter("birthday", getBirthdayString(month, day))
                .addParameter("time_zone", birthday.getTimeZone().getID())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    public void createUserBirthdayAsync(final String userID, final Birthday birthday, final Runnable successFunction, final Consumer<Exception> errorFunction) {
        BirthdayMonth month = birthday.getMonth();
        int day = birthday.getDay();

        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/birthdays/" + userID)
                .addParameter("birthday", getBirthdayString(month, day))
                .addParameter("time_zone", birthday.getTimeZone().getID())
                .setAuthorization(apiKey)
                .onSuccess((request) -> successFunction.run())
                .onError(errorFunction)
                .buildAsync();
    }

    public void createUserBirthdayAsync(final String userID, final Birthday birthday, final Runnable successFunction) {
        BirthdayMonth month = birthday.getMonth();
        int day = birthday.getDay();

        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/birthdays/" + userID)
                .addParameter("birthday", getBirthdayString(month, day))
                .addParameter("time_zone", birthday.getTimeZone().getID())
                .setAuthorization(apiKey)
                .buildAsync((request) -> successFunction.run());
    }

    /**
     * Removes a {@link Birthday} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Birthday} has been successfully removed from the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public boolean removeUserBirthday(final String userID)
    throws AuthorizationException, ResponseException {
        Request request = RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/birthdays/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    public void removeUserBirthdayAsync(final String userID, final Runnable successFunction, final Consumer<Exception> errorFunction) {
        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/birthdays/" + userID)
                .setAuthorization(apiKey)
                .onSuccess((request) -> successFunction.run())
                .onError(errorFunction)
                .buildAsync();
    }

    public void removeUserBirthdayAsync(final String userID, final Runnable successFunction) {
        RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/birthdays/" + userID)
                .setAuthorization(apiKey)
                .buildAsync((request) -> successFunction.run());
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
