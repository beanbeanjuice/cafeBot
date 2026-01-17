package com.beanbeanjuice.cafebot.endpoints.discord.server;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class CustomChannelApiTest extends ApiTest {

    private String guildId;
    private HashMap<CustomChannelType, String> channels;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException {
        guildId = generateSnowflake().toString();
        channels = new HashMap<>();

        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (CustomChannelType type : CustomChannelType.values()) {
            String channelId = generateSnowflake().toString();
            channels.put(type, channelId);
            futures.add(cafeAPI.getCustomChannelApi().setCustomChannel(guildId, type, channelId));
        }

        // Wait for all async calls to finish
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    }

    @Test
    @DisplayName("can get all custom channels of a type")
    public void testCanGetAllCustomChannelsOfAType() throws ExecutionException, InterruptedException {
        Map<String, CustomChannel> channels = cafeAPI.getCustomChannelApi().getCustomChannels(CustomChannelType.DAILY).get();

        Assertions.assertNotNull(channels.get(guildId));
    }

    @Test
    @DisplayName("can get all custom channels for a guild")
    public void canGetAllCustomChannelsForGuild() throws ExecutionException, InterruptedException {
        Map<CustomChannelType, CustomChannel> channels = cafeAPI.getCustomChannelApi().getCustomChannels(guildId).get();

        channels.forEach((customChannelType, channel) -> {
            Assertions.assertEquals(channel.getChannelId(), this.channels.get(customChannelType));
        });
    }

    @Test
    @DisplayName("can get specific channel for a guild")
    public void canGetSpecificChannelForGuild() throws ExecutionException, InterruptedException {
        CustomChannel channel = cafeAPI.getCustomChannelApi().getCustomChannel(guildId, CustomChannelType.BIRTHDAY).get();

        Assertions.assertEquals(channel.getChannelId(), this.channels.get(CustomChannelType.BIRTHDAY));
    }

    @Test
    @DisplayName("can set specific custom channel")
    public void canSetCustomChannel() throws ExecutionException, InterruptedException {
        String newChannelId = generateSnowflake().toString();
        CustomChannel channel = cafeAPI.getCustomChannelApi().setCustomChannel(guildId, CustomChannelType.BIRTHDAY, newChannelId).get();

        Assertions.assertNotEquals(channel.getChannelId(), this.channels.get(CustomChannelType.BIRTHDAY));
    }

    @Test
    @DisplayName("can set all custom channels")
    public void canSetAllCustomChannels() throws ExecutionException, InterruptedException {
        for (CustomChannelType type : CustomChannelType.values()) {
            String newChannelId = generateSnowflake().toString();
            CustomChannel channel = cafeAPI.getCustomChannelApi().setCustomChannel(guildId, type, newChannelId).get();

            Assertions.assertNotNull(channel);
            Assertions.assertEquals(channel.getChannelId(), newChannelId);
        }
    }

    @Test
    @DisplayName("can delete custom channel")
    public void canDeleteCustomChannel() {
        cafeAPI.getCustomChannelApi().deleteCustomChannel(guildId, CustomChannelType.BIRTHDAY).join();

        Assertions.assertThrows(CompletionException.class, () ->
                cafeAPI.getCustomChannelApi()
                        .getCustomChannel(guildId, CustomChannelType.BIRTHDAY)
                        .join()
        );
    }

}
