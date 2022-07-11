package com.beanbeanjuice.utility.section.twitch;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

/**
 * A class for handling Twitch events.
 *
 * @author beanbeanjuice
 */
public class TwitchListener {

    private final TwitchClient twitchClient;

    /**
     * Creates a new {@link TwitchListener} object.
     */
    public TwitchListener() {
        twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withDefaultAuthToken(new OAuth2Credential("twitch", Bot.TWITCH_ACCESS_TOKEN))
                .withDefaultEventHandler(TwitchMessageEventHandler.class)
                .build();
    }

    /**
     * Adds a stream to listen for.
     * @param channelName The channel name of the stream to listen for.
     */
    public void addStream(String channelName) {
        try {
            twitchClient.getClientHelper().enableStreamEventListener(channelName);
        } catch (Exception e) {
            Bot.getLogger().log(TwitchListener.class, LogLevel.ERROR, "Error Adding: " + channelName, false, false, e);
        }
    }

    /**
     * Removes a stream to listen for.
     * @param channelName The channel name of the stream to stop listening for.
     */
    public void removeStream(String channelName) {
        twitchClient.getClientHelper().disableStreamEventListener(channelName);
    }

    /**
     * Adds an event handler for listening for channels.
     * @param eventListener The event handler to add.
     */
    public void addEventHandler(SimpleEventHandler eventListener) {
        twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(eventListener);
    }

    /**
     * @return The current {@link TwitchClient}.
     */
    public TwitchClient getTwitchClient() {
        return twitchClient;
    }

}
