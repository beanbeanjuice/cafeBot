package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.cafebot.voicebinds.VoiceChannelBinds;
import com.beanbeanjuice.cafeapi.cafebot.voicebinds.VoiceChannelBind;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A test class used to test all aspects of the {@link VoiceChannelBinds VoiceChannelBinds} module.
 *
 * @author beanbeanjuice
 */
public class VoiceChannelBindTest {

    @Test
    @DisplayName("Test Voice Channel Binds API")
    public void voiceChannelBindsAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure that the list of voice channel binds contains the guild home guild.
        Assertions.assertTrue(cafeAPI.VOICE_CHANNEL_BIND.getAllVoiceChannelBinds().containsKey("798830792938881024"));

        // Makes sure that the amount of binds in the home guild is 3.
        Assertions.assertTrue(() -> cafeAPI.VOICE_CHANNEL_BIND.getAllVoiceChannelBinds().get("798830792938881024").size() >= 1);

        // Makes sure that the role ID specified is contained in the 2nd bind of voice channel binds in the home guild.
        Assertions.assertEquals("854163089590059048", cafeAPI.VOICE_CHANNEL_BIND.getAllVoiceChannelBinds().get("798830792938881024").get(0).getRoleID());

        // Makes sure that a ConflictException is thrown when trying to add a voice bind that already exists.
        Assertions.assertThrows(ConflictException.class, () -> {
            cafeAPI.VOICE_CHANNEL_BIND.addVoiceChannelBind("798830792938881024", new VoiceChannelBind("995371471230734417", "854163089590059048"));
        });

        // Makes sure that adding a voice bind works.
        Assertions.assertTrue(cafeAPI.VOICE_CHANNEL_BIND.addVoiceChannelBind("798830792938881024", new VoiceChannelBind("854163089590059048", "798830793380069378")));

        // Makes sure that deleting a voice bind works.
        Assertions.assertTrue(cafeAPI.VOICE_CHANNEL_BIND.deleteVoiceChannelBind("798830792938881024", new VoiceChannelBind("854163089590059048", "798830793380069378")));
    }

}
