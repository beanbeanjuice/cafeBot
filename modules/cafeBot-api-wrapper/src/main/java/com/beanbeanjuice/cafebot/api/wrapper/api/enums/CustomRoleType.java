package com.beanbeanjuice.cafebot.api.wrapper.api.enums;

import lombok.Getter;

public enum CustomRoleType {
    WELCOME ("Welcome Notifications"),
    GOODBYE ("Goodbye Notifications"),
    COUNTING_FAILURE ("Counting Failure"),
    TWITCH_NOTIFICATIONS ("Twitch Notifications");

    @Getter private final String friendlyName;

    CustomRoleType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

}
