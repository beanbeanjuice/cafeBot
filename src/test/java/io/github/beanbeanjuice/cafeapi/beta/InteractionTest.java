package io.github.beanbeanjuice.cafeapi.beta;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InteractionTest {

    @Test
    @DisplayName("Test Interactions API (SENDERS)")
    public void interactionSendersAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure to delete the user before-hand.
        Assertions.assertTrue(cafeAPI.interactions().deleteUserInteractionsSent("879761226761109544"));

        // Makes sure that a NotFoundException is thrown when trying to get the user.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.interactions().getUserInteractionsSent("879761226761109544"));

        // Makes sure it is able to create the user.
        Assertions.assertTrue(cafeAPI.interactions().createUserInteractionsSent("879761226761109544"));

        // Makes sure a ConflictException is thrown when the same user is tried to be made again.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.interactions().createUserInteractionsSent("879761226761109544"));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.HUG, 10));
        Assertions.assertEquals(10, cafeAPI.interactions().getAllInteractionSenders().get("879761226761109544").getHugAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.PUNCH, 11));
        Assertions.assertEquals(11, cafeAPI.interactions().getUserInteractionsSent("879761226761109544").getPunchAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.KISS, 12));
        Assertions.assertEquals(12, cafeAPI.interactions().getSpecificUserInteractionSentAmount("879761226761109544", InteractionType.KISS));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.BITE, 13));
        Assertions.assertEquals(13, cafeAPI.interactions().getAllInteractionSenders().get("879761226761109544").getBiteAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.BLUSH, 14));
        Assertions.assertEquals(14, cafeAPI.interactions().getUserInteractionsSent("879761226761109544").getBlushAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.CUDDLE, 15));
        Assertions.assertEquals(15, cafeAPI.interactions().getSpecificUserInteractionSentAmount("879761226761109544", InteractionType.CUDDLE));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.NOM, 16));
        Assertions.assertEquals(16, cafeAPI.interactions().getAllInteractionSenders().get("879761226761109544").getNomAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.POKE, 17));
        Assertions.assertEquals(17, cafeAPI.interactions().getUserInteractionsSent("879761226761109544").getPokeAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.SLAP, 18));
        Assertions.assertEquals(18, cafeAPI.interactions().getSpecificUserInteractionSentAmount("879761226761109544", InteractionType.SLAP));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.STAB, 19));
        Assertions.assertEquals(19, cafeAPI.interactions().getAllInteractionSenders().get("879761226761109544").getStabAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.HMPH, 20));
        Assertions.assertEquals(20, cafeAPI.interactions().getUserInteractionsSent("879761226761109544").getHmphAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.POUT, 21));
        Assertions.assertEquals(21, cafeAPI.interactions().getSpecificUserInteractionSentAmount("879761226761109544", InteractionType.POUT));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.THROW, 22));
        Assertions.assertEquals(22, cafeAPI.interactions().getAllInteractionSenders().get("879761226761109544").getThrowAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.SMILE, 23));
        Assertions.assertEquals(23, cafeAPI.interactions().getUserInteractionsSent("879761226761109544").getSmileAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.STARE, 24));
        Assertions.assertEquals(24, cafeAPI.interactions().getSpecificUserInteractionSentAmount("879761226761109544", InteractionType.STARE));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.TICKLE, 25));
        Assertions.assertEquals(25, cafeAPI.interactions().getAllInteractionSenders().get("879761226761109544").getTickleAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.RAGE, 26));
        Assertions.assertEquals(26, cafeAPI.interactions().getUserInteractionsSent("879761226761109544").getRageAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.YELL, 27));
        Assertions.assertEquals(27, cafeAPI.interactions().getSpecificUserInteractionSentAmount("879761226761109544", InteractionType.YELL));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.HEADPAT, 28));
        Assertions.assertEquals(28, cafeAPI.interactions().getAllInteractionSenders().get("879761226761109544").getHeadpatAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.CRY, 29));
        Assertions.assertEquals(29, cafeAPI.interactions().getUserInteractionsSent("879761226761109544").getCryAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.DANCE, 30));
        Assertions.assertEquals(30, cafeAPI.interactions().getSpecificUserInteractionSentAmount("879761226761109544", InteractionType.DANCE));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.DAB, 31));
        Assertions.assertEquals(31, cafeAPI.interactions().getAllInteractionSenders().get("879761226761109544").getDabAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.BONK, 32));
        Assertions.assertEquals(32, cafeAPI.interactions().getUserInteractionsSent("879761226761109544").getBonkAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.SLEEP, 33));
        Assertions.assertEquals(33, cafeAPI.interactions().getSpecificUserInteractionSentAmount("879761226761109544", InteractionType.SLEEP));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.DIE, 34));
        Assertions.assertEquals(34, cafeAPI.interactions().getAllInteractionSenders().get("879761226761109544").getDieAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.WELCOME, 35));
        Assertions.assertEquals(35, cafeAPI.interactions().getUserInteractionsSent("879761226761109544").getWelcomeAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.LICK, 36));
        Assertions.assertEquals(36, cafeAPI.interactions().getSpecificUserInteractionSentAmount("879761226761109544", InteractionType.LICK));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionSentAmount("879761226761109544", InteractionType.SHUSH, 37));
        Assertions.assertEquals(37, cafeAPI.interactions().getAllInteractionSenders().get("879761226761109544").getShushAmount());

        // Deletes the user from the database.
        Assertions.assertTrue(cafeAPI.interactions().deleteUserInteractionsSent("879761226761109544"));
    }

    @Test
    @DisplayName("Test Interactions API (RECEIVERS)")
    public void interactionReceiversAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure to delete the user before-hand.
        Assertions.assertTrue(cafeAPI.interactions().deleteUserInteractionsReceived("879761226761109544"));

        // Makes sure a NotFoundException is thrown when trying to search for the user.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.interactions().getUserInteractionsReceived("879761226761109544"));

        // Makes sure the user can be created.
        Assertions.assertTrue(cafeAPI.interactions().createUserInteractionsReceived("879761226761109544"));

        // Makes sure a ConflictException is thrown when trying to make the user again.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.interactions().createUserInteractionsReceived("879761226761109544"));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.HUG, 90));
        Assertions.assertEquals(90, cafeAPI.interactions().getAllInteractionReceivers().get("879761226761109544").getHugAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.PUNCH, 91));
        Assertions.assertEquals(91, cafeAPI.interactions().getUserInteractionsReceived("879761226761109544").getPunchAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.KISS, 92));
        Assertions.assertEquals(92, cafeAPI.interactions().getSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.KISS));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.BITE, 93));
        Assertions.assertEquals(93, cafeAPI.interactions().getAllInteractionReceivers().get("879761226761109544").getBiteAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.BLUSH, 94));
        Assertions.assertEquals(94, cafeAPI.interactions().getUserInteractionsReceived("879761226761109544").getBlushAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.CUDDLE, 95));
        Assertions.assertEquals(95, cafeAPI.interactions().getSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.CUDDLE));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.NOM, 96));
        Assertions.assertEquals(96, cafeAPI.interactions().getAllInteractionReceivers().get("879761226761109544").getNomAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.POKE, 97));
        Assertions.assertEquals(97, cafeAPI.interactions().getUserInteractionsReceived("879761226761109544").getPokeAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.SLAP, 98));
        Assertions.assertEquals(98, cafeAPI.interactions().getSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.SLAP));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.STAB, 99));
        Assertions.assertEquals(99, cafeAPI.interactions().getAllInteractionReceivers().get("879761226761109544").getStabAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.HMPH, 80));
        Assertions.assertEquals(80, cafeAPI.interactions().getUserInteractionsReceived("879761226761109544").getHmphAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.POUT, 81));
        Assertions.assertEquals(81, cafeAPI.interactions().getSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.POUT));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.THROW, 82));
        Assertions.assertEquals(82, cafeAPI.interactions().getAllInteractionReceivers().get("879761226761109544").getThrowAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.SMILE, 83));
        Assertions.assertEquals(83, cafeAPI.interactions().getUserInteractionsReceived("879761226761109544").getSmileAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.STARE, 84));
        Assertions.assertEquals(84, cafeAPI.interactions().getSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.STARE));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.TICKLE, 85));
        Assertions.assertEquals(85, cafeAPI.interactions().getAllInteractionReceivers().get("879761226761109544").getTickleAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.RAGE, 86));
        Assertions.assertEquals(86, cafeAPI.interactions().getUserInteractionsReceived("879761226761109544").getRageAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.YELL, 87));
        Assertions.assertEquals(87, cafeAPI.interactions().getSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.YELL));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.HEADPAT, 88));
        Assertions.assertEquals(88, cafeAPI.interactions().getAllInteractionReceivers().get("879761226761109544").getHeadpatAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.CRY, 89));
        Assertions.assertEquals(89, cafeAPI.interactions().getUserInteractionsReceived("879761226761109544").getCryAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.DANCE, 70));
        Assertions.assertEquals(70, cafeAPI.interactions().getSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.DANCE));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.DAB, 71));
        Assertions.assertEquals(71, cafeAPI.interactions().getAllInteractionReceivers().get("879761226761109544").getDabAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.BONK, 72));
        Assertions.assertEquals(72, cafeAPI.interactions().getUserInteractionsReceived("879761226761109544").getBonkAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.SLEEP, 73));
        Assertions.assertEquals(73, cafeAPI.interactions().getSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.SLEEP));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.DIE, 74));
        Assertions.assertEquals(74, cafeAPI.interactions().getAllInteractionReceivers().get("879761226761109544").getDieAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.WELCOME, 75));
        Assertions.assertEquals(75, cafeAPI.interactions().getUserInteractionsReceived("879761226761109544").getWelcomeAmount());

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.LICK, 76));
        Assertions.assertEquals(76, cafeAPI.interactions().getSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.LICK));

        Assertions.assertTrue(cafeAPI.interactions().updateSpecificUserInteractionReceivedAmount("879761226761109544", InteractionType.SHUSH, 77));
        Assertions.assertEquals(77, cafeAPI.interactions().getAllInteractionReceivers().get("879761226761109544").getShushAmount());

        // Deletes the user from the database.
        Assertions.assertTrue(cafeAPI.interactions().deleteUserInteractionsReceived("879761226761109544"));
    }

}
