package com.beanbeanjuice.cafebot.endpoints.discord.server;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.type.MutualGuild;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MutualGuildsApiTest extends ApiTest {

    private List<MutualGuild> mutualGuilds;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException {
        mutualGuilds = new ArrayList<>();

        // 10 users
        for (int i = 0; i < 10; i++) {
            String userId = generateSnowflake().toString();

            // 3 guilds each
            for (int j = 0; j < 3; j++) {
                String guildId = generateSnowflake().toString();

                mutualGuilds.add(new MutualGuild(userId, guildId));
            }
        }

        cafeAPI.getMutualGuildsApi().addMutualGuilds(mutualGuilds).get();
    }

    @Test
    @DisplayName("can get mutual guilds for a user")
    public void canGetMutualGuildsForUser() throws ExecutionException, InterruptedException {
        String userId = mutualGuilds.getFirst().getUserId();

        List<String> guilds = cafeAPI.getMutualGuildsApi().getMutualGuilds(userId).get();

        Assertions.assertEquals(3, guilds.size());
    }

    @Test
    @DisplayName("can create mutual guilds")
    public void canCreateMutualGuilds() throws ExecutionException, InterruptedException {
        int count = cafeAPI.getMutualGuildsApi().addMutualGuilds(mutualGuilds).get();

        Assertions.assertEquals(30, count);
    }

    @Test
    @DisplayName("can delete mutual guilds")
    public void canDeleteMutualGuilds() throws ExecutionException, InterruptedException {
        List<String> guilds = cafeAPI.getMutualGuildsApi().getMutualGuilds(mutualGuilds.getFirst().getUserId()).get();
        Assertions.assertEquals(3, guilds.size());

        cafeAPI.getMutualGuildsApi().deleteMutualGuilds(mutualGuilds).get();

        List<String> guilds2 = cafeAPI.getMutualGuildsApi().getMutualGuilds(mutualGuilds.getFirst().getUserId()).get();
        Assertions.assertEquals(0, guilds2.size());
    }

}
