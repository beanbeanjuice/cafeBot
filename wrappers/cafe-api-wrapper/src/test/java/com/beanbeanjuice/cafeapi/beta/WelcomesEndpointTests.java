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

import java.util.concurrent.ExecutionException;

/**
 * A test class used to test all aspects of the {@link WelcomesEndpoint Welcomes} module.
 *
 * @author beanbeanjuice
 */
public class WelcomesEndpointTests {

    @Test
    @DisplayName("Welcomes Endpoint Test")
    public void testWelcomesEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure that the amount of guilds in the guild's welcome is more than 0.
        Assertions.assertFalse(cafeAPI.getWelcomesEndpoint().getAllGuildWelcomes().get().isEmpty());

        // Makes sure that the current welcome is set properly.
        Assertions.assertTrue(cafeAPI.getWelcomesEndpoint().updateGuildWelcome(new GuildWelcome(
                "798830792938881024",
                "Welcome, {user} to the bot testing server!\\nI hope you enjoy your stay! :heart:\\nMake sure to...\\n Check out #polls,\\nCheck out #logger,\\n and Check out #cafebot-beta-log!",
                "https://i.pinimg.com/originals/3f/33/75/3f3375eaef9ed7529d0e1bb5b63a814a.gif",
                "https://i.pinimg.com/originals/3f/33/75/3f3375eaef9ed7529d0e1bb5b63a814a.gif",
                null
        )).get());

        // Makes sure that the first guild is equal to the home guild ID.
        Assertions.assertEquals("798830792938881024", cafeAPI.getWelcomesEndpoint().getAllGuildWelcomes().get().getFirst().getGuildID());

        // Makes sure that the description of the retrieved guild is "test description"
        Assertions.assertEquals(
                "Welcome, {user} to the bot testing server!\\nI hope you enjoy your stay! :heart:\\nMake sure to...\\n Check out #polls,\\nCheck out #logger,\\n and Check out #cafebot-beta-log!",
                cafeAPI.getWelcomesEndpoint().getGuildWelcome("798830792938881024").get().getDescription().orElse(null));

        // Makes sure that this throws a NotFoundException as the guild "bruhmoment" does not exist.
        cafeAPI.getWelcomesEndpoint().getGuildWelcome("bruhmoment")
                .thenApplyAsync((guildWelcome) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(NotFoundException.class, exception.getCause());
                    return null;
                }).join();

        // Makes sure that the guild can be updated.
        cafeAPI.getWelcomesEndpoint().getGuildWelcome("798830792938881024").thenAcceptAsync((currentGuildWelcome) -> {
            GuildWelcome newGuildWelcome = new GuildWelcome(
                    currentGuildWelcome.getGuildID(),
                    currentGuildWelcome.getDescription().orElse(null),
                    currentGuildWelcome.getThumbnailURL().orElse(null),
                    currentGuildWelcome.getImageURL().orElse(null),
                    "cool message!!!!"
            );

            cafeAPI.getWelcomesEndpoint().updateGuildWelcome(newGuildWelcome)
                    .thenAcceptAsync(Assertions::assertTrue)
                    .exceptionallyAsync(Assertions::fail)
                    .join();
        });

        // Throws a conflict exception because the guild welcome cannot be created because it already exists.
        GuildWelcome guildWelcome = new GuildWelcome(
                "798830792938881024",
                "COOL desc",
                "cool url",
                "cool image",
                null
        );
        cafeAPI.getWelcomesEndpoint().createGuildWelcome(guildWelcome)
                .thenAcceptAsync((isSuccess) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(ConflictException.class, exception.getCause());
                    return null;
                }).join();

        // Makes sure that a guild can be created.
        cafeAPI.getWelcomesEndpoint().createGuildWelcome(new GuildWelcome(
                        "236331890964037632",
                        "COOL desc",
                        "cool url",
                        "cool image",
                        null
                )).thenAcceptAsync(Assertions::assertTrue)
                .exceptionallyAsync(Assertions::fail)
                .join();

        // Is true when a guild welcome is successfully deleted.
        Assertions.assertTrue(cafeAPI.getWelcomesEndpoint().deleteGuildWelcome("236331890964037632").get());
    }

}
