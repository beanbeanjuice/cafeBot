package com.beanbeanjuice.utility.sections.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class BeanCoinDonationHandler {

    private final HashMap<String, Timestamp> beanCoinDonationUsersCache;

    public BeanCoinDonationHandler() {
        beanCoinDonationUsersCache = new HashMap<>();
        cacheBeanCoinDonationUsers();
        startTimer();
    }

    private void startTimer() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                beanCoinDonationUsersCache.forEach((userID, timeStamp) -> {
                    if (CafeBot.getGeneralHelper().compareTwoTimeStamps(timeStamp, new Timestamp(System.currentTimeMillis()), TimestampDifference.SECONDS) <= 0) {
                        beanCoinDonationUsersCache.remove(userID);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private void cacheBeanCoinDonationUsers() {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.beancoin_donation_users;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(arguments);

            while (resultSet.next()) {
                beanCoinDonationUsersCache.put(String.valueOf(resultSet.getLong(1)), resultSet.getTimestamp(2));
            }
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Caching beanCoin Donation Users: " + e.getMessage(), e);
        }
    }

    public Boolean addUser(@NotNull String userID) {
        if (!beanCoinDonationUsersCache.containsKey(userID)) {
            Connection connection = CafeBot.getSQLServer().getConnection();
            String arguments = "INSERT INTO cafeBot.beancoin_donation_users (user_id, time_until_next_donation) VALUES (?,?);";

            try {
                PreparedStatement statement = connection.prepareStatement(arguments);
                statement.setLong(1, Long.parseLong(userID));

                Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
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
     * Gets the amount of seconds you have until you can donate to another person.
     * @param userID The ID of the {@link net.dv8tion.jda.api.entities.User}.
     * @return The amount of seconds until the {@link net.dv8tion.jda.api.entities.User} can donate again as a {@link Long}.
     */
    @NotNull
    public Long timeUntilDonate (@NotNull String userID) {
        if (!beanCoinDonationUsersCache.containsKey(userID)) {
            return 0L;
        }
        return CafeBot.getGeneralHelper().compareTwoTimeStamps(beanCoinDonationUsersCache.get(userID), new Timestamp(System.currentTimeMillis()), TimestampDifference.SECONDS);
    }

}
