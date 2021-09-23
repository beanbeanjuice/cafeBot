package io.github.beanbeanjuice.cafeapi.beta;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.cafebot.polls.Poll;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PollTest {

    @Test
    @DisplayName("Test Polls API")
    public void pollsAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        long currentTime = System.currentTimeMillis();
        Timestamp currentTimestamp = CafeGeneric.parseTimestamp(new Timestamp(currentTime).toString());

        // Makes sure the poll doesn't exist before starting the test.
        Assertions.assertTrue(() -> cafeAPI.polls().deletePoll("798830792938881024", "879519824424890438"));

        // Makes sure that the wrapper is able to create the poll.
        Assertions.assertTrue(() -> cafeAPI.polls().createPoll("798830792938881024", new Poll("879519824424890438", currentTimestamp)));

        // Makes sure that a ConflictException is thrown when the same poll is attempted to be created twice.
        Assertions.assertThrows(ConflictException.class, () -> {
            cafeAPI.polls().createPoll("798830792938881024", new Poll("879519824424890438", currentTimestamp));
        });

        // Makes sure that the ending time retrieved from the API is the same as the one entered.
        Assertions.assertTrue(() -> {
            ArrayList<Poll> polls = cafeAPI.polls().getAllPolls().get("798830792938881024");

            for (Poll poll : polls) {
                if (poll.getEndingTime().equals(currentTimestamp)) {
                    return true;
                }
            }

            return false;
        });

        // Makes sure that the message ID retrieved from the API is the same as the one entered.
        Assertions.assertTrue(() -> {
            ArrayList<Poll> polls = cafeAPI.polls().getGuildPolls("798830792938881024");

            for (Poll poll : polls) {
                if (poll.getMessageID().equals("879519824424890438")) {
                    return true;
                }
            }

            return false;
        });

        // Makes sure a poll is able to be deleted from the API.
        Assertions.assertTrue(() -> cafeAPI.polls().deletePoll("798830792938881024", "879519824424890438"));
    }

}
