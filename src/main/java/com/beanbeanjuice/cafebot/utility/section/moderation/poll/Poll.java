package com.beanbeanjuice.cafebot.utility.section.moderation.poll;

import com.beanbeanjuice.cafeapi.wrapper.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.wrapper.utility.Time;
import com.beanbeanjuice.cafeapi.wrapper.utility.TimestampDifference;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

/**
 * A class used for {@link Poll} objects.
 *
 * @author beanbeanjuice
 */
public class Poll extends com.beanbeanjuice.cafeapi.wrapper.cafebot.polls.Poll {

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
        Timestamp currentTime = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis()).toString()).orElseThrow();

        return Time.compareTwoTimeStamps(
                getEndingTime(),
                currentTime,
                TimestampDifference.SECONDS) > 0;
    }

}
