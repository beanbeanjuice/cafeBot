package com.beanbeanjuice.utility.sections.games.connectfour;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ConnectFourHandler {

    private HashMap<String, ConnectFourGame> games;

    public ConnectFourHandler() {
        games = new HashMap<>();
    }

    @NotNull
    public Boolean createGame(@NotNull String guildID, @NotNull ConnectFourGame game) {
        if (games.get(guildID) == null) {
            games.put(guildID, game);
            game.startGame();
            return true;
        }
        return false;
    }

    public void stopGame(@NotNull ConnectFourGame game) {
        games.remove(game.getGuildID());
    }

}
