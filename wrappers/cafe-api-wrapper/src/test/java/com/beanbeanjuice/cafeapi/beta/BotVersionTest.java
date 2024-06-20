package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.exception.api.TeaPotException;
import com.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BotVersionTest {

    @Test
    @DisplayName("Bot Version API Test")
    public void testBotVersionAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Gets the current version of cafeBot.
        String currentVersion = cafeAPI.VERSION.getCurrentCafeBotVersion();

        // Makes sure the current version is not null.
        Assertions.assertNotNull(currentVersion);

        // Makes sure a TeaPotException is thrown when the user forgets to add a "v" to the beginning of the version number.
        Assertions.assertThrows(TeaPotException.class, () -> cafeAPI.VERSION.updateCurrentCafeBotVersion("2.0.0-UNIT-TEST"));

        // Makes sure the version number for cafeBot can be updated.
        Assertions.assertTrue(cafeAPI.VERSION.updateCurrentCafeBotVersion("v2.0.0-UNIT-TEST"));

        // Makes sure the version number has been changed.
        Assertions.assertEquals("v2.0.0-UNIT-TEST", cafeAPI.VERSION.getCurrentCafeBotVersion());

        // Changes the version number back to the original.
        Assertions.assertTrue(cafeAPI.VERSION.updateCurrentCafeBotVersion(currentVersion));

        // Makes sure the version number has been changed back to the original.
        Assertions.assertEquals(currentVersion, cafeAPI.VERSION.getCurrentCafeBotVersion());
    }

}
