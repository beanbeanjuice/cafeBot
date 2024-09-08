package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateGameRequest {

    @JsonProperty("opponent1_id")
    private final int opponent1ID;

    @JsonProperty("opponent2_id")
    private final int opponent2ID;

    @Nullable
    @JsonProperty("winner_id")
    private final Integer winnerID;

    @JsonProperty("game_type")
    private final String gameType;

}
