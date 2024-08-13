package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.TeaPotException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class VersionsEndpointTests {

    @Test
    @DisplayName("Bot Version Endpoint Test")
    public void testVersionsEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Gets the current version of cafeBot.
        String currentVersion = cafeAPI.getVersionsEndpoint().getCurrentCafeBotVersion().get();

        // Makes sure the current version is not null.
        Assertions.assertNotNull(currentVersion);

        // Makes sure a TeaPotException is thrown when the user forgets to add a "v" to the beginning of the version number.
        Assertions.assertThrows(TeaPotException.class, () -> cafeAPI.getVersionsEndpoint().updateCurrentCafeBotVersion("2.0.0-UNIT-TEST").get());

        // Makes sure the version number for cafeBot can be updated.
        Assertions.assertTrue(cafeAPI.getVersionsEndpoint().updateCurrentCafeBotVersion("v2.0.0-UNIT-TEST").get());

        // Makes sure the version number has been changed.
        Assertions.assertEquals("v2.0.0-UNIT-TEST", cafeAPI.getVersionsEndpoint().getCurrentCafeBotVersion().get());

        // Changes the version number back to the original.
        Assertions.assertTrue(cafeAPI.getVersionsEndpoint().updateCurrentCafeBotVersion(currentVersion).get());

        // Makes sure the version number has been changed back to the original.
        Assertions.assertEquals(currentVersion, cafeAPI.getVersionsEndpoint().getCurrentCafeBotVersion().get());
    }

}
