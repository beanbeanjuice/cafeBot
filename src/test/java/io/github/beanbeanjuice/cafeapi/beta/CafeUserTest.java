package io.github.beanbeanjuice.cafeapi.beta;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.cafebot.cafe.CafeType;
import io.github.beanbeanjuice.cafeapi.cafebot.cafe.CafeUser;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import io.github.beanbeanjuice.cafeapi.exception.TeaPotException;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

public class CafeUserTest {

    @Test
    @DisplayName("Cafe User API Test")
    public void testCafeUserAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the user does not exist beforehand.
        Assertions.assertTrue(cafeAPI.cafeUsers().deleteCafeUser("236654580300120064"));

        // Makes sure the user does not exist when trying to get it.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.cafeUsers().getCafeUser("236654580300120064"));

        // Creates the user.
        Assertions.assertTrue(cafeAPI.cafeUsers().createCafeUser("236654580300120064"));

        // Makes sure the user cannot be created twice.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.cafeUsers().createCafeUser("236654580300120064"));

        // Makes sure the user exists in the array list.
        Assertions.assertTrue(() -> {
            for (CafeUser cafeUser : cafeAPI.cafeUsers().getAllCafeUsers()) {
                if (cafeUser.getUserID().equals("236654580300120064")) {
                    return true;
                }
            }
            return false;
        });

        // Makes sure the user ID matches the one retrieved.
        Assertions.assertEquals("236654580300120064", cafeAPI.cafeUsers().getCafeUser("236654580300120064").getUserID());

        // Makes sure all settings are default when first created.
        Assertions.assertEquals(0, cafeAPI.cafeUsers().getCafeUser("236654580300120064").getBeanCoins());
        Assertions.assertNull(cafeAPI.cafeUsers().getCafeUser("236654580300120064").getLastServingTime());
        Assertions.assertEquals(0, cafeAPI.cafeUsers().getCafeUser("236654580300120064").getOrdersBought());
        Assertions.assertEquals(0, cafeAPI.cafeUsers().getCafeUser("236654580300120064").getOrdersReceived());

        // Makes sure the beanCoins can be changed.
        Assertions.assertTrue(cafeAPI.cafeUsers().updateCafeUser("236654580300120064", CafeType.BEAN_COINS, 100.0));
        Assertions.assertEquals(100.0, cafeAPI.cafeUsers().getCafeUser("236654580300120064").getBeanCoins());

        // Makes sure the timestamp can be changed.
        Timestamp currentTimeStamp = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        Assertions.assertTrue(cafeAPI.cafeUsers().updateCafeUser("236654580300120064", CafeType.LAST_SERVING_TIME, currentTimeStamp));
        Assertions.assertEquals(currentTimeStamp, cafeAPI.cafeUsers().getCafeUser("236654580300120064").getLastServingTime());

        // Makes sure the timestamp can be changed to null.
        Assertions.assertTrue(cafeAPI.cafeUsers().updateCafeUser("236654580300120064", CafeType.LAST_SERVING_TIME, null));
        Assertions.assertNull(cafeAPI.cafeUsers().getCafeUser("236654580300120064").getLastServingTime());

        // Makes sure the orders bought can be updated.
        Assertions.assertTrue(cafeAPI.cafeUsers().updateCafeUser("236654580300120064", CafeType.ORDERS_BOUGHT, 10));
        Assertions.assertEquals(10, cafeAPI.cafeUsers().getCafeUser("236654580300120064").getOrdersBought());

        // Makes sure the orders received can be updated.
        Assertions.assertTrue(cafeAPI.cafeUsers().updateCafeUser("236654580300120064", CafeType.ORDERS_RECEIVED, 15));
        Assertions.assertEquals(15, cafeAPI.cafeUsers().getCafeUser("236654580300120064").getOrdersReceived());

        // Makes sure a TeaPotException is thrown when trying to update using an invalid value.
        Assertions.assertThrows(TeaPotException.class, () -> cafeAPI.cafeUsers().updateCafeUser("236654580300120064", CafeType.LAST_SERVING_TIME, 100));

        // Deletes a cafe user
        Assertions.assertTrue(cafeAPI.cafeUsers().deleteCafeUser("236654580300120064"));
    }

}
