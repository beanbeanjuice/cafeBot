package com.beanbeanjuice.cafebot.utility;

public enum EnvironmentVariable {

    CAFEBOT_TOKEN,
    CAFEBOT_GUILD_ID,
    CAFEBOT_GUILD_LOG_CHANNEL_ID,
    CAFEBOT_GUILD_WEBHOOK_URL,
    CAFEBOT_REQUEST_LOCATION,
    CAFEBOT_TWITCH_ACCESS_TOKEN,
    KAWAII_API_TOKEN,
    API_PASSWORD;

    public String getSystemVariable() {
        return System.getenv(this.name());
    }

}
