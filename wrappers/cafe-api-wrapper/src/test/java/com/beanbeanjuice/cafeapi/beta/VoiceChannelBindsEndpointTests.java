package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.voicebinds.VoiceChannelBindsEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.voicebinds.VoiceChannelBind;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

/**
 * A test class used to test all aspects of the {@link VoiceChannelBindsEndpoint VoiceChannelBinds} module.
 *
 * @author beanbeanjuice
 */
public class VoiceChannelBindsEndpointTests {

    @Test
    @DisplayName("Voice Channel Binds Endpoint")
    public void testVoiceChannelBindEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure that the list of voice channel binds contains the guild home guild.
        Assertions.assertTrue(cafeAPI.getVoiceChannelBindsEndpoint().getAllVoiceChannelBinds().get().containsKey("798830792938881024"));

        // Makes sure that the amount of binds in the home guild is 3.
        Assertions.assertTrue(() -> {
            try {
                return !cafeAPI.getVoiceChannelBindsEndpoint().getAllVoiceChannelBinds().get().get("798830792938881024").isEmpty();
            } catch (Exception e) {
                Assertions.fail(e);
                return false;
            }
        });

        // Makes sure that the role ID specified is contained in the 2nd bind of voice channel binds in the home guild.
        Assertions.assertEquals("854163089590059048", cafeAPI.getVoiceChannelBindsEndpoint().getAllVoiceChannelBinds().get().get("798830792938881024").get(0).getRoleID());

        // Makes sure that a ConflictException is thrown when trying to add a voice bind that already exists.
        cafeAPI.getVoiceChannelBindsEndpoint().addVoiceChannelBind("798830792938881024", new VoiceChannelBind("995371471230734417", "854163089590059048"))
                .thenAcceptAsync((isSuccess) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(ConflictException.class, exception.getCause());
                    return null;
                }).join();

        // Makes sure that adding a voice bind works.
        Assertions.assertTrue(cafeAPI.getVoiceChannelBindsEndpoint().addVoiceChannelBind("798830792938881024", new VoiceChannelBind("854163089590059048", "798830793380069378")).get());

        // Makes sure that deleting a voice bind works.
        Assertions.assertTrue(cafeAPI.getVoiceChannelBindsEndpoint().deleteVoiceChannelBind("798830792938881024", new VoiceChannelBind("854163089590059048", "798830793380069378")).get());
    }

}
