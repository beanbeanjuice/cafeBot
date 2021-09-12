package com.beanbeanjuice.cafebot;

import com.beanbeanjuice.utility.logger.LogLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LogLevelTest {

    @Test
    @DisplayName("Log Level Test")
    public void logLevelTest() {
        Assertions.assertEquals(LogLevel.INFO.formatCode(), "INFO......", "Test the formatting method.");
    }

}