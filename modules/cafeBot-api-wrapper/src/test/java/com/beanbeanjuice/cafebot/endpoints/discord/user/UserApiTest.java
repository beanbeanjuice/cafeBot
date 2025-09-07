package com.beanbeanjuice.cafebot.endpoints.discord.user;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.InteractionType;
import com.beanbeanjuice.cafebot.api.wrapper.type.DiscordUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class UserApiTest extends ApiTest {

    private String userId;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException {
        this.userId = generateSnowflake().toString();

        String user2 = generateSnowflake().toString();

        // Dummy create user in database.
        cafeAPI.getInteractionsApi().createInteraction(this.userId, user2, InteractionType.WELCOME).get();
    }

    @Test
    @DisplayName("can get user details")
    public void canGetUserDetails() throws ExecutionException, InterruptedException {
        DiscordUser user = cafeAPI.getUserApi().getUser(this.userId).get();

        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId, user.getId());
        Assertions.assertEquals(100.0f, user.getBalance());
        Assertions.assertTrue(user.getLastServeTime().isEmpty());
        Assertions.assertTrue(user.getLastDonationTime().isEmpty());
    }

}
