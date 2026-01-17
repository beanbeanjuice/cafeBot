package com.beanbeanjuice.cafebot.api.wrapper.api.enums;

import lombok.Getter;

public enum GameType {
    TIC_TAC_TOE ("Tic Tac Toe"),
    CONNECT_FOUR ("Connect 4");

    @Getter private final String friendlyName;

    GameType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

}
