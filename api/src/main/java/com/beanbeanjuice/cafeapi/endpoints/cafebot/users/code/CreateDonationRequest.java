package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.code;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateDonationRequest {

    @JsonProperty("sender_id")
    private final int senderID;

    @JsonProperty("receiver_id")
    private final int receiverID;

}
