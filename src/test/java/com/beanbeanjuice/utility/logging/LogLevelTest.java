package com.beanbeanjuice.utility.logging;

import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafebot.utility.logging.LogManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link LogManager}.
 *
 * @author beanbeanjuice
 * @since v3.0.0
 */
class LogLevelTest {

    @Test
    @DisplayName("Log Level Test")
    public void logLevelTest() {
        assertEquals("INFO......", LogLevel.INFO.formatCode(), "Test the formatting method.");
    }

}
