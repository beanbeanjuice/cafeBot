package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InteractionTest {

    @Test
    @DisplayName("Test Interactions API (SENDERS)")
    public void interactionSendersAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure to delete the user before-hand.
        Assertions.assertTrue(cafeAPI.INTERACTION.deleteUserInteractionsSent("879761226761109544"));

        // Makes sure that a NotFoundException is thrown when trying to get the user.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.INTERACTION.getUserInteractionsSent("879761226761109544"));

        // Makes sure it is able to create the user.
        Assertions.assertTrue(cafeAPI.INTERACTION.createUserInteractionsSent("879761226761109544"));

        // Makes sure a ConflictException is thrown when the same user is tried to be made again.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.INTERACTION.createUserInteractionsSent("879761226761109544"));

        int interactionValue = 10;
        for (InteractionType type : InteractionType.values()) {
            Assertions.assertTrue(cafeAPI.INTERACTION.updateSpecificUserInteractionSentAmount("879761226761109544", type, interactionValue));
            Assertions.assertEquals(interactionValue, cafeAPI.INTERACTION.getAllInteractionSenders().get("879761226761109544").getInteractionAmount(type));
            interactionValue++;
        }

        // Deletes the user from the database.
        Assertions.assertTrue(cafeAPI.INTERACTION.deleteUserInteractionsSent("879761226761109544"));
    }

    @Test
    @DisplayName("Test Interactions API (RECEIVERS)")
    public void interactionReceiversAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure to delete the user before-hand.
        Assertions.assertTrue(cafeAPI.INTERACTION.deleteUserInteractionsReceived("879761226761109544"));

        // Makes sure a NotFoundException is thrown when trying to search for the user.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.INTERACTION.getUserInteractionsReceived("879761226761109544"));

        // Makes sure the user can be created.
        Assertions.assertTrue(cafeAPI.INTERACTION.createUserInteractionsReceived("879761226761109544"));

        // Makes sure a ConflictException is thrown when trying to make the user again.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.INTERACTION.createUserInteractionsReceived("879761226761109544"));

        int interactionValue = 10;
        for (InteractionType type : InteractionType.values()) {
            Assertions.assertTrue(cafeAPI.INTERACTION.updateSpecificUserInteractionReceivedAmount("879761226761109544", type, interactionValue));
            Assertions.assertEquals(interactionValue, cafeAPI.INTERACTION.getAllInteractionReceivers().get("879761226761109544").getInteractionAmount(type));
            interactionValue++;
        }

        // Deletes the user from the database.
        Assertions.assertTrue(cafeAPI.INTERACTION.deleteUserInteractionsReceived("879761226761109544"));
    }

}
