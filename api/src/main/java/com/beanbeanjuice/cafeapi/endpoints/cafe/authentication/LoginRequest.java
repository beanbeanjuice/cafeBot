package com.beanbeanjuice.cafeapi.endpoints.cafe.authentication;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {

    private final String username;
    private final String password;

}
