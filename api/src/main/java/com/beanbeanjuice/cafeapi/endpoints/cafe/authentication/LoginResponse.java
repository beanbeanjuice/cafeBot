package com.beanbeanjuice.cafeapi.endpoints.cafe.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonPropertyOrder({"user_id", "username", "access_token", "refresh_token"})
public class LoginResponse {

    @JsonProperty("user_id") private long userID;
    @JsonProperty("username") private String username;
    @JsonProperty("access_token") private String accessToken;
    @JsonProperty("refresh_token") private String refreshToken;

}
