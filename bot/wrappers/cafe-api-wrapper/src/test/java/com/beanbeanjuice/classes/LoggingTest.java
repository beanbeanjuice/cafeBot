package com.beanbeanjuice.classes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingTest {

    @Test
    @DisplayName("Logging Test")
    public void testLogging() {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Hello, world!");
    }

}
