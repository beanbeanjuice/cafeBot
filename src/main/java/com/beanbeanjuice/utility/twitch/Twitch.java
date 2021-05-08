package com.beanbeanjuice.utility.twitch;

import com.beanbeanjuice.utility.listener.TwitchListener;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A {@link Twitch} instance associated with each {@link com.beanbeanjuice.utility.guild.CustomGuild CustomGuild}.
 *
 * @author beanbeanjuice
 */
public class Twitch {

    private TwitchListener twitchListener;
    private TwitchChannelNamesHandler twitchChannelNamesHandler;

    /**
     * Creates a new {@link Twitch} instance.
     * @param guildID The ID of the {@link Guild}.
     * @param twitchChannels The {@link ArrayList<String>} of Twitch Channels.
     */
    public Twitch(@NotNull String guildID, @NotNull ArrayList<String> twitchChannels) {
        twitchListener = new TwitchListener();
        twitchChannelNamesHandler = new TwitchChannelNamesHandler(this);

        twitchListener.addEventHandler(new TwitchMessageEventHandler(guildID));

        if (!twitchChannels.isEmpty()) {
            for (String channel : twitchChannels) {
                twitchChannelNamesHandler.addTwitchChannelName(channel);
            }
        }
    }

    /**
     * @return The {@link TwitchListener} for the specified {@link Guild}.
     */
    @NotNull
    public TwitchListener getTwitchListener() {
        return twitchListener;
    }

    /**
     * @return The {@link TwitchChannelNamesHandler} for the specified {@link Guild}.
     */
    @NotNull
    public TwitchChannelNamesHandler getTwitchChannelNamesHandler() {
        return twitchChannelNamesHandler;
    }

}
