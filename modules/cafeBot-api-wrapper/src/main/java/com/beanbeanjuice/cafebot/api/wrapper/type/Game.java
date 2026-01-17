package com.beanbeanjuice.cafebot.api.wrapper.type;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameStatusType;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Game {

    private final int id;
    private final GameType type;
    private final GameStatusType status;
    private final String[] players;
    private final String[] winners;
    private int wager;
    private int pool;

}
