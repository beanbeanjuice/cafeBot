package com.beanbeanjuice.utility.sections.games;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import io.github.beanbeanjuice.cafeapi.cafebot.minigames.winstreaks.MinigameType;
import io.github.beanbeanjuice.cafeapi.cafebot.minigames.winstreaks.WinStreak;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
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
    public WinStreak getUserWinStreak(@NotNull String userID) {
        try {
            return CafeBot.getCafeAPI().winStreaks().getUserWinStreak(userID);
        } catch (NotFoundException e) {

            if (!createUserWinStreak(userID)) {
                return null;
            }

            return new WinStreak(0, 0);
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Retrieve Win Streak: " + e.getMessage(), e);
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
    public Integer getUserWinStreak(@NotNull String userID, @NotNull MinigameType type) {
        try {
            if (type == MinigameType.CONNECT_FOUR) {
                return getUserWinStreak(userID).getConnectFourWins();
            }

            if (type == MinigameType.TIC_TAC_TOE) {
                return getUserWinStreak(userID).getTicTacToeWins();
            }

            return null;
        } catch (NullPointerException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Winstreak Is Null: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Creates a new {@link WinStreak} for the {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link WinStreak} was created successfully.
     */
    @NotNull
    public Boolean createUserWinStreak(@NotNull String userID) {
        try {
            CafeBot.getCafeAPI().winStreaks().createUserWinStreak(userID);
            return true;
        } catch (ConflictException e) {
            return false;
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Creating Win Streak: " + e.getMessage(), e);
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
    public Boolean updateUserWinStreak(@NotNull String userID, @NotNull MinigameType gameType, @NotNull Integer winStreak) {
        try {
            return CafeBot.getCafeAPI().winStreaks().updateUserWinStreak(userID, gameType, winStreak);
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Setting Win Streak: " + e.getMessage(), e);
            return false;
        }
    }

}
