package com.beanbeanjuice.utility.sections.games;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;

/**
 * A handler used for {@link MiniGame} win streaks.
 *
 * @author beanbeanjuice
 */
public class WinStreakHandler {

    /**
     * Gets the {@link MiniGame} win streaks for a specified {@link net.dv8tion.jda.api.entities.User User}.
     * @param userID The ID of the {@link net.dv8tion.jda.api.entities.User User}.
     * @param minigame The {@link MiniGame} to get the win streaks for.
     * @return The amount of win streaks the user has for the specified {@link MiniGame}. Null, if there is an error.
     */
    @Nullable
    public Integer getUserWinStreak(@NotNull String userID, @NotNull MiniGame minigame) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.minigames_win_streaks WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(userID));
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            // Creates a new row if the user does not exist.
            try {
                resultSet.getLong(1);
            } catch (SQLException e) {
                if (!addUser(userID)) {
                    return null;
                }
                return 0;
            }

            return resultSet.getInt(minigame.getRowName());
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Retrieve User Win Streak: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Adds a new row to the database.
     * @param userID The ID of the specified {@link net.dv8tion.jda.api.entities.User User}.
     * @return True, if the row was successfully added.
     */
    @NotNull
    private Boolean addUser(@NotNull String userID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "INSERT INTO cafeBot.minigames_win_streaks (user_id) VALUES (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(userID));
            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Add User to Win Streaks: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link MiniGame} win streak for a specified {@link net.dv8tion.jda.api.entities.User User}.
     * @param userID The ID of the {@link net.dv8tion.jda.api.entities.User User}.
     * @param miniGame The {@link MiniGame} to get the win streak for.
     * @param newStreak The new win streak to set it to.
     * @return True, if successfully updated.
     */
    @NotNull
    public Boolean setUserWinStreak(@NotNull String userID, @NotNull MiniGame miniGame, @NotNull Integer newStreak) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.minigames_win_streaks SET " + miniGame.getRowName() + " = (?) WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setInt(1, newStreak);
            statement.setLong(2, Long.parseLong(userID));
            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Set User Win Streak: " + e.getMessage(), e);
            return false;
        }
    }

}
