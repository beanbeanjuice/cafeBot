package com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks;

import lombok.Getter;

import java.util.HashMap;

/**
 * A class used for a user's {@link WinStreak}.
 *
 * @author beanbeanjuice
 */
public class WinStreak {

    private final HashMap<MinigameType, Integer> wins;

    public WinStreak(final HashMap<MinigameType, Integer> wins) {
        this.wins = wins;
    }

    public int getWins(final MinigameType type) {
        return wins.get(type);
    }

}
