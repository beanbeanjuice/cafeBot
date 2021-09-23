package io.github.beanbeanjuice.cafeapi.release;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.exception.TeaPotException;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BotVersionTest {

    @Test
    @DisplayName("Bot Version API Test")
    public void testBotVersionAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("RELEASE_API_PASSWORD"), RequestLocation.RELEASE);

        // Gets the current version of cafeBot.
        String currentVersion = cafeAPI.versions().getCurrentCafeBotVersion();

        // Makes sure the current version is not null.
        Assertions.assertNotNull(currentVersion);

        // Makes sure a TeaPotException is thrown when the user forgets to add a "v" to the beginning of the version number.
        Assertions.assertThrows(TeaPotException.class, () -> cafeAPI.versions().updateCurrentCafeBotVersion("2.0.0-UNIT-TEST"));

        // Makes sure the version number for cafeBot can be updated.
        Assertions.assertTrue(cafeAPI.versions().updateCurrentCafeBotVersion("v2.0.0-UNIT-TEST"));

        // Makes sure the version number has been changed.
        Assertions.assertEquals("v2.0.0-UNIT-TEST", cafeAPI.versions().getCurrentCafeBotVersion());

        // Changes the version number back to the original.
        Assertions.assertTrue(cafeAPI.versions().updateCurrentCafeBotVersion(currentVersion));

        // Makes sure the version number has been changed back to the original.
        Assertions.assertEquals(currentVersion, cafeAPI.versions().getCurrentCafeBotVersion());
    }

}
