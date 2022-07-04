package com.beanbeanjuice.utility.handler.fun.poll;

import com.beanbeanjuice.utility.time.Time;
import com.beanbeanjuice.utility.time.TimestampDifference;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

/**
 * A class used for {@link Poll} objects.
 *
 * @author beanbeanjuice
 */
public class Poll extends io.github.beanbeanjuice.cafeapi.cafebot.polls.Poll {

    /**
     * Creates a new {@link Poll}.
     * @param messageID The {@link String messageID} of the {@link Poll}.
     * @param endingTime The {@link Timestamp endingTime}.
     */
    public Poll(@NotNull String messageID, @NotNull Timestamp endingTime) {
        super(messageID, endingTime);
    }

    /**
     * @return True, if the {@link Poll} is finished.
     */
    @NotNull
    public Boolean isFinished() {
        Timestamp currentTime = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        return Time.compareTwoTimeStamps(
                getEndingTime(),
                currentTime,
                TimestampDifference.SECONDS) > 0;
    }

}