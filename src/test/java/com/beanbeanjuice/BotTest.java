package com.beanbeanjuice;

import com.beanbeanjuice.cafebot.Bot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Assertions.assertDoesNotThrow(() -> {
            new Bot();
        });
    }

}
