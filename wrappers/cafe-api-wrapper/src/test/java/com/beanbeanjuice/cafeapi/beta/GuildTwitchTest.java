package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GuildTwitchTest {

    @Test
    @DisplayName("Guild Twitch API Test")
    public void testGuildTwitchAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure when getting twitch names for a guild that doesn't exist it returns empty.
        Assertions.assertTrue(cafeAPI.TWITCH.getGuildTwitches("491616686928166912").isEmpty());

        // Making sure that "beanbeanjuice" is contained in the specified guild.
        Assertions.assertTrue(cafeAPI.TWITCH.getGuildTwitches("798830792938881024").contains("beanbeanjuice"));

        // Makes sure "beanbeanjuice2" can be removed from the specified guild.
        Assertions.assertTrue(cafeAPI.TWITCH.removeGuildTwitch("798830792938881024", "beanbeanjuice2"));

        // Makes sure "beanbeanjuice2" can be added to the specified guild.
        Assertions.assertTrue(cafeAPI.TWITCH.addGuildTwitch("798830792938881024", "beanbeanjuice2"));

        // Makes sure "beanbeanjuice2" CANNOT be added to the specified guild.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.TWITCH.addGuildTwitch("798830792938881024", "beanbeanjuice2"));

        // Makes sure "beanbeanjuice2" can be retrieved for the specified guild.
        Assertions.assertTrue(cafeAPI.TWITCH.getAllTwitches().get("798830792938881024").contains("beanbeanjuice2"));
    }

}
