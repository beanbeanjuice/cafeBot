package com.beanbeanjuice.cafebot.endpoints.discord.server;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.PartialPoll;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.PartialPollOption;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.Poll;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PollApiTest extends ApiTest {

    private Poll poll;
    private String user1;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException {
        String guildId = generateSnowflake().toString();
        String messageId = generateSnowflake().toString();
        String user1 =  generateSnowflake().toString();

        List<PartialPollOption> options = new ArrayList<>();

        options.add(new PartialPollOption("🥺", "Poll Option #1", "Poll Description"));
        options.add(new PartialPollOption(null, "Second Option", null));

        PartialPoll partialPoll = new PartialPoll(
                "Example Title",
                "Example Description",
                true,
                Instant.now().plus(2, ChronoUnit.SECONDS).toString(),
                options.toArray(new PartialPollOption[0])
        );

        this.poll = cafeAPI.getPollApi().createPoll(guildId, messageId, partialPoll).get();
        this.poll = cafeAPI.getPollApi().toggleVote(this.poll.getId(), this.poll.getOptions()[0].getId(), user1).get();
    }

    @Test
    @DisplayName("can get all polls")
    public void testCanGetAllPolls() throws ExecutionException, InterruptedException {
        Thread.sleep(Duration.of(3, ChronoUnit.SECONDS).toMillis());

        Map<String, List<Poll>> polls = cafeAPI.getPollApi().getPolls().get();

        Assertions.assertNotNull(polls.get(this.poll.getGuildId()));
        Assertions.assertEquals(1, polls.get(this.poll.getGuildId()).size());
        Assertions.assertNotNull(polls.get(this.poll.getGuildId()).getFirst());
    }

    @Test
    @DisplayName("can get polls for guild")
    public void testCanGetSpecificPoll() throws ExecutionException, InterruptedException {
        Thread.sleep(Duration.of(3, ChronoUnit.SECONDS).toMillis());

        List<Poll> polls = cafeAPI.getPollApi().getPolls(this.poll.getGuildId()).get();

        Assertions.assertEquals(1, polls.size());
        Assertions.assertNotNull(polls.getFirst());
    }

    @Test
    @DisplayName("can create poll")
    public void testCanCreatePoll() throws ExecutionException, InterruptedException {
        String guildId = generateSnowflake().toString();
        String messageId = generateSnowflake().toString();

        List<PartialPollOption> options = new ArrayList<>();

        options.add(new PartialPollOption("🥺", "Poll Option #1", "Poll Description"));
        options.add(new PartialPollOption(null, "Second Option", null));

        PartialPoll partialPoll = new PartialPoll(
                "Example Title",
                "Example Description",
                true,
                Instant.now().plus(2, ChronoUnit.SECONDS).toString(),
                options.toArray(new PartialPollOption[0])
        );

        Poll poll = cafeAPI.getPollApi().createPoll(guildId, messageId, partialPoll).get();

        Assertions.assertNotNull(poll);
        Arrays.stream(poll.getResults()).forEach(option -> {
            Assertions.assertTrue(Arrays.stream(option.getVoters()).toList().isEmpty());
        });
        Arrays.stream(poll.getOptions()).forEach(option -> {
            Assertions.assertTrue(Arrays.stream(option.getVoters()).toList().isEmpty());
        });
        Assertions.assertEquals("Example Title", poll.getTitle());
        Assertions.assertTrue(poll.getDescription().isPresent());
        Assertions.assertEquals("Example Description", poll.getDescription().get());

        Assertions.assertEquals(2, poll.getOptions()[0].getEmoji().get().length());
        Assertions.assertEquals("🥺", poll.getOptions()[0].getEmoji().get());
        Assertions.assertEquals("Poll Option #1", poll.getOptions()[0].getTitle());
        Assertions.assertTrue(poll.getOptions()[0].getDescription().isPresent());
        Assertions.assertEquals("Poll Description", poll.getOptions()[0].getDescription().get());

        Assertions.assertFalse(poll.getOptions()[1].getEmoji().get().isBlank());
        Assertions.assertEquals("Second Option", poll.getOptions()[1].getTitle());
        Assertions.assertFalse(poll.getOptions()[1].getDescription().isPresent());
    }

    @Test
    @DisplayName("can vote for poll")
    public void testCanVoteForPoll() throws ExecutionException, InterruptedException {
        String user = generateSnowflake().toString();

        Poll poll = cafeAPI.getPollApi().toggleVote(this.poll.getId(), this.poll.getOptions()[1].getId(), user).get();

        Assertions.assertNotNull(poll);
        Assertions.assertTrue(Arrays.stream(poll.getOptions()[1].getVoters()).anyMatch((voters) -> voters.equalsIgnoreCase(user)));
    }

    @Test
    @DisplayName("can close poll")
    public void testCanClosePoll() throws ExecutionException, InterruptedException {
        Thread.sleep(Duration.of(3, ChronoUnit.SECONDS).toMillis());

        Poll poll = cafeAPI.getPollApi().closePoll(this.poll.getId()).get();

        Assertions.assertNotNull(poll);
        Assertions.assertFalse(poll.isActive());
        Assertions.assertEquals(1, poll.getResults().length);
    }

    @Test
    @DisplayName("can delete poll")
    public void testCanDeletePoll() throws ExecutionException, InterruptedException {
        List<Poll> polls = cafeAPI.getPollApi().getPolls(this.poll.getGuildId(), true, false).get();
        Assertions.assertEquals(1, polls.size());

        cafeAPI.getPollApi().deletePoll(this.poll.getId()).join();

        polls = cafeAPI.getPollApi().getPolls(this.poll.getGuildId()).get();
        Assertions.assertEquals(0, polls.size());
    }

    @Test
    @DisplayName("can get poll by message id")
    public void testCanGetPollById() throws ExecutionException, InterruptedException {
        Poll poll = cafeAPI.getPollApi().getPoll(this.poll.getGuildId(), this.poll.getMessageId()).get();

        Assertions.assertNotNull(poll);
        Assertions.assertEquals(poll.getId(), this.poll.getId());
        Assertions.assertEquals(poll.getTitle(), this.poll.getTitle());
        Assertions.assertEquals(poll.getDescription(), this.poll.getDescription());
    }

    @Test
    @DisplayName("can manually set poll submission to false")
    public void testCanSetPollSubmissionToFalse() throws ExecutionException, InterruptedException {
        int originalVotes = poll.getOptions()[0].getVoters().length;

        String user = generateSnowflake().toString();
        Poll poll = cafeAPI.getPollApi().setVote(this.poll.getId(), this.poll.getOptions()[0].getId(), user, true).get();

        Assertions.assertEquals(originalVotes + 1, poll.getOptions()[0].getVoters().length);
    }

    @Test
    @DisplayName("can manually set poll submission to true")
    public void testCanSetPollSubmissionToTrue() throws ExecutionException, InterruptedException {
        int originalVotes = poll.getOptions()[0].getVoters().length;

        String user = generateSnowflake().toString();
        Poll poll = cafeAPI.getPollApi().setVote(this.poll.getId(), this.poll.getOptions()[0].getId(), user, false).get();

        Assertions.assertEquals(originalVotes, poll.getOptions()[0].getVoters().length);
    }

}
