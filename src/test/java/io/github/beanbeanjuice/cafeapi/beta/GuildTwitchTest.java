package io.github.beanbeanjuice.cafeapi.beta;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GuildTwitchTest {

    @Test
    @DisplayName("Guild Twitch API Test")
    public void testGuildTwitchAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure when getting twitch names for a guild that doesn't exist it returns empty.
        Assertions.assertTrue(cafeAPI.guildTwitches().getGuildTwitches("491616686928166912").isEmpty());

        // Making sure that "beanbeanjuice" is contained in the specified guild.
        Assertions.assertEquals("beanbeanjuice", cafeAPI.guildTwitches().getGuildTwitches("798830792938881024").get(1));

        // Makes sure "beanbeanjuice2" can be removed from the specified guild.
        Assertions.assertTrue(cafeAPI.guildTwitches().removeGuildTwitch("798830792938881024", "beanbeanjuice2"));

        // Makes sure "beanbeanjuice2" can be added to the specified guild.
        Assertions.assertTrue(cafeAPI.guildTwitches().addGuildTwitch("798830792938881024", "beanbeanjuice2"));

        // Makes sure "beanbeanjuice2" CANNOT be added to the specified guild.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.guildTwitches().addGuildTwitch("798830792938881024", "beanbeanjuice2"));

        // Makes sure "beanbeanjuice2" can be retrieved for the specified guild.
        Assertions.assertTrue(cafeAPI.guildTwitches().getAllTwitches().get("798830792938881024").contains("beanbeanjuice2"));
    }

}
