package com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds;

import java.io.InvalidClassException;
import java.util.HashMap;

/**
 * A class used for {@link GuildInformation}.
 *
 * @author beanbeanjuice
 */
public class GuildInformation {

    private final HashMap<GuildInformationType, String> guildSettings;

    /**
     * Creates a new {@link GuildInformation} object.
     * @param guildSettings A {@link HashMap} containing the {@link GuildInformationType} as keys and {@link Object settings} as values.
     *
     * @throws NullPointerException When there is a mismatch between provided hashmap and guild information types.
     */
    public GuildInformation(HashMap<GuildInformationType, String> guildSettings) throws NullPointerException {
        if (guildSettings.size() != GuildInformationType.values().length) throw new NullPointerException("Mismatch between provided hashmap and guild information types.");

        this.guildSettings = guildSettings;
    }

    public String getSetting(GuildInformationType type) {
        return guildSettings.get(type);
    }

}
