package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.goodbyes.GoodbyesEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.goodbyes.GuildGoodbye;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

/**
 * A test class used to test all aspects of the {@link GoodbyesEndpoint} module.
 *
 * @author beanbeanjuice
 */
public class GoodbyesEndpointTests {

    @Test
    @DisplayName("Goodbyes Endpoint Test")
    public void testGoodbyesEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure that the amount of guilds in the guild's goodbye is more than 0.
        Assertions.assertFalse(cafeAPI.getGoodbyesEndpoint().getAllGuildGoodbyes().get().isEmpty());

        // Makes sure that the current goodbye is set properly.
        Assertions.assertTrue(cafeAPI.getGoodbyesEndpoint().updateGuildGoodbye(new GuildGoodbye(
                "798830792938881024",
                "Goodbye... {user}... thanks for joining!",
                "https://i.pinimg.com/originals/3f/33/75/3f3375eaef9ed7529d0e1bb5b63a814a.gif",
                "https://i.pinimg.com/originals/3f/33/75/3f3375eaef9ed7529d0e1bb5b63a814a.gif",
                null
        )).get());

        // Makes sure that the first guild is equal to the home guild ID.
        Assertions.assertEquals("798830792938881024", cafeAPI.getGoodbyesEndpoint().getAllGuildGoodbyes().get().getFirst().getGuildID());

        // Makes sure that the description of the retrieved guild is "test description"
        Assertions.assertEquals(
                "Goodbye... {user}... thanks for joining!",
                cafeAPI.getGoodbyesEndpoint().getGuildGoodbye("798830792938881024").get().getDescription().orElse(null));

        // Makes sure that this throws a NotFoundException as the guild "bruhmoment" does not exist.
        try {
            cafeAPI.getGoodbyesEndpoint().getGuildGoodbye("bruhmoment").get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(NotFoundException.class, e.getCause());
        }

        // Makes sure that the guild can be updated.
        Assertions.assertTrue(() -> {
            GuildGoodbye currentGuildGoodbye = null;
            try {
                currentGuildGoodbye = cafeAPI.getGoodbyesEndpoint().getGuildGoodbye("798830792938881024").get();
            } catch (Exception e) {
                Assertions.fail();
            }

            GuildGoodbye newGuildGoodbye = new GuildGoodbye(
                    currentGuildGoodbye.getGuildID(),
                    currentGuildGoodbye.getDescription().orElse(null),
                    currentGuildGoodbye.getThumbnailURL().orElse(null),
                    currentGuildGoodbye.getImageURL().orElse(null),
                    "cool message!!!!"
            );

            try {
                return cafeAPI.getGoodbyesEndpoint().updateGuildGoodbye(newGuildGoodbye).get();
            } catch (Exception e) {
                Assertions.fail();
                return false;
            }
        });

        // Throws a conflict exception because the guild goodbye cannot be created because it already exists.
        try {
            GuildGoodbye guildGoodbye = new GuildGoodbye(
                    "798830792938881024",
                    "COOL desc",
                    "cool url",
                    "cool image",
                    null
            );

            cafeAPI.getGoodbyesEndpoint().createGuildGoodbye(guildGoodbye).get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(ConflictException.class, e.getCause());
        }

        // Makes sure that a guild can be created.
        Assertions.assertTrue(() -> {
            GuildGoodbye guildGoodbye = new GuildGoodbye(
                    "236331890964037632",
                    "COOL desc",
                    "cool url",
                    "cool image",
                    null
            );

            try {
                return cafeAPI.getGoodbyesEndpoint().createGuildGoodbye(guildGoodbye).get();
            } catch (Exception e) {
                Assertions.fail();
                return false;
            }
        });

        // Is true when a guild goodbye is successfully deleted.
        Assertions.assertTrue(cafeAPI.getGoodbyesEndpoint().deleteGuildGoodbye("236331890964037632").get());
    }

}
