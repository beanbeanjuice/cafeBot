package com.beanbeanjuice.utility.sections.games.tictactoe;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class TicTacToeHandler {

    private HashMap<String, TicTacToeGame> games;

    public TicTacToeHandler() {
        games = new HashMap<>();
    }

    public Boolean createGame(@NotNull String guildID, @NotNull TicTacToeGame game) {
        if (games.get(guildID) == null) {
            games.put(guildID, game);
            return true;
        }
        return false;
    }

}
