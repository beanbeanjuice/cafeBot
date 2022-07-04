package com.beanbeanjuice.utility.section.cafe;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.beanbeanjuice.utility.time.Time;
import com.beanbeanjuice.utility.time.TimestampDifference;
import io.github.beanbeanjuice.cafeapi.cafebot.cafe.CafeUser;
import io.github.beanbeanjuice.cafeapi.cafebot.words.Word;
import io.github.beanbeanjuice.cafeapi.exception.AuthorizationException;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.ResponseException;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Random;

/**
 * A class used for handling everything to do with beanCoins.
 *
 * @author beanbeanjuice
 */
public class ServeHandler {

    private static final Integer TIP_MINIMUM = 5;
    private static final Integer TIP_MAXIMUM = 20;
    private static final Double TIP_MULTIPLIER = 1.125;
    private static final Double TIP_DIVIDER = 0.85;
    private static final Integer LENGTH_UNTIL_MULTIPLIER = 3;
    private static final Integer USAGE_AMOUNT_DIVIDE = 500;
    private static final Integer MINUTES_UNTIL_CAN_SERVE = 60;
    private static final Integer LETTER_STOP_AMOUNT = 20;

    /**
     * Calculates the {@link Double tip} to be given for a {@link String word}.
     * @param word The {@link String word} specified.
     * @return The calculated {@link Double tip}.
     */
    public static Double calculateTip(@NotNull Word word) {
        int length = word.getWord().length();
        int uses = word.getUses();

        // Randomly Between $5-$20
        Random random = new Random();
        double tip = TIP_MINIMUM + (TIP_MAXIMUM - TIP_MINIMUM) * random.nextDouble();
        double addedTip = 0.0;

        // If the word length is above 3, then x1.125 per letter.
        if (length > LENGTH_UNTIL_MULTIPLIER) {
            for (int i = 0; i < (length - LENGTH_UNTIL_MULTIPLIER); i++) {
                // Stop after 15.
                if (i > LETTER_STOP_AMOUNT) {
                    break;
                }
                addedTip += ((tip + addedTip) * TIP_MULTIPLIER) - (tip + addedTip);
            }

            // Get the uses divided by 10 and however many times that is, do the added tip x0.85.
            for (int i = 0; i < uses / USAGE_AMOUNT_DIVIDE; i++) {
                // Stop after 15.
                if (i > LETTER_STOP_AMOUNT) {
                    break;
                }
                addedTip *= TIP_DIVIDER;
            }
        }
        return tip + addedTip;
    }

    /**
     * Gets a specified {@link CafeUser}.
     * @param user The {@link User} of the {@link CafeUser}.
     * @return The {@link CafeUser} specified. Null, if there was an error.
     */
    @Nullable
    public static CafeUser getCafeUser(@NotNull User user) {
        return getCafeUser(user.getId());
    }

    /**
     * Gets a specified {@link CafeUser}.
     * @param userID The {@link String userID} of the {@link CafeUser}.
     * @return The {@link CafeUser} specified. Null, if there was an error.
     */
    @Nullable
    public static CafeUser getCafeUser(@NotNull String userID) {

        // Tries to create the user first. If there is an error, it catches the error that says
        // a user already exists.
        try {
            Bot.getCafeAPI().CAFE_USER.createCafeUser(userID);
        } catch (ConflictException ignored) {}
        catch (AuthorizationException | ResponseException e) {
            Bot.getLogger().log(ServeHandler.class, LogLevel.ERROR, "Error Creating User: " + e.getMessage(), e);
        }

        // Now tries to retrieve the user.
        try {
            return Bot.getCafeAPI().CAFE_USER.getCafeUser(userID);
        } catch (CafeException e) {
            Bot.getLogger().log(ServeHandler.class, LogLevel.ERROR, "Error Getting User: " + e.getMessage());
        }

        return null;
    }

    /**
     * Gets the amount of {@link Integer minutes} until a {@link CafeUser} can serve again.
     * @param cafeUser The specified {@link CafeUser}.
     * @return The {@link Integer minutes} until a {@link CafeUser} can serve again.
     */
    @NotNull
    public static Integer minutesBetween(@NotNull CafeUser cafeUser) {
        if (cafeUser.getLastServingTime() == null) {
            return MINUTES_UNTIL_CAN_SERVE + 10;
        }

        Timestamp currentTimestamp = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        try {
            return Math.round(Time.compareTwoTimeStamps(cafeUser.getLastServingTime(), currentTimestamp, TimestampDifference.MINUTES));
        } catch (UnsupportedTemporalTypeException e) {
            return MINUTES_UNTIL_CAN_SERVE + 10;
        }
    }

    /**
     * Checks if a {@link CafeUser} can serve.
     * @param cafeUser The {@link CafeUser} specified.
     * @return True, if the {@link CafeUser} can serve.
     */
    @NotNull
    public static Boolean canServe(@NotNull CafeUser cafeUser) {
        return minutesBetween(cafeUser) >= MINUTES_UNTIL_CAN_SERVE;
    }

    /**
     * Gets the {@link Integer minutes} until a {@link CafeUser} can serve again.
     * @return The {@link Integer minutes}.
     */
    @NotNull
    public static Integer getMinutesUntilCanServe() {
        return MINUTES_UNTIL_CAN_SERVE;
    }

}
