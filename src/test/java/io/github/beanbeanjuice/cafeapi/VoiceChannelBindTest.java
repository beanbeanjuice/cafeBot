package io.github.beanbeanjuice.cafeapi;

import io.github.beanbeanjuice.cafeapi.cafebot.voicebinds.VoiceChannelBind;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A test class used to test all aspects of the {@link io.github.beanbeanjuice.cafeapi.cafebot.voicebinds.VoiceChannelBinds VoiceChannelBinds} module.
 *
 * @author beanbeanjuice
 */
public class VoiceChannelBindTest {

    @Test
    @DisplayName("Test Voice Channel Binds API")
    public void voiceChannelBindsAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"));

        // Makes sure that the list of voice channel binds contains the guild home guild.
        Assertions.assertTrue(cafeAPI.voiceChannelBinds().getAllVoiceChannelBinds().containsKey("798830792938881024"));

        // Makes sure that the amount of binds in the home guild is 3.
        Assertions.assertEquals(3, cafeAPI.voiceChannelBinds().getAllVoiceChannelBinds().get("798830792938881024").size());

        // Makes sure that the role ID specified is contained in the 2nd bind of voice channel binds in the home guild.
        Assertions.assertEquals("854169730370109491", cafeAPI.voiceChannelBinds().getAllVoiceChannelBinds().get("798830792938881024").get(1).getRoleID());

        // Makes sure that there are 3 voice binds in the home guild.
        Assertions.assertEquals(3, cafeAPI.voiceChannelBinds().getGuildVoiceChannelBinds("798830792938881024").size());

        // Makes sure that a ConflictException is thrown when trying to add a voice bind that already exists.
        Assertions.assertThrows(ConflictException.class, () -> {
            cafeAPI.voiceChannelBinds().addVoiceChannelBind("798830792938881024", new VoiceChannelBind("798830793380069378", "854163089590059048"));
        });

        // Makes sure that adding a voice bind works.
        Assertions.assertTrue(cafeAPI.voiceChannelBinds().addVoiceChannelBind("798830792938881024", new VoiceChannelBind("854163089590059048", "798830793380069378")));

        // Makes sure the total voice binds is now 4.
        Assertions.assertEquals(4, cafeAPI.voiceChannelBinds().getGuildVoiceChannelBinds("798830792938881024").size());

        // Makes sure that deleting a voice bind works.
        Assertions.assertTrue(cafeAPI.voiceChannelBinds().deleteVoiceChannelBind("798830792938881024", new VoiceChannelBind("854163089590059048", "798830793380069378")));

        // Makes sure the total voice binds is now 3.
        Assertions.assertEquals(3, cafeAPI.voiceChannelBinds().getGuildVoiceChannelBinds("798830792938881024").size());
    }

}
