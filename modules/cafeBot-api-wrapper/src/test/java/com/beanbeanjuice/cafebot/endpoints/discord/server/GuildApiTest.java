package com.beanbeanjuice.cafebot.endpoints.discord.server;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GuildFlag;
import com.beanbeanjuice.cafebot.api.wrapper.type.DiscordServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class GuildApiTest extends ApiTest {

    private String guildId;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException {
        this.guildId = generateSnowflake().toString();

        // make sure guild exists in database.
        cafeAPI.getGuildApi().updateDiscordServer(guildId, new DiscordServer(guildId, 100.0f, false)).get();
    }

    @Test
    @DisplayName("can get all guilds")
    public void canGetAllGuilds() throws ExecutionException, InterruptedException {
        Map<String, DiscordServer> guilds = cafeAPI.getGuildApi().getDiscordServers().get();

        Assertions.assertNotNull(guilds.get(this.guildId));
    }

    @Test
    @DisplayName("can get single guild")
    public void canGetSingleGuild() throws ExecutionException, InterruptedException {
        DiscordServer guild = cafeAPI.getGuildApi().getDiscordServer(this.guildId).get();

        Assertions.assertNotNull(guild);
    }

    @Test
    @DisplayName("can update guild")
    public void canUpdateGuild() throws ExecutionException, InterruptedException {
        DiscordServer guild = cafeAPI.getGuildApi().getDiscordServer(this.guildId).get();
        Assertions.assertFalse(guild.isAiEnabled());

        DiscordServer guild2 = cafeAPI.getGuildApi().updateDiscordServer(this.guildId, new DiscordServer(null, 0.0f, true)).get();
        Assertions.assertTrue(guild2.isAiEnabled());
    }

    @Test
    @DisplayName("can delete single guild")
    public void canDeleteGuild() throws ExecutionException, InterruptedException {
        Map<String, DiscordServer> guilds = cafeAPI.getGuildApi().getDiscordServers().get();
        Assertions.assertNotNull(guilds.get(this.guildId));

        cafeAPI.getGuildApi().deleteDiscordServer(this.guildId).get();

        Map<String, DiscordServer> guilds2 = cafeAPI.getGuildApi().getDiscordServers().get();
        Assertions.assertNull(guilds2.get(this.guildId));
    }

    @Test
    @DisplayName("can get discord server flags")
    public void canGetGuildFlags() throws ExecutionException, InterruptedException {
        Map<GuildFlag, Boolean> flags = cafeAPI.getGuildApi().getDiscordServerFlags(this.guildId).get();

        for (var entry : flags.entrySet()) Assertions.assertFalse(entry.getValue());
    }

}
