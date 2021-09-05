package com.beanbeanjuice.utility.sections.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.cafebot.cafe.CafeUser;
import com.beanbeanjuice.cafeapi.cafebot.words.Word;
import com.beanbeanjuice.cafeapi.exception.AuthorizationException;
import com.beanbeanjuice.cafeapi.exception.CafeException;
import com.beanbeanjuice.cafeapi.exception.ConflictException;
import com.beanbeanjuice.cafeapi.exception.ResponseException;
import com.beanbeanjuice.cafeapi.generic.CafeGeneric;
import com.beanbeanjuice.utility.sections.cafe.object.CafeCustomer;
import com.beanbeanjuice.utility.sections.cafe.object.ServeWord;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Random;

public class ServeHandler {

    private final Integer TIP_MINIMUM = 5;
    private final Integer TIP_MAXIMUM = 20;
    private final Double TIP_MULTIPLIER = 1.125;
    private final Double TIP_DIVIDER = 0.85;
    private final Integer LENGTH_UNTIL_MULTIPLIER = 3;
    private final Integer USAGE_AMOUNT_DIVIDE = 500;
    private final Integer MINUTES_UNTIL_CAN_SERVE = 60;
    private final Integer LETTER_STOP_AMOUNT = 20;

    public Double calculateTip(@NotNull Word word) {
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

    @Nullable
    public CafeUser getCafeUser(@NotNull User user) {
        return getCafeUser(user.getId());
    }

    @Nullable
    public CafeUser getCafeUser(@NotNull String userID) {
        try {
            CafeBot.getCafeAPI().cafeUsers().createCafeUser(userID);
        } catch (ConflictException ignored) {}
        catch (AuthorizationException | ResponseException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Creating User: " + e.getMessage(), e);
        }

        try {
            return CafeBot.getCafeAPI().cafeUsers().getCafeUser(userID);
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Getting User: " + e.getMessage());
        }

        return null;
    }

    @NotNull
    public Integer minutesBetween(@NotNull CafeUser cafeUser) {

        if (cafeUser.getLastServingTime() == null) {
            return MINUTES_UNTIL_CAN_SERVE + 10;
        }

        Timestamp currentTimestamp = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        try {
            return Math.round(CafeBot.getGeneralHelper().compareTwoTimeStamps(cafeUser.getLastServingTime(), currentTimestamp, TimestampDifference.MINUTES));
        } catch (UnsupportedTemporalTypeException e) {
            return MINUTES_UNTIL_CAN_SERVE + 10;
        }
    }

    @NotNull
    public Boolean canServe(@NotNull CafeUser cafeUser) {
        return minutesBetween(cafeUser) >= MINUTES_UNTIL_CAN_SERVE;
    }

    @NotNull
    public Integer getMinutesUntilCanServe() {
        return MINUTES_UNTIL_CAN_SERVE;
    }

}
