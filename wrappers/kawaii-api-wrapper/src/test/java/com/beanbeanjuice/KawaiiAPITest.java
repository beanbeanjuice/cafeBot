package com.beanbeanjuice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class KawaiiAPITest {

    @Test
    @DisplayName("Kawaii API Startup Test")
    public void testStartup() {
        KawaiiAPI kawaiiAPI = new KawaiiAPI("anonymous");
    }

}
