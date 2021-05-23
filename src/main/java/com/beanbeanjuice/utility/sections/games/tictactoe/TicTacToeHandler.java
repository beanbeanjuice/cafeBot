package com.beanbeanjuice.utility.sections.games.tictactoe;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sql.SQLServer;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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
