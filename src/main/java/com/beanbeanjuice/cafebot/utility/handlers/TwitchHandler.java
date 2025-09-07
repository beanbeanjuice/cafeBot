package com.beanbeanjuice.cafebot.utility.handlers;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.listeners.TwitchGoLiveEventListener;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

import java.util.Set;

public class TwitchHandler {

    private final TwitchClient twitchClient;

    public TwitchHandler(String token, CafeBot cafeBot) {
        twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withDefaultAuthToken(new OAuth2Credential("twitch", token))
                .build();

        twitchClient.getEventManager().registerEventHandler(new SimpleEventHandler());
        twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(new TwitchGoLiveEventListener(twitchClient, cafeBot));

        handleStartup(cafeBot);
    }

    public void handleStartup(final CafeBot cafeBot) {
        cafeBot.getCafeAPI().getTwitchChannelApi().getChannels().thenAccept((twitchChannels) -> {
            Set<String> channels = twitchChannels.keySet();
            this.addStreams(channels);
            cafeBot.getLogger().log(TwitchHandler.class, LogLevel.INFO, String.format("Adding Twitch Channels: %s", channels), false, false);
        })
        .exceptionally((ex) -> {
            cafeBot.getLogger().log(TwitchHandler.class, LogLevel.WARN, String.format("Error Adding Twitch Channels: %s", ex.getMessage()), ex);
            return null;
        });
    }

    public void addStream(final String channelName) {
        twitchClient.getClientHelper().enableStreamEventListener(channelName);
    }

    public void addStreams(Set<String> channelNames) {
        twitchClient.getClientHelper().enableStreamEventListener(channelNames);
    }

}
