package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.welcomes.WelcomesEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.welcomes.GuildWelcome;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A test class used to test all aspects of the {@link WelcomesEndpoint Welcomes} module.
 *
 * @author beanbeanjuice
 */
public class GuildWelcomeTest {

    @Test
    @DisplayName("Welcomes Endpoint Test")
    public void testWelcomesEndpoint() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure that the amount of guilds in the guild's welcome is more than 0.
        Assertions.assertTrue(cafeAPI.getWelcomesEndpoint().getAllGuildWelcomes().size() > 0);

        // Makes sure that the current welcome is set properly.
        Assertions.assertTrue(cafeAPI.getWelcomesEndpoint().updateGuildWelcome(new GuildWelcome(
                "798830792938881024",
                "Welcome, {user} to the bot testing server!\\nI hope you enjoy your stay! :heart:\\nMake sure to...\\n Check out #polls,\\nCheck out #logger,\\n and Check out #cafebot-beta-log!",
                "https://i.pinimg.com/originals/3f/33/75/3f3375eaef9ed7529d0e1bb5b63a814a.gif",
                "https://i.pinimg.com/originals/3f/33/75/3f3375eaef9ed7529d0e1bb5b63a814a.gif",
                null
        )));

        // Makes sure that the first guild is equal to the home guild ID.
        Assertions.assertEquals("798830792938881024", cafeAPI.getWelcomesEndpoint().getAllGuildWelcomes().get(0).getGuildID());

        // Makes sure that the description of the retrieved guild is "test description"
        Assertions.assertEquals(
                "Welcome, {user} to the bot testing server!\\nI hope you enjoy your stay! :heart:\\nMake sure to...\\n Check out #polls,\\nCheck out #logger,\\n and Check out #cafebot-beta-log!",
                cafeAPI.getWelcomesEndpoint().getGuildWelcome("798830792938881024").getDescription().orElse(null));

        // Makes sure that this throws a NotFoundException as the guild "bruhmoment" does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.getWelcomesEndpoint().getGuildWelcome("bruhmoment");
        });

        // Makes sure that the guild can be updated.
        Assertions.assertTrue(() -> {
            GuildWelcome currentGuildWelcome = cafeAPI.getWelcomesEndpoint().getGuildWelcome("798830792938881024");

            GuildWelcome newGuildWelcome = new GuildWelcome(
                    currentGuildWelcome.getGuildID(),
                    currentGuildWelcome.getDescription().orElse(null),
                    currentGuildWelcome.getThumbnailURL().orElse(null),
                    currentGuildWelcome.getImageURL().orElse(null),
                    "cool message!!!!"
            );

            return cafeAPI.getWelcomesEndpoint().updateGuildWelcome(newGuildWelcome);
        });

        // Throws a conflict exception because the guild welcome cannot be created because it already exists.
        Assertions.assertThrows(ConflictException.class, () -> {
            GuildWelcome guildWelcome = new GuildWelcome(
                    "798830792938881024",
                    "COOL desc",
                    "cool url",
                    "cool image",
                    null
            );

            cafeAPI.getWelcomesEndpoint().createGuildWelcome(guildWelcome);
        });

        // Makes sure that a guild can be created.
        Assertions.assertTrue(() -> {
            GuildWelcome guildWelcome = new GuildWelcome(
                    "236331890964037632",
                    "COOL desc",
                    "cool url",
                    "cool image",
                    null
            );

            return cafeAPI.getWelcomesEndpoint().createGuildWelcome(guildWelcome);
        });

        // Is true when a guild welcome is successfully deleted.
        Assertions.assertTrue(cafeAPI.getWelcomesEndpoint().deleteGuildWelcome("236331890964037632"));
    }

}
