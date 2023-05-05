package com.beanbeanjuice.utility.section.twitch;

import com.beanbeanjuice.Bot;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

        twitchClient.getEventManager().registerEventHandler(new SimpleEventHandler());  // TODO: CONFIRM THIS WORKS
    }

    /**
     * Adds a stream to listen for.
     * @param channelName The channel name of the stream to listen for.
     */
    public void addStream(String channelName) throws HystrixRuntimeException, ContextedRuntimeException {
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
     * @return The current {@link TwitchClient}.
     */
    public TwitchClient getTwitchClient() {
        return twitchClient;
    }

    /**
     * Check if a {@link String twitchChannel} exists.
     * @param channelName The {@link String channelName} of the twitch channel.
     * @return True, if the channel exists.
     */
    @NotNull
    public Boolean channelExists(@NotNull String channelName) {
        try {
            return !twitchClient.getHelix().getUsers(null, null, List.of(channelName)).execute().getUsers().isEmpty();
        } catch (HystrixRuntimeException | ContextedRuntimeException | UnsupportedOperationException e) {
            return false;
        }
    }

}
