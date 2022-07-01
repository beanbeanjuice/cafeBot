package com.beanbeanjuice.utility;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.logging.LogLevel;
import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * A general class used for everything.
 *
 * @author beanbeanjuice
 */
public class Helper {

    /**
     * Shorten the message to a certain limit.
     * @param message The contents of the message.
     * @param limit The limit of the new message.
     * @return The new, limited message.
     */
    @NotNull
    public static String shortenToLimit(@NotNull String message, @NotNull Integer limit) {
        message = message.replace("\"", "\\\"");
        if (message.length() >= limit) {
            return message.substring(0, limit - 3) + "...";
        }

        return message;
    }

    /**
     * Formats the specified time, including days.
     * @param timeInMillis The time as a {@link Long} value.
     * @return The formatted time {@link String}.
     */
    @NotNull
    public static String millisToDays(@NotNull Long timeInMillis) {
        final long days = timeInMillis / TimeUnit.DAYS.toMillis(1);
        final long hours = timeInMillis % TimeUnit.DAYS.toMillis(1) / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis % TimeUnit.DAYS.toMillis(1) % TimeUnit.HOURS.toMillis(1) / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.DAYS.toMillis(1) % TimeUnit.HOURS.toMillis(1) % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
    }

    /**
     * @return A random {@link Color}.
     */
    @NotNull
    public static Color getRandomColor() {
        Random random = new Random();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        return new Color(r, g, b);
    }

    /**
     * Updates the {@link CafeAPI} every hour.
     */
    public static void startCafeAPIRefreshTimer(@NotNull RequestLocation requestLocation) {
        Timer cafeAPITimer = new Timer();
        TimerTask cafeAPITimerTask = new TimerTask() {

            @Override
            public void run() {
                Bot.setCafeAPI(new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), requestLocation));
                Bot.getLogger().log(this.getClass(), LogLevel.INFO, "Updated the CafeAPI Token... Valid for 3600 Seconds", true, false);
            }
        };
        cafeAPITimer.scheduleAtFixedRate(cafeAPITimerTask, 0, 3400000);
    }

}