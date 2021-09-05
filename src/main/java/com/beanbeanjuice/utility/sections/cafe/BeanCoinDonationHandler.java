package com.beanbeanjuice.utility.sections.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.exception.CafeException;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A handler used for beanCoin donations.
 *
 * @author beanbeanjuice
 */
public class BeanCoinDonationHandler {

    private HashMap<String, Timestamp> beanCoinDonationUsersCache;
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
     * Caches the {@link com.beanbeanjuice.cafeapi.cafebot.beancoins.users.DonationUsers}.
     */
    private void cacheBeanCoinDonationUsers() {
        try {
            beanCoinDonationUsersCache = CafeBot.getCafeAPI().donationUsers().getAllUserDonationTimes();
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Getting Donation Users Cooldowns: " + e.getMessage(), e);
        }
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
                        try {
                            CafeBot.getCafeAPI().donationUsers().deleteDonationUser(userID);
                        } catch (CafeException e) {
                            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Remove User from Donation Cooldowns: " + e.getMessage(), e);
                            beanCoinDonationUsersCache.remove(userID);
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 30000);
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
