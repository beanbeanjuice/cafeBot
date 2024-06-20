package com.beanbeanjuice.cafeapi.cafebot.minigames.winstreaks;

import lombok.Getter;

/**
 * A class used for a user's {@link WinStreak}.
 *
 * @author beanbeanjuice
 */
public class WinStreak {

    @Getter private final int ticTacToeWins;
    @Getter private final int connectFourWins;

    /**
     * Creates a new {@link WinStreak}.
     * @param ticTacToeWins The {@link Integer amount} of {@link MinigameType TIC_TAC_TOE} wins.
     * @param connectFourWins The {@link Integer amount} of {@link MinigameType CONNECT_FOUR} wins.
     */
    public WinStreak(int ticTacToeWins, int connectFourWins) {
        this.ticTacToeWins = ticTacToeWins;
        this.connectFourWins = connectFourWins;
    }

}
