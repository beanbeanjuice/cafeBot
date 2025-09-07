package com.beanbeanjuice.cafebot.endpoints.discord.server;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.AirportMessageType;
import com.beanbeanjuice.cafebot.api.wrapper.type.airport.AirportMessage;
import com.beanbeanjuice.cafebot.api.wrapper.type.airport.PartialAirportMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class AirportApiTest extends ApiTest {

    private String guildId;
    private PartialAirportMessage welcomeMessage;
    private PartialAirportMessage goodbyeMessage;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException {
        guildId = generateSnowflake().toString();

        welcomeMessage = new PartialAirportMessage(
                AirportMessageType.WELCOME,
                "Example Title",
                "Example Author",
                "https://www.example.com",
                "https://www.exampleImage.com",
                "https://www.exampleThumbnail.com",
                "Example Description",
                "Example Message"
        );

        goodbyeMessage = new PartialAirportMessage(
                AirportMessageType.GOODBYE,
                "Another Title",
                "Another Author",
                "https://www.another.com",
                "https://www.anotherImage.com",
                "https://www.anotherThumbnail.com",
                "Another Description",
                "Another Message"
        );

        cafeAPI.getAirportApi().setAirportMessage(guildId, welcomeMessage).get();
        cafeAPI.getAirportApi().setAirportMessage(guildId, goodbyeMessage).get();
    }

    @Test
    @DisplayName("can get all airport messages for a guild")
    public void canGetAllAirportMessagesForGuild() throws ExecutionException, InterruptedException {
        AirportMessage message = cafeAPI.getAirportApi().getAirportMessage(guildId, AirportMessageType.WELCOME).get();

        Assertions.assertNotNull(message);
        Assertions.assertEquals(guildId, message.getGuildId());
        Assertions.assertEquals(welcomeMessage.getTitle(), message.getTitle());
        Assertions.assertEquals(welcomeMessage.getAuthor(), message.getAuthor());
        Assertions.assertEquals(welcomeMessage.getAuthorUrl(), message.getAuthorUrl());
        Assertions.assertEquals(welcomeMessage.getDescription(), message.getDescription());
        Assertions.assertEquals(welcomeMessage.getMessage(), message.getMessage());
        Assertions.assertEquals(welcomeMessage.getThumbnailUrl(), message.getThumbnailUrl());
        Assertions.assertEquals(welcomeMessage.getImageUrl(), message.getImageUrl());
    }

    @Test
    @DisplayName("can delete airport message")
    public void canUpdateAirportMessage() throws ExecutionException, InterruptedException {
        cafeAPI.getAirportApi().deleteAirportMessage(guildId, AirportMessageType.GOODBYE).get();

        Assertions.assertThrows(CompletionException.class, () ->
                cafeAPI.getAirportApi()
                        .getAirportMessage(guildId, AirportMessageType.GOODBYE)
                        .join()
        );
    }

}
