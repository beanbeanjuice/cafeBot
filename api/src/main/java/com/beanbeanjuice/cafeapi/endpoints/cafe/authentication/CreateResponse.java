package com.beanbeanjuice.cafeapi.endpoints.cafe.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateResponse {

    private final long id;
    private final String username;

}
