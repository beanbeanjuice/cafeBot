package com.beanbeanjuice.utility.sections.fun.raffle;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

/**
 * A custom {@link Raffle} class.
 *
 * @author beanbeanjuice
 */
public class Raffle extends io.github.beanbeanjuice.cafeapi.cafebot.raffles.Raffle {

    /**
     * Creates a new {@link Raffle}.
     * @param messageID The {@link String messageID} of the {@link Raffle}.
     * @param endingTime The {@link Timestamp endingTime} of the {@link Raffle}.
     * @param winnerAmount The {@link Integer winnerAmount} for the {@link Raffle}.
     */
    public Raffle(@NotNull String messageID, @NotNull Timestamp endingTime, @NotNull Integer winnerAmount) {
        super(messageID, endingTime, winnerAmount);
    }

    /**
     * @return True, if the {@link Raffle} is finished.
     */
    @NotNull
    public Boolean isFinished() {
        Timestamp currentTime = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        return CafeBot.getGeneralHelper().compareTwoTimeStamps(
                getEndingTime(),
                currentTime,
                TimestampDifference.SECONDS) > 0;
    }

}
