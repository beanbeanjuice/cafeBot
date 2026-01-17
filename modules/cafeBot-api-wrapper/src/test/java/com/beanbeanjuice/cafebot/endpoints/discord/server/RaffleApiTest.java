package com.beanbeanjuice.cafebot.endpoints.discord.server;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.type.Raffle;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RaffleApiTest extends ApiTest {

    private String guildId;
    private String messageId;
    private Instant instant;
    private int raffleId;
    private Raffle raffle;

    private String user1;
    private String user2;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException {
        this.guildId = generateSnowflake().toString();
        this.messageId = generateSnowflake().toString();
        this.user1 = generateSnowflake().toString();
        this.user2 = generateSnowflake().toString();

        this.instant = Instant.now().plus(2, ChronoUnit.SECONDS).truncatedTo(ChronoUnit.MILLIS);

        this.raffle = cafeAPI.getRaffleApi().createRaffle(new Raffle(
                0,
                guildId,
                messageId,
                "Some Title",
                "Some Description",
                10,
                true,
                instant.toString(),
                new String[0],
                new String[0]
        )).get();

        this.raffleId = raffle.getId();

        this.raffle = cafeAPI.getRaffleApi().toggleSubmission(raffle.getId(), user1).get();
    }

    @Test
    @DisplayName("can get all raffles")
    public void getAllRafflesTest() throws ExecutionException, InterruptedException {
        Thread.sleep(Duration.of(3, ChronoUnit.SECONDS).toMillis());

        Map<String, List<Raffle>> raffles = cafeAPI.getRaffleApi().getRaffles().get();  // Active + Expired raffles.
        Assertions.assertTrue(!raffles.isEmpty());
    }

    @Test
    @DisplayName("can get all raffles for a guild")
    public void getAllRafflesForGuildTest() throws ExecutionException, InterruptedException {
        Thread.sleep(Duration.of(3, ChronoUnit.SECONDS).toMillis());

        List<Raffle> raffles = cafeAPI.getRaffleApi().getRaffles(guildId).get();  // Active + Expired raffles.
        Assertions.assertEquals(1, raffles.size());
    }

    @Test
    @DisplayName("can create raffle")
    public void createRaffleTest() throws ExecutionException, InterruptedException {
        String guildId = generateSnowflake().toString();
        String messageId = generateSnowflake().toString();

        Instant instant = Instant.now().plus(1, ChronoUnit.HOURS);

        Raffle raffle = cafeAPI.getRaffleApi().createRaffle(new Raffle(
                0,
                guildId,
                messageId,
                "Some Title",
                "Some Description",
                10,
                true,
                instant.toString(),
                new String[0],
                new String[0]
        )).get();

        Assertions.assertEquals(guildId, raffle.getGuildId());
        Assertions.assertEquals(messageId, raffle.getMessageId());
        Assertions.assertEquals("Some Title", raffle.getTitle());
        Assertions.assertEquals("Some Description", raffle.getDescription().get());
        Assertions.assertEquals(10, raffle.getNumWinners());
        Assertions.assertEquals(true, raffle.isActive());
        Assertions.assertEquals(
                instant.truncatedTo(ChronoUnit.MILLIS),
                raffle.getEndsAt().truncatedTo(ChronoUnit.MILLIS)
        );
        Assertions.assertEquals(0, raffle.getSubmissions().length);
        Assertions.assertEquals(0, raffle.getWinners().length);
    }

    @Test
    @DisplayName("can add submission to raffle")
    public void addSubmissionToRaffleTest() throws ExecutionException, InterruptedException {
        Raffle raffle = cafeAPI.getRaffleApi().toggleSubmission(this.raffleId, user2).get();

        Assertions.assertNotNull(raffle);
        Assertions.assertNotNull(raffle.getSubmissions());
        Assertions.assertTrue(Arrays.stream(raffle.getSubmissions()).anyMatch((userId) -> userId.equals(user2)));
    }

    @Test
    @DisplayName("can close raffle")
    public void closeRaffleTest() throws ExecutionException, InterruptedException {
        Thread.sleep(Duration.of(3, ChronoUnit.SECONDS).toMillis());

        Raffle raffle = cafeAPI.getRaffleApi().closeRaffle(this.raffleId).get();

        Assertions.assertNotNull(raffle);
        Assertions.assertEquals(1, raffle.getSubmissions().length);
        Assertions.assertEquals(1, raffle.getWinners().length);
        Assertions.assertTrue(Arrays.stream(raffle.getWinners()).anyMatch((userId) -> userId.equals(user1)));
        Assertions.assertFalse(raffle.isActive());
    }

    @Test
    @DisplayName("can delete raffle")
    public void deleteRaffleTest() throws ExecutionException, InterruptedException {
        cafeAPI.getRaffleApi().deleteRaffle(this.raffleId).join();

        List<Raffle> raffles = cafeAPI.getRaffleApi().getRaffles(this.guildId).get();

        Assertions.assertEquals(0, raffles.size());
    }

    @Test
    @DisplayName("can get raffle by message id")
    public void testCanGetRaffleById() throws ExecutionException, InterruptedException {
        Raffle raffle = cafeAPI.getRaffleApi().getRaffle(this.guildId, this.messageId).get();

        Assertions.assertNotNull(raffle);
        Assertions.assertEquals(raffle.getId(), this.raffle.getId());
        Assertions.assertEquals(raffle.getTitle(), this.raffle.getTitle());
        Assertions.assertEquals(raffle.getDescription(), this.raffle.getDescription());
    }

    @Test
    @DisplayName("can manually set raffle submission to false")
    public void testCanSetRaffleSubmissionToFalse() throws ExecutionException, InterruptedException {
        int originalSubmissions = this.raffle.getSubmissions().length;

        String user = generateSnowflake().toString();
        Raffle raffle = cafeAPI.getRaffleApi().setSubmission(this.raffle.getId(), user, true).get();

        Assertions.assertEquals(originalSubmissions + 1, raffle.getSubmissions().length);
    }

    @Test
    @DisplayName("can manually set raffle submission to true")
    public void testCanSetRaffleSubmissionToTrue() throws ExecutionException, InterruptedException {
        int originalSubmissions = this.raffle.getSubmissions().length;

        String user = generateSnowflake().toString();
        Raffle raffle = cafeAPI.getRaffleApi().setSubmission(this.raffle.getId(), user, false).get();

        Assertions.assertEquals(originalSubmissions, raffle.getSubmissions().length);
    }

}
