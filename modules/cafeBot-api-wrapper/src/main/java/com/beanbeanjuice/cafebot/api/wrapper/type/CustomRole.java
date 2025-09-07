package com.beanbeanjuice.cafebot.api.wrapper.type;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomRoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomRole {

    private final String guildId;
    private final CustomRoleType type;
    private final String roleId;

}
