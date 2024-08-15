package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks.MinigameType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MutualGuildsEndpointTests {

    @Test
    @DisplayName("Get Mutual Guilds")
    public void testWinStreakEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        List<String> mutualGuildIDs = cafeAPI.getMutualGuildsEndpoint().getMutualGuilds("690927484199370753").get();
        Assertions.assertFalse(mutualGuildIDs.isEmpty());

        mutualGuildIDs.forEach(System.out::println);
    }

}
