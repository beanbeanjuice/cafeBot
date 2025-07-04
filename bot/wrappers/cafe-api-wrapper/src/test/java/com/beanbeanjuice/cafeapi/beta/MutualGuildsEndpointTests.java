package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MutualGuildsEndpointTests {

    private static CafeAPI cafeAPI;

    @BeforeAll
    public static void instantiateCafeAPI() {
        cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);
    }

    @BeforeEach
    public void deleteGuild() throws InterruptedException, ExecutionException {
        cafeAPI.getMutualGuildsEndpoint().removeMutualGuild("690927484199370753", "723103621889130497").get();
    }

    @Test
    @DisplayName("Get Mutual Guilds")
    public void testGetMutualGuilds() throws ExecutionException, InterruptedException {
        List<String> mutualGuildIDs = cafeAPI.getMutualGuildsEndpoint().getMutualGuilds("690927484199370753").get();
        Assertions.assertFalse(mutualGuildIDs.isEmpty());
    }

    @Test
    @DisplayName("Add Mutual Guild")
    public void testAddMutualGuild() throws ExecutionException, InterruptedException {
        Assertions.assertTrue(cafeAPI.getMutualGuildsEndpoint().addMutualGuild("690927484199370753", "723103621889130497").get());
        Assertions.assertTrue(cafeAPI.getMutualGuildsEndpoint().getMutualGuilds("690927484199370753").get().contains("723103621889130497"));

        Assertions.assertTrue(cafeAPI.getMutualGuildsEndpoint().removeMutualGuild("690927484199370753", "723103621889130497").get());
        Assertions.assertFalse(cafeAPI.getMutualGuildsEndpoint().getMutualGuilds("690927484199370753").get().contains("723103621889130497"));
    }

    @Test
    @DisplayName("Remove Mutual Guild")
    public void testRemoveMutualGuild() throws ExecutionException, InterruptedException {
        Assertions.assertTrue(cafeAPI.getMutualGuildsEndpoint().removeMutualGuild("690927484199370753", "723103621889130497").get());
        Assertions.assertFalse(cafeAPI.getMutualGuildsEndpoint().getMutualGuilds("690927484199370753").get().contains("723103621889130497"));
    }

}
