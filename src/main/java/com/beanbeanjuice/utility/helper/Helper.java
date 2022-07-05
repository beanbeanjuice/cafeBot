package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.logging.LogLevel;
import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        // TODO: There must be a way to do this. Somehow wait until cafeAPI is not null anymore.
        Bot.setCafeAPI(new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), requestLocation));
        Bot.getLogger().log(Helper.class, LogLevel.INFO, "Updated the CafeAPI Token... Valid for 3600 Seconds", true, false);

        Timer cafeAPITimer = new Timer();
        TimerTask cafeAPITimerTask = new TimerTask() {

            @Override
            public void run() {
                Bot.setCafeAPI(new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), requestLocation));
                Bot.getLogger().log(Helper.class, LogLevel.INFO, "Updated the CafeAPI Token... Valid for 3600 Seconds", true, false);
            }
        };
        cafeAPITimer.scheduleAtFixedRate(cafeAPITimerTask, 3400000, 3400000);
    }

    /**
     * Check if a {@link String} is a number.
     * @param check The {@link String} to check.
     * @return True, if the {@link String} is a number.
     */
    @NotNull
    public static Boolean isNumber(@NotNull String check) {
        try {
            Integer.parseInt(check);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @return The SQL Server Error {@link MessageEmbed}.
     */
    @NotNull
    public static MessageEmbed sqlServerError() {
        return sqlServerError(null);
    }

    /**
     * @param optionalMessage An optional message to add to the {@link MessageEmbed}.
     * @return The SQL Server Error {@link MessageEmbed}.
     */
    @NotNull
    public static MessageEmbed sqlServerError(@Nullable String optionalMessage) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setTitle("Connection Error");

        String description = "The bot is unable to connect to the SQL database. Please try again later.";

        if (optionalMessage != null) {
            description += " - " + optionalMessage;
        }
        embedBuilder.setDescription(description);
        return embedBuilder.build();
    }

    /**
     * Creates an error {@link MessageEmbed}.
     * @param title The title for the {@link MessageEmbed}.
     * @param description The description for the {@link MessageEmbed}.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    public static MessageEmbed errorEmbed(@NotNull String title, @NotNull String description) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    /**
     * Creates a success {@link MessageEmbed}.
     * @param title The title for the {@link MessageEmbed}.
     * @param description The description for the {@link MessageEmbed}.
     * @return The creates {@link MessageEmbed}.
     */
    @NotNull
    public static MessageEmbed successEmbed(@NotNull String title, @NotNull String description) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(getRandomColor());
        return embedBuilder.build();
    }

    /**
     * Gets a {@link Role} from the ID.
     * @param guild The {@link Guild} that contains the {@link Role}.
     * @param roleID The ID of the {@link Role}.
     * @return The {@link Role}.
     */
    @Nullable
    public static Role getRole(@NotNull Guild guild, @NotNull String roleID) {
        roleID = roleID.replace("<@&", "");
        roleID = roleID.replace("<@", "");
        roleID = roleID.replace(">", "");

        try {
            return guild.getRoleById(roleID);
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * Round the specified {@link Double} to 2 decimal places.
     * @param amount The {@link Double} to round.
     * @return The rounded {@link Double}.
     */
    @NotNull
    public static Double roundDouble(@NotNull Double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    /**
     * Get a random number.
     * @param minimum The minimum {@link Integer}.
     * @param maximum The maximum {@link Integer}.
     * @return The random {@link Integer}.
     */
    @NotNull
    public static Integer getRandomNumber(@NotNull Integer minimum, @NotNull Integer maximum) {
        return (int) ((Math.random() * (maximum - minimum)) + minimum);
    }

    /**
     * Private message's a specified {@link User}.
     * @param user The {@link User} to be messaged.
     * @param message The contents of the message.
     */
    public static void pmUser(@NotNull User user, @NotNull String message) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }

    /**
     * Private messages a specified {@link User}.
     * @param user The {@link User} to be messaged.
     * @param embed The {@link MessageEmbed} to be sent.
     */
    public static void pmUser(@NotNull User user, @NotNull MessageEmbed embed) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embed)).queue();
    }

    /**
     * Gets a {@link User} from the ID.
     * @param userID The ID of the {@link User}.
     * @return The {@link User}.
     */
    @Nullable
    public static User getUser(@NotNull String userID) {
        userID = userID.replace("<@!", "");
        userID = userID.replace("<@", ""); // Edge Case for Mobile
        userID = userID.replace(">", "");

        try {
            return Bot.getBot().getUserById(userID);
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * Generates a Random Alpha-Numeric {@link String}.
     * @param n The length of the random alphanumeric {@link String}.
     * @return The randomly generated alphanumeric {@link String}.
     */
    @NotNull
    public static String getRandomAlphaNumericString(@NotNull Integer n) {

        // Chooses a Random Character from This String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // Creates a String Builder with the specified size
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}
