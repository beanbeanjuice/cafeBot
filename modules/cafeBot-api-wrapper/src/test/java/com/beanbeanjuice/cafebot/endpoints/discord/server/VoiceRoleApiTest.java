package com.beanbeanjuice.cafebot.endpoints.discord.server;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.type.VoiceRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class VoiceRoleApiTest extends ApiTest {

    private String guildId;
    private String channelId;
    private String roleId;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException {
        guildId = generateSnowflake().toString();
        channelId = generateSnowflake().toString();
        roleId = generateSnowflake().toString();

        cafeAPI.getVoiceRoleApi().createVoiceRole(guildId, channelId, roleId).get();
    }

    @Test
    @DisplayName("can get voice roles for guild")
    public void canGetVoiceRolesForGuild() throws ExecutionException, InterruptedException {
        List<VoiceRole> voiceRoles = cafeAPI.getVoiceRoleApi().getVoiceRoles(guildId).get();

        Assertions.assertEquals(1, voiceRoles.size());
        Assertions.assertEquals(roleId, voiceRoles.getFirst().getRoleId());
        Assertions.assertEquals(channelId, voiceRoles.getFirst().getChannelId());
        Assertions.assertEquals(guildId, voiceRoles.getFirst().getGuildId());
    }

    @Test
    @DisplayName("can set voice role for guild")
    public void setVoiceRoleForGuild() throws ExecutionException, InterruptedException {
        String roleId = generateSnowflake().toString();
        String channelId = generateSnowflake().toString();

        VoiceRole voiceRole = cafeAPI.getVoiceRoleApi().createVoiceRole(guildId, channelId, roleId).get();

        Assertions.assertNotEquals(this.roleId, voiceRole.getRoleId());
        Assertions.assertNotEquals(this.channelId, voiceRole.getChannelId());
        Assertions.assertEquals(this.guildId, voiceRole.getGuildId());
    }

    @Test
    @DisplayName("can delete voice role for guild")
    public void deleteVoiceRoleForGuild() throws ExecutionException, InterruptedException {
        cafeAPI.getVoiceRoleApi().deleteVoiceRole(guildId, channelId, roleId).join();

        List<VoiceRole> voiceRoles = cafeAPI.getVoiceRoleApi().getVoiceRoles(guildId).get();

        voiceRoles.forEach(voiceRole -> {
            Assertions.assertFalse(voiceRole.getRoleId().equals(roleId) && voiceRole.getChannelId().equals(channelId));
        });
    }

}
