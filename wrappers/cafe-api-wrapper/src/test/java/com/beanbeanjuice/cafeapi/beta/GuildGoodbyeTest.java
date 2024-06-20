package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.cafebot.goodbyes.GuildGoodbye;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A test class used to test all aspects of the {@link com.beanbeanjuice.cafeapi.cafebot.goodbyes.Goodbyes} module.
 *
 * @author beanbeanjuice
 */
public class GuildGoodbyeTest {

    @Test
    @DisplayName("Test Guild Goodbyes API")
    public void guildGoodbyesAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure that the amount of guilds in the guild's goodbye is more than 0.
        Assertions.assertTrue(cafeAPI.GOODBYE.getAllGuildGoodbyes().size() > 0);

        // Makes sure that the current goodbye is set properly.
        Assertions.assertTrue(cafeAPI.GOODBYE.updateGuildGoodbye(new GuildGoodbye(
                "798830792938881024",
                "Goodbye... {user}... thanks for joining!",
                "https://i.pinimg.com/originals/3f/33/75/3f3375eaef9ed7529d0e1bb5b63a814a.gif",
                "https://i.pinimg.com/originals/3f/33/75/3f3375eaef9ed7529d0e1bb5b63a814a.gif",
                null
        )));

        // Makes sure that the first guild is equal to the home guild ID.
        Assertions.assertEquals("798830792938881024", cafeAPI.GOODBYE.getAllGuildGoodbyes().get(0).getGuildID());

        // Makes sure that the description of the retrieved guild is "test description"
        Assertions.assertEquals(
                "Goodbye... {user}... thanks for joining!",
                cafeAPI.GOODBYE.getGuildGoodbye("798830792938881024").getDescription().orElse(null));

        // Makes sure that this throws a NotFoundException as the guild "bruhmoment" does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.GOODBYE.getGuildGoodbye("bruhmoment");
        });

        // Makes sure that the guild can be updated.
        Assertions.assertTrue(() -> {
            GuildGoodbye currentGuildGoodbye = cafeAPI.GOODBYE.getGuildGoodbye("798830792938881024");

            GuildGoodbye newGuildGoodbye = new GuildGoodbye(
                    currentGuildGoodbye.getGuildID(),
                    currentGuildGoodbye.getDescription().orElse(null),
                    currentGuildGoodbye.getThumbnailURL().orElse(null),
                    currentGuildGoodbye.getImageURL().orElse(null),
                    "cool message!!!!"
            );

            return cafeAPI.GOODBYE.updateGuildGoodbye(newGuildGoodbye);
        });

        // Throws a conflict exception because the guild goodbye cannot be created because it already exists.
        Assertions.assertThrows(ConflictException.class, () -> {
            GuildGoodbye guildGoodbye = new GuildGoodbye(
                    "798830792938881024",
                    "COOL desc",
                    "cool url",
                    "cool image",
                    null
            );

            cafeAPI.GOODBYE.createGuildGoodbye(guildGoodbye);
        });

        // Makes sure that a guild can be created.
        Assertions.assertTrue(() -> {
            GuildGoodbye guildGoodbye = new GuildGoodbye(
                    "236331890964037632",
                    "COOL desc",
                    "cool url",
                    "cool image",
                    null
            );

            return cafeAPI.GOODBYE.createGuildGoodbye(guildGoodbye);
        });

        // Is true when a guild goodbye is successfully deleted.
        Assertions.assertTrue(cafeAPI.GOODBYE.deleteGuildGoodbye("236331890964037632"));
    }

}
