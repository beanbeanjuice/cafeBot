package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.CafeBot;
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

    private TwitchClient twitchClient;

    /**
     * Creates a new {@link TwitchListener} object.
     */
    public TwitchListener() {
        twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withEnableChat(true)
                .withDefaultAuthToken(new OAuth2Credential("twitch", CafeBot.getTwitchAccessToken()))
                .withDefaultEventHandler(SimpleEventHandler.class)
                .build();
    }

    /**
     * Adds a stream to listen for.
     * @param channelName The channel name of the stream to listen for.
     */
    public void addStream(String channelName) {
        twitchClient.getClientHelper().enableStreamEventListener(channelName);
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
     *
     * @return The current {@link TwitchClient}.
     */
    public TwitchClient getTwitchClient() {
        return twitchClient;
    }

}
