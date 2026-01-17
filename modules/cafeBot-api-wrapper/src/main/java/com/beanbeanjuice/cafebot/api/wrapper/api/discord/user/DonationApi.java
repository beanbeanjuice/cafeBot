package com.beanbeanjuice.cafebot.api.wrapper.api.discord.user;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.Donation;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class DonationApi extends Api {

    public DonationApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<ArrayList<Donation>> getDonations(String userId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/donations/%s", userId))
                    .queue()
                    .thenApply(this::parseDonations);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<ArrayList<Donation>> getSentDonations(String userId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/donations/%s/sent", userId))
                    .queue()
                    .thenApply(this::parseDonations);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<ArrayList<Donation>> getReceivedDonations(String userId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/users/donations/%s/received", userId))
                    .queue()
                    .thenApply(this::parseDonations);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private ArrayList<Donation> parseDonations(BasicResponse response) {
        ArrayList<Donation> donations = new ArrayList<>();

        JsonNode donationsNode = response.getBody().get("donations");

        for (JsonNode donation : donationsNode) {
            String from = donation.get("from").asString();
            String to = donation.get("to").asString();
            int amount = donation.get("amount").asInt();
            String date = donation.get("createdAt").asString();

            donations.add(new Donation(from, to, amount, date));
        }

        return donations;
    }

    public CompletableFuture<Boolean> createDonation(String fromId, String toId, double amount) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("from", fromId);
        map.put("to", toId);
        map.put("amount", amount);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route("/api/v4/discord/users/donations")
                    .body(map)
                    .queue()
                    .thenApply((response) -> true); // Just return true. If it's false it will return exceptionally.
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

}
