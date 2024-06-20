package com.beanbeanjuice.cafebot.utility.section.game.connectfour;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * A class used for handling {@link ConnectFourGame}.
 *
 * @author beanbeanjuice
 */
public class ConnectFourHandler {

    private static final HashMap<String, ConnectFourGame> games = new HashMap<>();

    /**
     * Creates a new {@link ConnectFourGame} for the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild} specified.
     * @param game The {@link ConnectFourGame} to add.
     * @return True, if the {@link ConnectFourGame} was created successfully.
     */
    @NotNull
    public static Boolean createGame(@NotNull String guildID, @NotNull ConnectFourGame game) {
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
    public static void stopGame(@NotNull ConnectFourGame game) {
        games.remove(game.getGuildID());
    }

}
