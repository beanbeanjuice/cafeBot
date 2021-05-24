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
            game.startGame();
            return true;
        }
        return false;
    }

    public void stopGame(@NotNull TicTacToeGame game) {
        games.remove(game.getGuildID());
    }

    public Boolean hasGame(@NotNull String guildID) {
        return games.containsKey(guildID);
    }

}
