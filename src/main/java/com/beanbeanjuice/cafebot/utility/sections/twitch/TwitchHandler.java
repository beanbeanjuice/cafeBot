package com.beanbeanjuice.cafebot.utility.sections.twitch;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

import java.util.HashSet;

public class TwitchHandler {

    private final TwitchClient twitchClient;

    public TwitchHandler(final String token, final CafeBot cafeBot) {
        twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withDefaultAuthToken(new OAuth2Credential("twitch", token))
                .build();

        twitchClient.getEventManager().registerEventHandler(new SimpleEventHandler());
        twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(new TwitchGoLiveEventListener(twitchClient, cafeBot));

        handleStartup(cafeBot);
    }

    public void handleStartup(final CafeBot cafeBot) {
        cafeBot.getCafeAPI().getTwitchEndpoint().getAllTwitches().thenAcceptAsync((twitchMap) -> {
            HashSet<String> channels = new HashSet<>();
            twitchMap.values().forEach(channels::addAll);
            channels.forEach((channel) -> {
                this.addStream(channel);
                cafeBot.getLogger().log(TwitchHandler.class, LogLevel.DEBUG, String.format("Adding Twitch Channel: %s", channel), false, false);
            });
        }).exceptionallyAsync((e) -> {
            cafeBot.getLogger().log(TwitchHandler.class, LogLevel.ERROR, String.format("Error Adding Twitch Channels: %s", e.getMessage()), e);
            return null;
        });
    }

    public void addStream(final String channelName) {
        twitchClient.getClientHelper().enableStreamEventListener(channelName);
    }

}
