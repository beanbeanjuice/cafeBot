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
    @DisplayName("Interactions (SENDERS) Endpoint Test")
    public void testInteractionSendersEndpoint() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure to delete the user before-hand.
        Assertions.assertTrue(cafeAPI.getInteractionsEndpoint().deleteUserInteractionsSent("879761226761109544"));

        // Makes sure that a NotFoundException is thrown when trying to get the user.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.getInteractionsEndpoint().getUserInteractionsSent("879761226761109544"));

        // Makes sure it is able to create the user.
        Assertions.assertTrue(cafeAPI.getInteractionsEndpoint().createUserInteractionsSent("879761226761109544"));

        // Makes sure a ConflictException is thrown when the same user is tried to be made again.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.getInteractionsEndpoint().createUserInteractionsSent("879761226761109544"));

        int interactionValue = 10;
        for (InteractionType type : InteractionType.values()) {
            Assertions.assertTrue(cafeAPI.getInteractionsEndpoint().updateSpecificUserInteractionSentAmount("879761226761109544", type, interactionValue));
            Assertions.assertEquals(interactionValue, cafeAPI.getInteractionsEndpoint().getAllInteractionSenders().get("879761226761109544").getInteractionAmount(type));
            interactionValue++;
        }

        // Deletes the user from the database.
        Assertions.assertTrue(cafeAPI.getInteractionsEndpoint().deleteUserInteractionsSent("879761226761109544"));
    }

    @Test
    @DisplayName("Interactions (RECEIVERS) Endpoint Test")
    public void testInteractionsReceiversEndpoint() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure to delete the user before-hand.
        Assertions.assertTrue(cafeAPI.getInteractionsEndpoint().deleteUserInteractionsReceived("879761226761109544"));

        // Makes sure a NotFoundException is thrown when trying to search for the user.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.getInteractionsEndpoint().getUserInteractionsReceived("879761226761109544"));

        // Makes sure the user can be created.
        Assertions.assertTrue(cafeAPI.getInteractionsEndpoint().createUserInteractionsReceived("879761226761109544"));

        // Makes sure a ConflictException is thrown when trying to make the user again.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.getInteractionsEndpoint().createUserInteractionsReceived("879761226761109544"));

        int interactionValue = 10;
        for (InteractionType type : InteractionType.values()) {
            Assertions.assertTrue(cafeAPI.getInteractionsEndpoint().updateSpecificUserInteractionReceivedAmount("879761226761109544", type, interactionValue));
            Assertions.assertEquals(interactionValue, cafeAPI.getInteractionsEndpoint().getAllInteractionReceivers().get("879761226761109544").getInteractionAmount(type));
            interactionValue++;
        }

        // Deletes the user from the database.
        Assertions.assertTrue(cafeAPI.getInteractionsEndpoint().deleteUserInteractionsReceived("879761226761109544"));
    }

}
