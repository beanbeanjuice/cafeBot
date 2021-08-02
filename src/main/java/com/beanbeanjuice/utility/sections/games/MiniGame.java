package com.beanbeanjuice.utility.sections.games;

import org.jetbrains.annotations.NotNull;

public enum MiniGame {

    TIC_TAC_TOE("tic_tac_toe", "Tic-Tac-Toe"),
    CONNECT_FOUR("connect_four", "Connect-4");

    private String rowName;
    private String name;

    MiniGame(@NotNull String rowName, @NotNull String name) {
        this.rowName = rowName;
        this.name = name;
    }

    @NotNull
    public String getRowName() {
        return rowName;
    }

    @NotNull
    public String getName() {
        return name;
    }
}
