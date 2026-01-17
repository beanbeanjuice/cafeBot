package com.beanbeanjuice.cafebot.endpoints.discord.server;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomRoleType;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomRole;
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

public class CustomRoleApiTest extends ApiTest {

    private String guildId;
    private HashMap<CustomRoleType, String> roles;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException {
        guildId = generateSnowflake().toString();
        roles = new HashMap<>();

        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (CustomRoleType type : CustomRoleType.values()) {
            String roleId = generateSnowflake().toString();
            roles.put(type, roleId);
            futures.add(cafeAPI.getCustomRoleApi().setCustomRole(guildId, type, roleId));
        }

        // Wait for all async calls to finish
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    }

    @Test
    @DisplayName("can get all custom roles for a guild")
    public void canGetAllCustomRolesForGuild() throws ExecutionException, InterruptedException {
        Map<CustomRoleType, CustomRole> roles = cafeAPI.getCustomRoleApi().getCustomRoles(guildId).get();

        roles.forEach((customRoleType, role) -> {
            Assertions.assertEquals(role.getRoleId(), this.roles.get(customRoleType));
        });
    }

    @Test
    @DisplayName("can get specific role for a guild")
    public void canGetSpecificRoleForGuild() throws ExecutionException, InterruptedException {
        CustomRole role = cafeAPI.getCustomRoleApi().getCustomRole(guildId, CustomRoleType.TWITCH_NOTIFICATIONS).get();

        Assertions.assertEquals(role.getRoleId(), this.roles.get(CustomRoleType.TWITCH_NOTIFICATIONS));
    }

    @Test
    @DisplayName("can set custom role")
    public void canSetCustomRole() throws ExecutionException, InterruptedException {
        String newRoleId = generateSnowflake().toString();
        CustomRole role = cafeAPI.getCustomRoleApi().setCustomRole(guildId, CustomRoleType.TWITCH_NOTIFICATIONS, newRoleId).get();

        Assertions.assertNotEquals(role.getRoleId(), this.roles.get(CustomRoleType.TWITCH_NOTIFICATIONS));
    }

    @Test
    @DisplayName("can delete custom role")
    public void canDeleteCustomRole() {
        cafeAPI.getCustomRoleApi().deleteCustomRole(guildId, CustomRoleType.TWITCH_NOTIFICATIONS).join();

        Assertions.assertThrows(CompletionException.class, () ->
                cafeAPI.getCustomRoleApi()
                        .getCustomRole(guildId, CustomRoleType.TWITCH_NOTIFICATIONS)
                        .join()
        );
    }
    
}
