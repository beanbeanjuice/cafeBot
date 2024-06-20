package com.beanbeanjuice.cafebot.utility.section.cafe;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafeapi.wrapper.cafebot.beancoins.users.DonationUsers;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;
import com.beanbeanjuice.cafeapi.wrapper.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.wrapper.utility.Time;
import com.beanbeanjuice.cafeapi.wrapper.utility.TimestampDifference;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A handler used for beanCoin donations.
 *
 * @author beanbeanjuice
 */
public class BeanCoinDonationHandler {

    private static HashMap<String, Timestamp> beanCoinDonationUsersCache;
    private static final int MINUTES = 60;

    /**
     * Start the {@link BeanCoinDonationHandler}.
     */
    public static void start() {
        beanCoinDonationUsersCache = new HashMap<>();
        cacheBeanCoinDonationUsers();
        startTimer();
    }

    /**
     * Caches the {@link DonationUsers}.
     */
    private static void cacheBeanCoinDonationUsers() {
        try {
            beanCoinDonationUsersCache = Bot.getCafeAPI().DONATION_USER.getAllUserDonationTimes();
        } catch (CafeException e) {
            Bot.getLogger().log(BeanCoinDonationHandler.class, LogLevel.ERROR, "Error Getting Donation Users Cooldowns: " + e.getMessage(), e);
        }
    }

    /**
     * Starts the {@link Timer}.
     */
    private static void startTimer() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                // Iterates Through Everything in the HashMap
                new HashMap<>(beanCoinDonationUsersCache).forEach((userID, timeStamp) -> {
                    if (timeUntilDonate(userID) <= -1) {
                        try {
                            Bot.getCafeAPI().DONATION_USER.deleteDonationUser(userID);
                        } catch (CafeException e) {
                            Bot.getLogger().log(this.getClass(), LogLevel.WARN, "Unable to Remove User from Donation Cooldowns: " + e.getMessage(), e);
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
    public static Long timeUntilDonate (@NotNull String userID) {
        if (!beanCoinDonationUsersCache.containsKey(userID)) {
            return -1L;
        }

        // Converts to UTC time.
        Timestamp currentTime = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis()).toString()).orElseThrow();
        return Time.compareTwoTimeStamps(currentTime, beanCoinDonationUsersCache.get(userID), TimestampDifference.MINUTES);
    }

    /**
     * Adds a {@link String userID} to the {@link HashMap cache}.
     * @param userID The specified {@link String userID}.
     * @param endingTime The new {@link Timestamp endingTime}.
     * @return True, if added successfully.
     */
    @NotNull
    public static Boolean addUser(@NotNull String userID, @NotNull Timestamp endingTime) {
        beanCoinDonationUsersCache.put(userID, endingTime);
        return true;
    }

    /**
     * @return The {@link Integer cooldown} in minutes.
     */
    @NotNull
    public static Integer getCooldown() {
        return MINUTES * 60000;
    }

}
