package com.beanbeanjuice.cafeapi.cafebot.voicebinds;

import lombok.Getter;

/**
 * A class used for a {@link VoiceChannelBind} for a Discord Server.
 *
 * @author beanbeanjuice
 */
public class VoiceChannelBind {

    @Getter private final String voiceChannelID;
    @Getter private final String roleID;

    /**
     * Creates a new {@link VoiceChannelBind}.
     * @param voiceChannelID The {@link String voiceChannelID} of the {@link VoiceChannelBind}.
     * @param roleID The {@link String roleID} of the {@link VoiceChannelBind}.
     */
    public VoiceChannelBind(String voiceChannelID, String roleID) {
        this.voiceChannelID = voiceChannelID;
        this.roleID = roleID;
    }

}
