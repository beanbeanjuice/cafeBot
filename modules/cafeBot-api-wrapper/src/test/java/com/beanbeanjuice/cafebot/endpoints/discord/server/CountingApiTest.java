package com.beanbeanjuice.cafebot.endpoints.discord.server;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.type.CountingStatistics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CountingApiTest extends ApiTest {

    private String guildId;

    private String user1;
    private String user2;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException {
        guildId = generateSnowflake().toString();
        user1 = generateSnowflake().toString();
        user2 = generateSnowflake().toString();

        cafeAPI.getCountingApi().updateCountingStatistics(guildId, user1, 1).get();
    }

    @Test
    @DisplayName("can get global counting statistics")
    public void getGlobalCountingStatistics() throws ExecutionException, InterruptedException {
        Map<String, CountingStatistics> statisticsMap = cafeAPI.getCountingApi().getCountingStatistics().get();

        Assertions.assertEquals(1, statisticsMap.get(guildId).getCurrentCount());
        Assertions.assertEquals(1, statisticsMap.get(guildId).getHighestCount());
        Assertions.assertEquals(user1, statisticsMap.get(guildId).getLastUserId().get());
    }

    @Test
    @DisplayName("can get counting statistics")
    public void getCountingStatistics() throws ExecutionException, InterruptedException {
        CountingStatistics statistics = cafeAPI.getCountingApi().getCountingStatistics(guildId).get();

        Assertions.assertEquals(1, statistics.getCurrentCount());
        Assertions.assertEquals(1, statistics.getHighestCount());
        Assertions.assertEquals(user1, statistics.getLastUserId().get());
    }

    @Test
    @DisplayName("update counting statistics")
    public void updateCountingStatistics() throws ExecutionException, InterruptedException {
        CountingStatistics statistics = cafeAPI.getCountingApi().updateCountingStatistics(guildId, user2, 2).get();

        Assertions.assertEquals(2, statistics.getCurrentCount());
        Assertions.assertEquals(2, statistics.getHighestCount());
        Assertions.assertEquals(user2, statistics.getLastUserId().get());
    }

}
