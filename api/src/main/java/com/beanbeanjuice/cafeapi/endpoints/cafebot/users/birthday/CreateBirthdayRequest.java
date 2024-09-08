package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.birthday;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateBirthdayRequest {

    private final String date;
    @JsonProperty("time_zone") private final String timeZone;

}
