package com.beanbeanjuice.utility.logging;

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
        assertEquals(LogLevel.INFO.formatCode(), "INFO......", "Test the formatting method.");
    }

}
