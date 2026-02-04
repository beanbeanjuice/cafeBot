package com.beanbeanjuice.cafebot.api.wrapper.api.discord;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.OwnerType;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.calendar.Calendar;
import com.beanbeanjuice.cafebot.api.wrapper.type.calendar.PartialCalendar;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CalendarApi extends Api {

    public CalendarApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<Calendar> getCalendar(String calendarId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/calendars/%s", calendarId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("calendar"))
                    .thenApply(this::parseCalendar);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<List<Calendar>> getGuildCalendars(String guildId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/calendars/guild/%s", guildId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("calendars"))
                    .thenApply(this::parseCalendars);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<List<Calendar>> getUserCalendars(String userId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/calendars/user/%s", userId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("calendars"))
                    .thenApply(this::parseCalendars);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Calendar> createCalendar(PartialCalendar calendar) {
        Map<String, String> body = new HashMap<>();

        body.put("ownerId", calendar.getOwnerId());
        body.put("ownerType", calendar.getOwnerType().toString());
        body.put("name", calendar.getName());
        body.put("url", calendar.getUrl());

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/calendars")
                    .body(body)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("calendar"))
                    .thenApply(this::parseCalendar);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Void> deleteCalendar(String calendarId, String callerId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.DELETE)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/calendars/%s?callerId=%s", calendarId, callerId))
                    .queue()
                    .thenApply((res) -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private Calendar parseCalendar(JsonNode calendarNode) {
        String id = calendarNode.get("id").asString();
        OwnerType ownerType = OwnerType.valueOf(calendarNode.get("ownerType").asString());
        String ownerId = (ownerType == OwnerType.DISCORD_USER) ? calendarNode.get("discordUserId").asString() : calendarNode.get("guildId").asString();

        String name = calendarNode.get("name").asString();
        String url = calendarNode.get("url").asString();

        return new Calendar(id, ownerType, ownerId, name, url);
    }

    private List<Calendar> parseCalendars(JsonNode calendarsNode) {
        List<Calendar> calendars = new ArrayList<>();

        for (JsonNode calendarNode : calendarsNode) calendars.add(parseCalendar(calendarNode));

        return calendars;
    }

}
