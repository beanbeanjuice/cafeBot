package com.beanbeanjuice.cafeapi.wrapper.endpoints.polls;

import lombok.Getter;

import java.sql.Timestamp;

/**
 * A class used for {@link Poll} data.
 *
 * @author beanbeanjuice
 */
public class Poll {

    @Getter private final String messageID;
    @Getter private final Timestamp endingTime;

    /**
     * Creates a new {@link Poll} object.
     * @param messageID The {@link String messageID} of the {@link Poll}.
     * @param endingTime The {@link Timestamp endingTime} of the {@link Poll}. UTC Timezone.
     */
    public Poll(final String messageID, final Timestamp endingTime) {
        this.messageID = messageID;
        this.endingTime = endingTime;
    }

}
