package com.beanbeanjuice.cafebot.utility.section.game;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks.MinigameType;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks.WinStreak;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A handler used for {@link WinStreak}.
 *
 * @author beanbeanjuice
 */
public class WinStreakHandler {

    /**
     * Retrieves a {@link WinStreak} for a specified {@link String userID}.
     * @param userID The {@link String userID} specified.
     * @return The {@link WinStreak} for the specified {@link String userID}. Null, if there was an error.
     */
    @Nullable
    public static WinStreak getUserWinStreak(@NotNull String userID) {
        try {
            return Bot.getCafeAPI().WIN_STREAK.getUserWinStreak(userID);
        } catch (NotFoundException e) {

            if (!createUserWinStreak(userID))
                return null;

            return new WinStreak(0, 0);
        } catch (CafeException e) {
            Bot.getLogger().log(WinStreakHandler.class, LogLevel.WARN, "Unable to Retrieve Win Streak: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Retrieves a specific {@link WinStreak} for a {@link MinigameType} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param type The {@link MinigameType} to get the {@link WinStreak} for.
     * @return The {@link Integer winStreak} for the specific {@link MinigameType} for the specified {@link String userID}. Null, if there was an error.
     */
    @Nullable
    public static Integer getUserWinStreak(@NotNull String userID, @NotNull MinigameType type) {
        try {
            if (type == MinigameType.CONNECT_FOUR)
                return getUserWinStreak(userID).getConnectFourWins();

            if (type == MinigameType.TIC_TAC_TOE)
                return getUserWinStreak(userID).getTicTacToeWins();

            return null;
        } catch (NullPointerException e) {
            Bot.getLogger().log(WinStreakHandler.class, LogLevel.WARN, "Winstreak Is Null: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Creates a new {@link WinStreak} for the {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link WinStreak} was created successfully.
     */
    @NotNull
    public static Boolean createUserWinStreak(@NotNull String userID) {
        try {
            Bot.getCafeAPI().WIN_STREAK.createUserWinStreak(userID);
            return true;
        } catch (ConflictException e) {
            return false;
        } catch (CafeException e) {
            Bot.getLogger().log(WinStreakHandler.class, LogLevel.ERROR, "Error Creating Win Streak: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Updates a {@link MinigameType} {@link WinStreak} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @param gameType The {@link MinigameType gameType}.
     * @param winStreak The new {@link Integer winStreak}.
     * @return True, if the {@link WinStreak} was successfully updated for the {@link MinigameType} for the {@link String userID}.
     */
    @NotNull
    public static Boolean updateUserWinStreak(@NotNull String userID, @NotNull MinigameType gameType, @NotNull Integer winStreak) {
        try {
            return Bot.getCafeAPI().WIN_STREAK.updateUserWinStreak(userID, gameType, winStreak);
        } catch (CafeException e) {
            Bot.getLogger().log(WinStreakHandler.class, LogLevel.ERROR, "Error Setting Win Streak: " + e.getMessage(), e);
            return false;
        }
    }

}
