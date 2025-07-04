package com.beanbeanjuice.cafebot.utility.sections.settings;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import lombok.Getter;

public enum CustomChannels {

    WELCOME (GuildInformationType.WELCOME_CHANNEL_ID),
    GOODBYE (GuildInformationType.GOODBYE_CHANNEL_ID),
    VENTING (GuildInformationType.VENTING_CHANNEL_ID),
    COUNTING (GuildInformationType.COUNTING_CHANNEL_ID),
    DAILY (GuildInformationType.DAILY_CHANNEL_ID),
    UPDATE (GuildInformationType.UPDATE_CHANNEL_ID),
    TWITCH (GuildInformationType.TWITCH_CHANNEL_ID),
    BIRTHDAY (GuildInformationType.BIRTHDAY_CHANNEL_ID);

    @Getter private final GuildInformationType type;

    CustomChannels(GuildInformationType type) {
        this.type = type;
    }

}
