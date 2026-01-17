package com.beanbeanjuice.cafebot.utility;

import io.github.cdimascio.dotenv.Dotenv;

public enum EnvironmentVariable {

    CAFEBOT_API_URL,
    CAFEBOT_API_TOKEN,
    CAFEBOT_KUMA_URL,
    CAFEBOT_TOKEN,
    CAFEBOT_GUILD_ID,
    CAFEBOT_GUILD_LOG_CHANNEL_ID,
    CAFEBOT_GUILD_WEBHOOK_URL,
    CAFEBOT_REQUEST_LOCATION,
    CAFEBOT_TWITCH_ACCESS_TOKEN,
    CAFEBOT_OPENAI_API_KEY,
    CAFEBOT_OPENAI_ASSISTANT_ID,
    CAFEBOT_LOG_LEVEL,
    REDDIT_CLIENT_ID,
    REDDIT_CLIENT_SECRET,
    REDDIT_REFRESH_TOKEN;

    private static final Dotenv dotenv;

    static {
        Dotenv temp;
        try {
            temp = Dotenv.configure()
                    .ignoreIfMissing() // prevent exception if .env is missing
                    .load();
        } catch (Exception e) {
            temp = null;
        }
        dotenv = temp;
    }

    public String getSystemVariable() {
        // .env first
        if (dotenv != null) {
            String value = dotenv.get(this.name());
            if (value != null) return value;
        }

        // fallback
        return System.getenv(this.name());
    }

}
