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

    private static Poll setupPoll() throws ExecutionException, InterruptedException {
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
                Instant.now().plus(1, ChronoUnit.SECONDS).toString(),
                options.toArray(new PartialPollOption[0])
        );

        Poll poll = cafeAPI.getPollApi().createPoll(guildId, messageId, partialPoll).get();
        poll = cafeAPI.getPollApi().toggleVote(poll.getId(), poll.getOptions()[0].getId(), user1).get();

        Thread.sleep(Duration.of(1, ChronoUnit.SECONDS).toMillis());

        return poll;
    }

    @Test
    @DisplayName("can get all polls")
    public void testCanGetAllPolls() throws ExecutionException, InterruptedException {
        Poll originalPoll = setupPoll();

        Map<String, List<Poll>> polls = cafeAPI.getPollApi().getPolls().get();

        Assertions.assertNotNull(polls.get(originalPoll.getGuildId()));
        Assertions.assertEquals(1, polls.get(originalPoll.getGuildId()).size());
        Assertions.assertNotNull(polls.get(originalPoll.getGuildId()).getFirst());
    }

    @Test
    @DisplayName("can get polls for guild")
    public void testCanGetSpecificPoll() throws ExecutionException, InterruptedException {
        Poll originalPoll = setupPoll();

        List<Poll> polls = cafeAPI.getPollApi().getPolls(originalPoll.getGuildId()).get();

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

        Assertions.assertEquals("🥺".length(), poll.getOptions()[0].getEmoji().get().length());
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
        Poll originalPoll = setupPoll();
        String user = generateSnowflake().toString();

        Poll poll = cafeAPI.getPollApi().toggleVote(originalPoll.getId(), originalPoll.getOptions()[1].getId(), user).get();

        Assertions.assertNotNull(poll);
        Assertions.assertTrue(Arrays.stream(poll.getOptions()[1].getVoters()).anyMatch((voters) -> voters.equalsIgnoreCase(user)));
    }

    @Test
    @DisplayName("can close poll")
    public void testCanClosePoll() throws ExecutionException, InterruptedException {
        Poll originalPoll = setupPoll();

        Poll poll = cafeAPI.getPollApi().closePoll(originalPoll.getId()).get();

        Assertions.assertNotNull(poll);
        Assertions.assertFalse(poll.isActive());
        Assertions.assertEquals(1, poll.getResults().length);
    }

    @Test
    @DisplayName("can delete poll")
    public void testCanDeletePoll() throws ExecutionException, InterruptedException {
        Poll originalPoll = setupPoll();

        List<Poll> polls = cafeAPI.getPollApi().getPolls(originalPoll.getGuildId(), true, false).get();
        Assertions.assertEquals(1, polls.size());

        cafeAPI.getPollApi().deletePoll(originalPoll.getId()).join();

        polls = cafeAPI.getPollApi().getPolls(originalPoll.getGuildId()).get();
        Assertions.assertEquals(0, polls.size());
    }

    @Test
    @DisplayName("can get poll by message id")
    public void testCanGetPollById() throws ExecutionException, InterruptedException {
        Poll originalPoll = setupPoll();
        Poll poll = cafeAPI.getPollApi().getPoll(originalPoll.getGuildId(), originalPoll.getMessageId()).get();

        Assertions.assertNotNull(poll);
        Assertions.assertEquals(poll.getId(), originalPoll.getId());
        Assertions.assertEquals(poll.getTitle(), originalPoll.getTitle());
        Assertions.assertEquals(poll.getDescription(), originalPoll.getDescription());
    }

    @Test
    @DisplayName("can manually set poll submission to false")
    public void testCanSetPollSubmissionToFalse() throws ExecutionException, InterruptedException {
        Poll originalPoll = setupPoll();
        int originalVotes = originalPoll.getOptions()[0].getVoters().length;

        String user = generateSnowflake().toString();
        Poll poll = cafeAPI.getPollApi().setVote(originalPoll.getId(), originalPoll.getOptions()[0].getId(), user, true).get();

        Assertions.assertEquals(originalVotes + 1, poll.getOptions()[0].getVoters().length);
    }

    @Test
    @DisplayName("can manually set poll submission to true")
    public void testCanSetPollSubmissionToTrue() throws ExecutionException, InterruptedException {
        Poll originalPoll = setupPoll();
        int originalVotes = originalPoll.getOptions()[0].getVoters().length;

        String user = generateSnowflake().toString();
        Poll poll = cafeAPI.getPollApi().setVote(originalPoll.getId(), originalPoll.getOptions()[0].getId(), user, false).get();

        Assertions.assertEquals(originalVotes, poll.getOptions()[0].getVoters().length);
    }

}
