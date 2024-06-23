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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * A class used for {@link Birthday} requests to the {@link CafeAPI CafeAPI}.
 *
 * @author beanbeanjuice
 */
public class BirthdaysEndpoint extends CafeEndpoint {

    /**
     * Retrieves all {@link Birthday} from the {@link CafeAPI CafeAPI}.
     * @return A {@link HashMap} with keys of {@link String userID} and values of {@link Birthday}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     * @throws ParseException Thrown when there is an error parsing the {@link Birthday}.
     */
    public HashMap<String, Birthday> getAllBirthdays()
            throws AuthorizationException, ResponseException, ParseException {
        HashMap<String, Birthday> birthdays = new HashMap<>();

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/birthdays")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        for (JsonNode birthday : request.getData().get("birthdays")) {
            String userID = birthday.get("user_id").asText();
            birthdays.put(userID, parseBirthday(birthday));
        }

        return birthdays;
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
    public Birthday getUserBirthday(final String userID)
            throws AuthorizationException, ResponseException, NotFoundException, ParseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/birthdays/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return parseBirthday(request.getData().get("birthday"));
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
    public Boolean updateUserBirthday(final String userID, final Birthday birthday)
    throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException, TeaPotException {
        BirthdayMonth month = birthday.getMonth();
        int day = birthday.getDay();

        // Checking for days in a month.
        if (month.getDaysInMonth() < day)
            throw new TeaPotException("There are only " + month.getDaysInMonth() + " days in " + month + ", not " + day + ".");

        // Checking if the month is valid.
        if (month == BirthdayMonth.ERROR)
            throw new TeaPotException(month + " is an invalid month.");

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/birthdays/" + userID)
                .addParameter("birthday", getBirthdayString(month, day))
                .addParameter("time_zone", birthday.getTimeZone().getID())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
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
    public Boolean updateUserBirthdayMention(final String userID, final boolean alreadyMentioned)
    throws AuthorizationException, ResponseException, NotFoundException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/birthdays/" + userID + "/mention")
                .addParameter("already_mentioned", String.valueOf(alreadyMentioned))
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
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
    public Boolean createUserBirthday(final String userID, final Birthday birthday)
    throws AuthorizationException, ResponseException, ConflictException, UndefinedVariableException, TeaPotException {
        BirthdayMonth month = birthday.getMonth();
        int day = birthday.getDay();

        // Checking for days in a month.
        if (month.getDaysInMonth() < day)
            throw new TeaPotException("There are only " + month.getDaysInMonth() + " days in " + month + ", not " + day + ".");

        // Checking if the month is valid.
        if (month == BirthdayMonth.ERROR)
            throw new TeaPotException(month + " is an invalid month.");

        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.POST)
                .setRoute("/birthdays/" + userID)
                .addParameter("birthday", getBirthdayString(month, day))
                .addParameter("time_zone", birthday.getTimeZone().getID())
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Removes a {@link Birthday} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Birthday} has been successfully removed from the {@link CafeAPI CafeAPI}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException}.
     */
    public Boolean removeUserBirthday(final String userID)
    throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.DELETE)
                .setRoute("/birthdays/" + userID)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

    /**
     * Parses a {@link Birthday} from a {@link JsonNode}.
     * @param birthday The {@link JsonNode} to parse.
     * @return The parsed {@link Birthday}.
     */
    private Birthday parseBirthday(final JsonNode birthday) throws ParseException {
        String unformattedDate = birthday.get("birth_date").asText();
        String timeZoneString = birthday.get("time_zone").asText();
        boolean alreadyMentioned = birthday.get("already_mentioned").asBoolean();

        Date date = Time.getFullDate(unformattedDate + "-2020", TimeZone.getTimeZone(timeZoneString));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new Birthday(getBirthdayMonth(month), day, timeZoneString, alreadyMentioned);
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
