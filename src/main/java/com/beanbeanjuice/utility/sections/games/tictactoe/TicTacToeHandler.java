package com.beanbeanjuice.utility.sections.games.tictactoe;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * A class used for handling {@link TicTacToeGame}.
 *
 * @author beanbeanjuice
 */
public class TicTacToeHandler {

    private HashMap<String, TicTacToeGame> games;

    /**
     * Creates a new {@link TicTacToeHandler}.
     */
    public TicTacToeHandler() {
        games = new HashMap<>();
    }

    /**
     * Creates a {@link TicTacToeGame}.
     * @param guildID the ID of the {@link net.dv8tion.jda.api.entities.Guild Guild} to create the {@link TicTacToeGame} in.
     * @param game The {@link TicTacToeGame}.
     * @return True, if the {@link TicTacToeGame} was created successfully.
     */
    @NotNull
    public Boolean createGame(@NotNull String guildID, @NotNull TicTacToeGame game) {
        if (games.get(guildID) == null) {
            games.put(guildID, game);
            game.startGame();
            return true;
        }
        return false;
    }

    /**
     * Stops a running {@link TicTacToeGame}.
     * @param game The {@link TicTacToeGame} to be removed and stopped.
     */
    public void stopGame(@NotNull TicTacToeGame game) {
        games.remove(game.getGuildID());
    }

}
