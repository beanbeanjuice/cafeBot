package com.beanbeanjuice.cafebot.endpoints.discord.server;

import com.beanbeanjuice.cafebot.ApiTest;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class TwitchChannelApiTest extends ApiTest {

    private String CHANNEL_1;
    private String CHANNEL_2;

    private String GUILD_1;
    private String GUILD_2;

    @BeforeEach
    public void createTwitchChannels() throws ExecutionException, InterruptedException {
        GUILD_1 = generateSnowflake().toString();
        GUILD_2 = generateSnowflake().toString();

        // I know it's not a snowflake, but just want to use a random string here.
        CHANNEL_1 = generateSnowflake().toString();
        CHANNEL_2 = generateSnowflake().toString();

        cafeAPI.getTwitchChannelApi().addChannel(GUILD_1, CHANNEL_1).get();
        cafeAPI.getTwitchChannelApi().addChannel(GUILD_1, CHANNEL_2).get();

        cafeAPI.getTwitchChannelApi().addChannel(GUILD_2, CHANNEL_1).get();
    }

    @AfterEach
    public void deleteTwitchChannels() throws ExecutionException, InterruptedException {
        cafeAPI.getTwitchChannelApi().deleteChannel(GUILD_1, CHANNEL_1).get();
        cafeAPI.getTwitchChannelApi().deleteChannel(GUILD_1, CHANNEL_2).get();

        cafeAPI.getTwitchChannelApi().deleteChannel(GUILD_2, CHANNEL_1).get();
    }

    @Test
    @DisplayName("can get all twitch channels and respective guilds")
    public void canGetAllTwitchChannelsAndRespectiveGuilds() throws ExecutionException, InterruptedException {
        HashMap<String, ArrayList<String>> channels = cafeAPI.getTwitchChannelApi().getChannels().get();

        Assertions.assertEquals(2, channels.get(CHANNEL_1).size());
        Assertions.assertEquals(1, channels.get(CHANNEL_2).size());
    }

    @Test
    @DisplayName("can get twitch channels for a guild")
    public void canGetTwitchChannelsForGuild() throws ExecutionException, InterruptedException {
        ArrayList<String> channels = cafeAPI.getTwitchChannelApi().getChannels(GUILD_1).get();

        Assertions.assertEquals(2, channels.size());
    }

    @Test
    @DisplayName("can get guilds for twitch channel")
    public void canGetGuildsForTwitchChannel() throws ExecutionException, InterruptedException {
        ArrayList<String> guilds = cafeAPI.getTwitchChannelApi().getGuilds(CHANNEL_1).get();

        Assertions.assertEquals(2, guilds.size());
    }

}
