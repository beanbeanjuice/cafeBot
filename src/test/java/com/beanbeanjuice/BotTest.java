package com.beanbeanjuice;

import com.beanbeanjuice.utility.logging.LogLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the startup of the {@link Bot}.
 *
 * @author beanbeanjuice
 * @since v3.0.1
 */
public class BotTest {

    @Test
    @DisplayName("Log Level Test")
    public void logLevelTest() {
        assertEquals(LogLevel.INFO.formatCode(), "INFO......", "Test the formatting method.");
    }

}
