package com.beanbeanjuice.cafebot.endpoints.discord.user;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.type.Donation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DonationApiTest extends ApiTest {

    private String user1;
    private String user2;

    @BeforeEach
    public void createDonations() throws ExecutionException, InterruptedException {
        user1 = generateSnowflake().toString();
        user2 = generateSnowflake().toString();

        // After these two, user1 should have 20 more than user2.
        cafeAPI.getDonationApi().createDonation(user1, user2, 10).get();
        cafeAPI.getDonationApi().createDonation(user2, user1, 20).get();
    }

    @Test
    @DisplayName("can get all sent/received donations for a user")
    public void getAllDonations() throws ExecutionException, InterruptedException {
        ArrayList<Donation> user1Donations = cafeAPI.getDonationApi().getDonations(user1).get();
        ArrayList<Donation> user2Donations = cafeAPI.getDonationApi().getDonations(user2).get();

        Assertions.assertEquals(2, user1Donations.size());
        Assertions.assertEquals(2, user2Donations.size());
    }

    @Test
    @DisplayName("can get all sent donations for a user")
    public void getAllSentDonations() throws ExecutionException, InterruptedException {
        ArrayList<Donation> donations = cafeAPI.getDonationApi().getSentDonations(user1).get();

        Assertions.assertEquals(1, donations.size());
    }

    @Test
    @DisplayName("can get all received donations for a user")
    public void getAllReceivedDonations() throws ExecutionException, InterruptedException {
        ArrayList<Donation> donations = cafeAPI.getDonationApi().getReceivedDonations(user1).get();

        Assertions.assertEquals(1, donations.size());
    }

}
