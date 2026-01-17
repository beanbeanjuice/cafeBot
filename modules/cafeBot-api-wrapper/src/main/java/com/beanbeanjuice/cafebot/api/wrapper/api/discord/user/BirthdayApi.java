package com.beanbeanjuice.cafebot.api.wrapper.api.discord.user;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.type.Birthday;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

public class BirthdayApi extends Api {

    public BirthdayApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    /**
     * @return A {@link String[]} containing all of the available timezones.
     */
    public CompletableFuture<String[]> getTimezones() {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/users/birthday/timezones")
                    .queue()
                    .thenApply((basicResponse) -> {
                        JsonNode timezonesNode = basicResponse.getBody().get("timezones");
                        if (timezonesNode != null && timezonesNode.isArray()) {
                            return StreamSupport.stream(timezonesNode.spliterator(), false)
                                    .map(JsonNode::asString)
                                    .toArray(String[]::new);
                        } else {
                            return new String[0];
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    /**
     * Set someone's birthday with the API.
     * @param userId The {@link String id} of the person.
     * @param birthday The {@link Birthday} to set.
     * @return The newly set {@link Birthday}.
     */
    public CompletableFuture<Birthday> setBirthday(String userId, Birthday birthday) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("month", birthday.getMonth());
        body.put("day", birthday.getDay());
        body.put("year", birthday.getYear());
        body.put("timezone", birthday.getTimeZone());

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/birthday/%s", userId))
                    .body(body)
                    .queue()
                    .thenApply((response) -> {
                        JsonNode responseBirthday = response.getBody().get("birthday");
                        return new Birthday(
                                userId,
                                responseBirthday.get("year").asInt(),
                                responseBirthday.get("month").asInt(),
                                responseBirthday.get("day").asInt(),
                                TimeZone.getTimeZone(responseBirthday.get("timezone").asString()).toZoneId()
                        );
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    /**
     * Deletes a user's birthday.
     * @param userId The {@link String id} of the user.
     * @return Nothing.
     */
    public CompletableFuture<Void> deleteBirthday(String userId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.DELETE)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/birthday/%s", userId))
                    .queue()
                    .thenApply((response) -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Birthday> updateBirthdayMention(String userId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.PUT)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/birthday/%s", userId))
                    .queue()
                    .thenApply((response) -> {
                        JsonNode responseBirthday = response.getBody().get("birthday");
                        return new Birthday(
                                userId,
                                responseBirthday.get("year").asInt(),
                                responseBirthday.get("month").asInt(),
                                responseBirthday.get("day").asInt(),
                                TimeZone.getTimeZone(responseBirthday.get("timezone").asString()).toZoneId(),
                                responseBirthday.get("discordUser").get("lastBirthdayWishAt").asString()
                        );
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    /**
     * @param userId The {@link String id} of the person.
     * @return A {@link Birthday}.
     */
    public CompletableFuture<Birthday> getBirthday(String userId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/birthday/%s", userId))
                    .queue()
                    .thenApply((response) -> {
                        JsonNode responseBirthday = response.getBody().get("birthday");
                        return new Birthday(
                                userId,
                                responseBirthday.get("year").asInt(),
                                responseBirthday.get("month").asInt(),
                                responseBirthday.get("day").asInt(),
                                TimeZone.getTimeZone(responseBirthday.get("timezone").asString()).toZoneId(),
                                responseBirthday.get("discordUser").get("lastBirthdayWishAt").asString()
                        );
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    /**
     * @return An {@link ArrayList} containing all current birthdays up to floor of the hour.
     */
    public CompletableFuture<ArrayList<Birthday>> getCurrentBirthdays() {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/users/birthday/now")
                    .queue()
                    .thenApply((response) -> {
                        JsonNode responseBirthdays = response.getBody().get("birthdays");
                        ArrayList<Birthday> list = new ArrayList<>();

                        if (responseBirthdays != null && responseBirthdays.isArray()) {
                            for (JsonNode bdayNode : responseBirthdays) {
                                String userId = bdayNode.get("discordUserId").asString();
                                int year = bdayNode.get("year").asInt();
                                int month = bdayNode.get("month").asInt();
                                int day = bdayNode.get("day").asInt();
                                String tzStr = bdayNode.get("timezone").asString();
                                String lastMentionedAt = bdayNode.get("discordUser").get("lastBirthdayWishAt").toString();
                                Birthday bday = new Birthday(userId, year, month, day, ZoneId.of(tzStr), lastMentionedAt);
                                list.add(bday);
                            }
                        }

                        return list;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

}
