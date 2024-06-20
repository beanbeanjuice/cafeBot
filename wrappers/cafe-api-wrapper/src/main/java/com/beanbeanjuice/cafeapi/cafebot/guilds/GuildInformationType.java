package com.beanbeanjuice.cafeapi.cafebot.guilds;

/**
 * A static class for {@link GuildInformationType}.
 *
 * @author beanbeanjuice
 */
public enum GuildInformationType {

    PREFIX ("prefix"),
    MODERATOR_ROLE_ID ("moderator_role_id"),
    TWITCH_CHANNEL_ID ("twitch_channel_id"),
    MUTED_ROLE_ID ("muted_role_id"),
    LIVE_NOTIFICATIONS_ROLE_ID ("live_notifications_role_id"),
    NOTIFY_ON_UPDATE ("notify_on_update"),
    UPDATE_CHANNEL_ID ("update_channel_id"),
    COUNTING_CHANNEL_ID ("counting_channel_id"),
    POLL_CHANNEL_ID ("poll_channel_id"),
    RAFFLE_CHANNEL_ID ("raffle_channel_id"),
    BIRTHDAY_CHANNEL_ID ("birthday_channel_id"),
    WELCOME_CHANNEL_ID ("welcome_channel_id"),
    GOODBYE_CHANNEL_ID ("goodbye_channel_id"),
    LOG_CHANNEL_ID ("log_channel_id"),
    VENTING_CHANNEL_ID ("venting_channel_id"),
    AI_RESPONSE ("ai_response"),
    DAILY_CHANNEL_ID ("daily_channel_id");

    private final String type;

    /**
     * Creates a new {@link GuildInformationType} static object.
     * @param type The {@link String type} of object.
     */
    GuildInformationType(String type) {
        this.type = type;
    }

    /**
     * @return The {@link GuildInformationType} as a {@link String}.
     */
    public String getType() {
        return type;
    }
}
