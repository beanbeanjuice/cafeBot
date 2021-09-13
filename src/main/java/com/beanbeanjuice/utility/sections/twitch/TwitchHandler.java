package com.beanbeanjuice.utility.sections.twitch;

import com.beanbeanjuice.CafeBot;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import com.beanbeanjuice.utility.listener.TwitchListener;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used for handling Twitch instances.
 *
 * @author beanbeanjuice
 */
public class TwitchHandler {

    private final ArrayList<String> alreadyAddedTwitchNames;
    private final TwitchListener twitchListener;

    private final HashMap<String, ArrayList<String>> guildTwitches;

    /**
     * Creates a new {@link TwitchHandler} instance.
     */
    public TwitchHandler() {
        alreadyAddedTwitchNames = new ArrayList<>();
        twitchListener = new TwitchListener();
        guildTwitches = new HashMap<>();
        cacheTwitchChannels();
        twitchListener.addEventHandler(new TwitchMessageEventHandler());
    }

    /**
     * Add a twitch channel name.
     * @param twitchUsername The twitch channel name specified.
     */
    public void addTwitchChannel(@NotNull String twitchUsername) {
        twitchUsername = twitchUsername.toLowerCase();
        if (!alreadyAddedTwitchNames.contains(twitchUsername)) {
            twitchListener.addStream(twitchUsername);
            alreadyAddedTwitchNames.add(twitchUsername);
        }
    }

    /**
     * Add an {@link ArrayList<String>} of twitch channel names.
     * @param twitchUsernames The {@link ArrayList<String>} specified.
     */
    public void addTwitchChannels(@NotNull ArrayList<String> twitchUsernames) {
        for (String string : twitchUsernames) {
            addTwitchChannel(string);
        }
    }

    /**
     * Retrieve the {@link ArrayList} of {@link String guildID} for the specified {@link String channelName}.
     * @param channelName The specified {@link String channelName}.
     * @return The {@link ArrayList} of {@link String guildID}.
     */
    @NotNull
    public ArrayList<String> getGuildsForChannel(@NotNull String channelName) {
        channelName = channelName.toLowerCase();

        if (guildTwitches.containsKey(channelName)) {
            return guildTwitches.get(channelName);
        }

        return new ArrayList<>();
    }

    /**
     * Caches the twitch channels for every {@link net.dv8tion.jda.api.entities.Invite.Guild Guild}.
     */
    private void cacheTwitchChannels() {
        try {
            CafeBot.getCafeAPI().guildTwitches().getAllTwitches().forEach((guild, twitchChannels) -> {
                twitchChannels.forEach((twitchChannel) -> {
                    if (!guildTwitches.containsKey(twitchChannel)) {
                        guildTwitches.put(twitchChannel, new ArrayList<>());
                    }

                    guildTwitches.get(twitchChannel).add(guild);
                });
            });
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Getting Twitch Channels: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a link between a {@link String guildID} and a specified {@link String twitchChannel}.
     * @param guildID The {@link String guildID} specified.
     * @param twitchChannel The linked {@link String twitchChannel}.
     * @return True, if the link was successfully created.
     */
    @NotNull
    public Boolean addCache(@NotNull String guildID, @NotNull String twitchChannel) {
        twitchChannel = twitchChannel.toLowerCase();
        addTwitchChannel(twitchChannel);
        if (!guildTwitches.containsKey(twitchChannel)) {
            guildTwitches.put(twitchChannel, new ArrayList<>());
        }

        if (guildTwitches.get(twitchChannel).contains(guildID)) {
            return false;
        }

        guildTwitches.get(twitchChannel).add(guildID);
        return true;
    }

    /**
     * Removes the link between a specified {@link String guildID} and a specified {@link String twitchChannel}.
     * @param guildID The {@link String guildID} specified.
     * @param twitchChannel The linked {@link String twitchChannel}.
     * @return True, if the link was successfully removed.
     */
    @NotNull
    public Boolean removeCache(@NotNull String guildID, @NotNull String twitchChannel) {
        twitchChannel = twitchChannel.toLowerCase();

        if (!guildTwitches.containsKey(twitchChannel)) {
            return true;
        }

        guildTwitches.get(twitchChannel).remove(guildID);
        return true;
    }

}
