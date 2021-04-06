package com.beanbeanjuice.utility.listener.twitch;

import com.beanbeanjuice.utility.guild.CustomGuild;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TwitchChannelNamesHandler {

    private ArrayList<String> twitchChannelNames;
    private CustomGuild customGuild;

    /**
     * Creates a new instance of the {@link TwitchChannelNamesHandler} object.
     */
    public TwitchChannelNamesHandler(CustomGuild customGuild) {
        this.customGuild = customGuild;
        twitchChannelNames = new ArrayList<>();

        for (String twitchChannelName : getTwitchChannelNames()) {
            customGuild.getTwitchListener().addStream(twitchChannelName);
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



        // TODO: Eventually use a SQL database.
        twitchChannelNames.add(twitchChannelName.toLowerCase());
        customGuild.getTwitchListener().addStream(twitchChannelName);
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

                // TODO: Eventually use a SQL database.
                twitchChannelNames.remove(twitchChannelName.toLowerCase());
                customGuild.getTwitchListener().removeStream(twitchChannelName);
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
        // TODO: Eventually use a SQL database.
        return twitchChannelNames;
    }
}