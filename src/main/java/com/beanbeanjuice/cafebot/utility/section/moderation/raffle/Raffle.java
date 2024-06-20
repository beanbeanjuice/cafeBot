package com.beanbeanjuice.cafebot.utility.section.moderation.raffle;

import com.beanbeanjuice.cafeapi.wrapper.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.wrapper.utility.Time;
import com.beanbeanjuice.cafeapi.wrapper.utility.TimestampDifference;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

/**
 * A custom {@link Raffle} class.
 *
 * @author beanbeanjuice
 */
public class Raffle extends com.beanbeanjuice.cafeapi.wrapper.cafebot.raffles.Raffle {

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
        Timestamp currentTime = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis()).toString()).orElseThrow();

        return Time.compareTwoTimeStamps(
                getEndingTime(),
                currentTime,
                TimestampDifference.SECONDS) > 0;
    }

}
