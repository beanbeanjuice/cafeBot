package com.beanbeanjuice.cafeapi.endpoints.cafe.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateRequest {

    private final String username;
    private final String email;

    private final String password;

    @JsonProperty("first_name")
    private final String firstName;

    @Nullable
    @JsonProperty("last_name")
    private final String lastName;

}
