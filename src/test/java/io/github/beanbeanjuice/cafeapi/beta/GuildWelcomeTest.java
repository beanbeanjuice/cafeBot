package io.github.beanbeanjuice.cafeapi.beta;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.cafebot.welcomes.GuildWelcome;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A test class used to test all aspects of the {@link io.github.beanbeanjuice.cafeapi.cafebot.welcomes.Welcomes Welcomes} module.
 *
 * @author beanbeanjuice
 */
public class GuildWelcomeTest {

    @Test
    @DisplayName("Test Guild Welcomes API")
    public void guildWelcomesAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure that the amount of guilds in the guild's welcome is more than 0.
        Assertions.assertTrue(cafeAPI.welcomes().getAllGuildWelcomes().size() > 0);

        // Makes sure that the current welcome is set properly.
        Assertions.assertTrue(cafeAPI.welcomes().updateGuildWelcome(new GuildWelcome(
                "798830792938881024",
                "Welcome, {user} to the bot testing server!\\nI hope you enjoy your stay! :heart:\\nMake sure to...\\n Check out #polls,\\nCheck out #logger,\\n and Check out #cafebot-beta-log!",
                "https://i.pinimg.com/originals/3f/33/75/3f3375eaef9ed7529d0e1bb5b63a814a.gif",
                "https://i.pinimg.com/originals/3f/33/75/3f3375eaef9ed7529d0e1bb5b63a814a.gif",
                null
        )));

        // Makes sure that the first guild is equal to the home guild ID.
        Assertions.assertEquals("798830792938881024", cafeAPI.welcomes().getAllGuildWelcomes().get(0).getGuildID());

        // Makes sure that the description of the retrieved guild is "test description"
        Assertions.assertEquals(
                "Welcome, {user} to the bot testing server!\\nI hope you enjoy your stay! :heart:\\nMake sure to...\\n Check out #polls,\\nCheck out #logger,\\n and Check out #cafebot-beta-log!",
                cafeAPI.welcomes().getGuildWelcome("798830792938881024").getDescription());

        // Makes sure that this throws a NotFoundException as the guild "bruhmoment" does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.welcomes().getGuildWelcome("bruhmoment");
        });

        // Makes sure that the guild can be updated.
        Assertions.assertTrue(() -> {
            GuildWelcome currentGuildWelcome = cafeAPI.welcomes().getGuildWelcome("798830792938881024");

            GuildWelcome newGuildWelcome = new GuildWelcome(
                    currentGuildWelcome.getGuildID(),
                    currentGuildWelcome.getDescription(),
                    currentGuildWelcome.getThumbnailURL(),
                    currentGuildWelcome.getImageURL(),
                    "cool message!!!!"
            );

            return cafeAPI.welcomes().updateGuildWelcome(newGuildWelcome);
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

            cafeAPI.welcomes().createGuildWelcome(guildWelcome);
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

            return cafeAPI.welcomes().createGuildWelcome(guildWelcome);
        });

        // Is true when a guild welcome is successfully deleted.
        Assertions.assertTrue(cafeAPI.welcomes().deleteGuildWelcome("236331890964037632"));
    }

}
