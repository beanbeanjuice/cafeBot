package com.beanbeanjuice.utility.sections.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sections.cafe.object.CafeCustomer;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A handler used for beanCoin donations.
 */
public class BeanCoinDonationHandler {

    private final HashMap<String, Timestamp> beanCoinDonationUsersCache;
    private final int MINUTES = 60;

    /**
     * Creates a new {@link BeanCoinDonationHandler}.
     */
    public BeanCoinDonationHandler() {
        beanCoinDonationUsersCache = new HashMap<>();
        cacheBeanCoinDonationUsers();
        startTimer();
    }

    /**
     * Starts the {@link Timer}.
     */
    private void startTimer() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                // Iterates Through Everything in the HashMap
                new HashMap<>(beanCoinDonationUsersCache).forEach((userID, timeStamp) -> {
                    if (timeUntilDonate(userID) <= -1) {
                        if (!removeUser(userID)) {
                            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Remove User from Donation Cooldowns");
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 30000);
    }

    /**
     * Updates the cache from the database.
     */
    private void cacheBeanCoinDonationUsers() {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.beancoin_donation_users;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(arguments);

            while (resultSet.next()) {
                String userID = String.valueOf(resultSet.getLong(1));
                Timestamp timestamp = resultSet.getTimestamp(2);

                beanCoinDonationUsersCache.put(userID, timestamp);
            }
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Caching beanCoin Donation Users: " + e.getMessage(), e);
        }
    }

    /**
     * Removes a specified {@link net.dv8tion.jda.api.entities.User User} from the cooldown database.
     * @param userID The ID of the {@link net.dv8tion.jda.api.entities.User User}.
     * @return True, if removed successfully.
     */
    @NotNull
    public Boolean removeUser(@NotNull String userID) {
        if (!beanCoinDonationUsersCache.containsKey(userID)) {
            return false;
        }

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "DELETE FROM cafeBot.beancoin_donation_users WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(userID));
            statement.execute();
            beanCoinDonationUsersCache.remove(userID);
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Remove User from Donation Users: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Updates the {@link CafeCustomer} in the database.
     * @param cafeCustomer THe specified {@link CafeCustomer}.
     * @param newBalance The new balance for the {@link CafeCustomer}.
     * @return True, if updated successfully.
     */
    @NotNull
    public Boolean updateCafeCustomer(@NotNull CafeCustomer cafeCustomer, @NotNull Double newBalance) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.cafe_information SET bean_coins = (?) WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setDouble(1, newBalance);
            statement.setLong(2, Long.parseLong(cafeCustomer.getUserID()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating CafeCustomer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds a {@link net.dv8tion.jda.api.entities.User User} to the cooldown database.
     * @param userID The ID of the {@link net.dv8tion.jda.api.entities.User User} to add.
     * @return True, if added successfully.
     */
    @NotNull
    public Boolean addUser(@NotNull String userID) {
        if (!beanCoinDonationUsersCache.containsKey(userID)) {
            Connection connection = CafeBot.getSQLServer().getConnection();
            String arguments = "INSERT INTO cafeBot.beancoin_donation_users (user_id, time_until_next_donation) VALUES (?,?);";

            try {
                PreparedStatement statement = connection.prepareStatement(arguments);
                statement.setLong(1, Long.parseLong(userID));

                Timestamp timeStamp = new Timestamp(System.currentTimeMillis() + (MINUTES*60000));
                statement.setTimestamp(2, timeStamp);
                statement.execute();

                beanCoinDonationUsersCache.put(userID, timeStamp);
                return true;
            } catch (SQLException e) {
                CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Add Donation User: " + e.getMessage(), e);
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Gets the amount of minutes you have until you can donate to another person.
     * @param userID The ID of the {@link net.dv8tion.jda.api.entities.User}.
     * @return The amount of minutes until the {@link net.dv8tion.jda.api.entities.User} can donate again as a {@link Long}.
     */
    @NotNull
    public Long timeUntilDonate (@NotNull String userID) {
        if (!beanCoinDonationUsersCache.containsKey(userID)) {
            return -1L;
        }
        return CafeBot.getGeneralHelper().compareTwoTimeStamps(new Timestamp(System.currentTimeMillis()), beanCoinDonationUsersCache.get(userID), TimestampDifference.MINUTES);
    }

}
