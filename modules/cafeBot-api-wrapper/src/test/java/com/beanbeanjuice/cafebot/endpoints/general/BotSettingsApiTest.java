package com.beanbeanjuice.cafebot.endpoints.general;

import com.beanbeanjuice.cafebot.ApiTest;
import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutionException;

public class BotSettingsApiTest extends ApiTest {

    String oldVersion;

    @BeforeAll
    public void setup() throws ExecutionException, InterruptedException {
        oldVersion = cafeAPI.getBotSettingsApi().getBotVersion().get();
    }

    @BeforeEach
    public void beforeEach() throws ExecutionException, InterruptedException {
        cafeAPI.getBotSettingsApi().updateBotVersion("9.9.9.9").get();
    }

    @AfterAll
    public void teardown() {
        cafeAPI.getBotSettingsApi().updateBotVersion(oldVersion);
    }

    @Test
    @DisplayName("can get bot version")
    public void canGetBotVersion() throws ExecutionException, InterruptedException {
        String version = cafeAPI.getBotSettingsApi().getBotVersion().get();

        Assertions.assertEquals("9.9.9.9", version);
    }

    @Test
    @DisplayName("can update bot version")
    public void canUpdateBotVersion() throws ExecutionException, InterruptedException {
        cafeAPI.getBotSettingsApi().updateBotVersion("0.1.0.0").get();

        String version = cafeAPI.getBotSettingsApi().getBotVersion().get();
        Assertions.assertEquals("0.1.0.0", version);
    }

}
