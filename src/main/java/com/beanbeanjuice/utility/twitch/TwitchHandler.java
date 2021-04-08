package com.beanbeanjuice.utility.twitch;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * A class used for handling {@link Twitch} instances.
 *
 * @author beanbeanjuice
 */
public class TwitchHandler {

    private HashMap<String, Twitch> twitchListenerDatabase;

    /**
     * Creates a new {@link TwitchHandler} instance.
     */
    public TwitchHandler() {
        twitchListenerDatabase = new HashMap<>();
    }

    /**
     * Adds a {@link Twitch} instance to the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param twitch Thw {@link Twitch} instance associated with the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @return Whether or not it was added successfully.
     */
    @NotNull
    public Boolean addTwitchToGuild(@NotNull String guildID, @NotNull Twitch twitch) {
        if (twitchListenerDatabase.get(guildID) == null) {
            twitchListenerDatabase.put(guildID, twitch);
            return true;
        }
        return false;
    }

    /**
     * Gets the {@link Twitch} instance associated with the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @return The {@link Twitch} instance associated with the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     */
    @Nullable
    public Twitch getTwitch(@NotNull String guildID) {
        return twitchListenerDatabase.get(guildID);
    }

}
