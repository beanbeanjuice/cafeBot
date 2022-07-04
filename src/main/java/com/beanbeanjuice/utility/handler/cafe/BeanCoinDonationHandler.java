package com.beanbeanjuice.utility.handler.cafe;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.beanbeanjuice.utility.time.Time;
import com.beanbeanjuice.utility.time.TimestampDifference;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
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
     * Caches the {@link io.github.beanbeanjuice.cafeapi.cafebot.beancoins.users.DonationUsers}.
     */
    private void cacheBeanCoinDonationUsers() {
        try {
            beanCoinDonationUsersCache = Bot.getCafeAPI().DONATION_USER.getAllUserDonationTimes();
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Getting Donation Users Cooldowns: " + e.getMessage(), e);
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
    public Long timeUntilDonate (@NotNull String userID) {
        if (!beanCoinDonationUsersCache.containsKey(userID)) {
            return -1L;
        }

        // Converts to UTC time.
        Timestamp currentTime = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        return Time.compareTwoTimeStamps(currentTime, beanCoinDonationUsersCache.get(userID), TimestampDifference.MINUTES);
    }

    /**
     * Adds a {@link String userID} to the {@link HashMap cache}.
     * @param userID The specified {@link String userID}.
     * @param endingTime The new {@link Timestamp endingTime}.
     * @return True, if added successfully.
     */
    @NotNull
    public Boolean addUser(@NotNull String userID, @NotNull Timestamp endingTime) {
        beanCoinDonationUsersCache.put(userID, endingTime);
        return true;
    }

    /**
     * @return The {@link Integer cooldown} in minutes.
     */
    @NotNull
    public Integer getCooldown() {
        return MINUTES * 60000;
    }

}
