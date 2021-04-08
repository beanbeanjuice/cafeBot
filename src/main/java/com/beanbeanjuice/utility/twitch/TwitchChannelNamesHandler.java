package com.beanbeanjuice.utility.twitch;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TwitchChannelNamesHandler {

    private ArrayList<String> twitchChannelNames;
    private Twitch twitch;

    /**
     * Creates a new instance of the {@link TwitchChannelNamesHandler} object.
     */
    public TwitchChannelNamesHandler(Twitch twitch) {
        this.twitch = twitch;
        twitchChannelNames = new ArrayList<>();

        for (String twitchChannelName : getTwitchChannelNames()) {
            twitch.getTwitchListener().addStream(twitchChannelName);
        }
    }

    /**
     * Adds a Twitch channel to be listened for.
     * @param twitchChannelName The Twitch channel name.
     * @return Whether or not it was successfully added.
     */
    @NotNull
    public Boolean addTwitchChannelName(@NotNull String twitchChannelName) {

        for (String string : getTwitchChannelNames()) {
            if (string.equalsIgnoreCase(twitchChannelName)) {
                return false;
            }
        }

        twitchChannelNames.add(twitchChannelName.toLowerCase());
        twitch.getTwitchListener().addStream(twitchChannelName);
        return true;
    }

    /**
     * Removes a Twitch channel that is being listened to.
     * @param twitchChannelName The Twitch channel name.
     * @return Whether or not it was successfully removed.
     */
    @NotNull
    public Boolean removeTwitchChannelName(@NotNull String twitchChannelName) {

        for (String string : getTwitchChannelNames()) {
            if (string.equalsIgnoreCase(twitchChannelName)) {

                twitchChannelNames.remove(twitchChannelName.toLowerCase());
                twitch.getTwitchListener().removeStream(twitchChannelName);
                return true;

            }
        }

        return false;

    }

    /**
     * @return The {@link ArrayList<String>} of Twitch Channel names.
     */
    @NotNull
    public ArrayList<String> getTwitchChannelNames() {
        return twitchChannelNames;
    }
}