package com.beanbeanjuice.utility.section.twitch;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.exception.api.CafeException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used for handling Twitch instances.
 *
 * @author beanbeanjuice
 */
public class TwitchHandler {

    private static ArrayList<String> alreadyAddedTwitchNames;
    private static TwitchListener twitchListener;
    private static HashMap<String, ArrayList<String>> guildTwitches;  // Twitch Name, Guild IDs

    /**
     * Starts the {@link TwitchHandler}.
     */
    public static void start() {
        alreadyAddedTwitchNames = new ArrayList<>();
        twitchListener = new TwitchListener();
        guildTwitches = new HashMap<>();
        cacheTwitchChannels();

        // Register the event handler.
        twitchListener.addEventHandler(new TwitchMessageEventHandler());
    }

    /**
     * @return The current {@link TwitchListener}.
     */
    public static TwitchListener getTwitchListener() {
        return twitchListener;
    }

    /**
     * Add a twitch channel name.
     * @param twitchUsername The twitch channel name specified.
     */
    public static void addTwitchChannel(@NotNull String twitchUsername) throws HystrixRuntimeException, ContextedRuntimeException {
        twitchUsername = twitchUsername.toLowerCase();

        // If a listener does not already exist, add it.
        if (!alreadyAddedTwitchNames.contains(twitchUsername)) {

            // Check if the channel exists.
            twitchListener.addStream(twitchUsername);
            alreadyAddedTwitchNames.add(twitchUsername);
            Bot.getLogger().log(TwitchHandler.class, LogLevel.DEBUG, "Adding " + twitchUsername);  // TODO: Remove
        }
    }

    /**
     * Add an {@link ArrayList<String>} of twitch channel names.
     * @param guildID The {@link String guildID} of the twitch channels.
     * @param twitchUsernames The {@link ArrayList<String>} specified.
     */
    public static void addTwitchChannels(@NotNull String guildID, @NotNull ArrayList<String> twitchUsernames) {
        for (String string : twitchUsernames) {
            try {
                // If the channel does not exist, remove the twitch discord CHANNEL from the database
                if (!twitchListener.channelExists(string))
                    Bot.getCafeAPI().TWITCH.removeGuildTwitch(guildID, string);
                else
                    addTwitchChannel(string);
            } catch (HystrixRuntimeException | ContextedRuntimeException | UnsupportedOperationException e) {
                Bot.getCafeAPI().TWITCH.removeGuildTwitch(guildID, string);
            }
        }
    }

    /**
     * Retrieve the {@link ArrayList} of {@link String guildID} for the specified {@link String channelName}.
     * @param channelName The specified {@link String channelName}.
     * @return The {@link ArrayList} of {@link String guildID}.
     */
    @NotNull
    public static ArrayList<String> getGuildsForChannel(@NotNull String channelName) {
        channelName = channelName.toLowerCase();

        if (guildTwitches.containsKey(channelName))
            return guildTwitches.get(channelName);

        return new ArrayList<>();
    }

    /**
     * Caches the twitch channels for every {@link net.dv8tion.jda.api.entities.Invite.Guild Guild}.
     *
     * This does not register the events.
     */
    private static void cacheTwitchChannels() {
        try {
            Bot.getCafeAPI().TWITCH.getAllTwitches().forEach((guild, twitchChannels) -> {
                twitchChannels.forEach((twitchChannel) -> {

                    if (Bot.getBot().getGuildById(guild) != null) {
                        if (!guildTwitches.containsKey(twitchChannel))
                            guildTwitches.put(twitchChannel, new ArrayList<>());

                        guildTwitches.get(twitchChannel).add(guild);
                    }

                });
            });
        } catch (CafeException e) {
            Bot.getLogger().log(TwitchHandler.class, LogLevel.ERROR, "Error Getting Twitch Channels: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a link between a {@link String guildID} and a specified {@link String twitchChannel}.
     * @param guildID The {@link String guildID} specified.
     * @param twitchChannel The linked {@link String twitchChannel}.
     * @return True, if the link was successfully created.
     */
    @NotNull
    public static Boolean addCache(@NotNull String guildID, @NotNull String twitchChannel) throws HystrixRuntimeException, ContextedRuntimeException {
        twitchChannel = twitchChannel.toLowerCase();

        // Remove if the channel does not exist.
        if (!twitchListener.channelExists(twitchChannel)) {
            Bot.getCafeAPI().TWITCH.removeGuildTwitch(guildID, twitchChannel);
            return false;
        }

        addTwitchChannel(twitchChannel);
        if (!guildTwitches.containsKey(twitchChannel))
            guildTwitches.put(twitchChannel, new ArrayList<>());

        if (guildTwitches.get(twitchChannel).contains(guildID))
            return false;

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
    public static Boolean removeCache(@NotNull String guildID, @NotNull String twitchChannel) {
        twitchChannel = twitchChannel.toLowerCase();

        if (!guildTwitches.containsKey(twitchChannel))
            return true;

        guildTwitches.get(twitchChannel).remove(guildID);
        return true;
    }

}
