package com.beanbeanjuice.cafebot.api.wrapper.api.enums;

import lombok.Getter;

public enum CustomChannelType {

    AIRPORT_WELCOME ("Welcome", "This channel is for people who join your server. If you have a greeting set, the greeting will be sent here! Use `/airport WELCOME` to set the greeting!"),
    AIRPORT_GOODBYE ("Goodbye", "This channel is for people who leave your server... If you want to set a goodbye message, use `/airport GOODBYE` to set it!"),
    COUNTING ("Counting", "This channel is for counting purposes! Start from 0! 😀"),
    UPDATE_NOTIFICATIONS ("Update Notifications", "This channel will receive notifications whenever my boss updates me! <a:cafeBot:1119635469727191190>\n"),
    TWITCH_NOTIFICATIONS ("Twitch Notifications", "This channel will receive any notifications whenever your selected twitch channels go live. Use the `/twitch` command!"),
    POLL ("Poll", "This channel will receive all polls you create. Use the `/poll` command to get started!"),
    RAFFLE ("Raffle", "This channel will receive all raffles you create. Use the `/raffle` command to get started!"),
    BIRTHDAY ("Birthday", "This channel will receive all birthday messages for people who have told me to remind people of their birthday! To set your birthday, use `/birthday`!"),
    CONFESSIONS ("Confessions", "This channel if for people to anonymously confess things using the `/confess` command!"),
    HONEYPOT ("Honeypot", "This channel will ban any user that sends reactions or messages in this channel... for those pesky evil bots... unlike me of course! <:cafeBot_thumbs_up:1457847525280321577>"),
    DAILY ("Daily", "This channel will reset daily!"),
    LOG ("Log", "This channel will receive any log messages that I might need to send you!");

    @Getter private final String friendlyName;
    @Getter private final String description;

    CustomChannelType(String friendlyName, String description) {
        this.friendlyName = friendlyName;
        this.description = description;
    }

}
