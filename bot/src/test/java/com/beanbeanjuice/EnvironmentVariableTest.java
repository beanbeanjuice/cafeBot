package com.beanbeanjuice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EnvironmentVariableTest {

    @Test
    @DisplayName("Environment Variable Test")
    public void logLevelTest() {
        Assertions.assertNotNull(System.getenv("CAFEBOT_VERSION"));
        Assertions.assertNotNull(System.getenv("CAFEBOT_TOKEN"));
        Assertions.assertNotNull(System.getenv("CAFEBOT_TWITCH_ACCESS_TOKEN"));
        Assertions.assertNotNull(System.getenv("CAFEBOT_REQUEST_LOCATION"));
        Assertions.assertNotNull(System.getenv("CAFEBOT_GUILD_ID"));
        Assertions.assertNotNull(System.getenv("CAFEBOT_GUILD_LOG_CHANNEL_ID"));
        Assertions.assertNotNull(System.getenv("CAFEBOT_GUILD_WEBHOOK_URL"));
    }

}
