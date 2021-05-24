package com.beanbeanjuice.utility.sections.games.connectfour;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * A class used for handling {@link ConnectFourGame}.
 *
 * @author beanbeanjuice
 */
public class ConnectFourHandler {

    private HashMap<String, ConnectFourGame> games;

    /**
     * Creates a new {@link ConnectFourHandler}.
     */
    public ConnectFourHandler() {
        games = new HashMap<>();
    }

    /**
     * Creates a new {@link ConnectFourGame} for the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild} specified.
     * @param game The {@link ConnectFourGame} to add.
     * @return Whether or not it can be added.
     */
    @NotNull
    public Boolean createGame(@NotNull String guildID, @NotNull ConnectFourGame game) {
        if (games.get(guildID) == null) {
            games.put(guildID, game);
            game.startGame();
            return true;
        }
        return false;
    }

    /**
     * Stops the specified {@link ConnectFourGame}.
     * @param game The {@link ConnectFourGame} specified.
     */
    public void stopGame(@NotNull ConnectFourGame game) {
        games.remove(game.getGuildID());
    }

}
