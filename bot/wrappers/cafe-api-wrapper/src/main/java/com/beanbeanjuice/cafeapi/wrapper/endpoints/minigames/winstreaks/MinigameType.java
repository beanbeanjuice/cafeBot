package com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks;

import lombok.Getter;

public enum MinigameType {

    TIC_TAC_TOE ("tic_tac_toe", "Tic-Tac-Toe"),
    CONNECT_FOUR ("connect_four", "Connect-4");

    @Getter private final String type;
    @Getter private final String name;

    MinigameType(final String type, final String name) {
        this.type = type;
        this.name = name;
    }

}
