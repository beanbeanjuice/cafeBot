package com.beanbeanjuice.utility.sections.fun.poll;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
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

        return CafeBot.getGeneralHelper().compareTwoTimeStamps(
                getEndingTime(),
                currentTime,
                TimestampDifference.SECONDS) > 0;
    }

}
