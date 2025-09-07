package com.beanbeanjuice.cafebot.endpoints.discord.user;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.InteractionType;
import com.beanbeanjuice.cafebot.api.wrapper.type.Interaction;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class InteractionsApiTest extends ApiTest {

    private String ID_1;
    private int ID_1_SENT;
    private String ID_2;
    private int ID_2_SENT;

    @BeforeEach
    public void generateFakeInteractions() throws ExecutionException, InterruptedException {
        ID_1 = generateSnowflake().toString();
        ID_1_SENT = 0;
        ID_2 = generateSnowflake().toString();
        ID_2_SENT = 0;

        boolean swap = false;
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (int i = 0; i < InteractionType.values().length; i++) {
            if (i >= InteractionType.values().length / 2) swap = true;

            String from = (!swap) ? ID_1 : ID_2;
            String to = (!swap) ? ID_2 : ID_1;

            CompletableFuture<?> future =
                    cafeAPI.getInteractionsApi().createInteraction(from, to, InteractionType.values()[i]);

            futures.add(future);

            if (!swap) ID_1_SENT++;
            else ID_2_SENT++;
        }

        // Wait for all async calls to finish
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    }

    @Test
    @DisplayName("can get all interaction images")
    public void canGetAllInteractionImages() throws ExecutionException, InterruptedException {
        CompletableFuture<?>[] futures = Arrays.stream(InteractionType.values())
                .map(type ->
                        cafeAPI.getInteractionsApi()
                                .getImage(type)
                                .thenAccept(Assertions::assertNotNull)
                )
                .toArray(CompletableFuture[]::new);

        // Wait for all to complete
        CompletableFuture.allOf(futures).get();
    }

    @Test
    @DisplayName("can get all interactions")
    public void canGetAllInteractions() throws ExecutionException, InterruptedException {
        ArrayList<Interaction> ID_1_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractions(ID_1).get();
        ArrayList<Interaction> ID_2_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractions(ID_2).get();

        Assertions.assertEquals(ID_1_INTERACTIONS.size(), ID_2_INTERACTIONS.size());
        Assertions.assertEquals(ID_1_SENT + ID_2_SENT, ID_1_INTERACTIONS.size());
        Assertions.assertEquals(ID_1_SENT + ID_2_SENT, ID_2_INTERACTIONS.size());
    }

    @Test
    @DisplayName("can get all interactions of type")
    public void canGetAllInteractionsOfType() throws ExecutionException, InterruptedException {
        ArrayList<Interaction> ID_1_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractions(ID_1, InteractionType.HUG).get();
        ArrayList<Interaction> ID_2_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractions(ID_2, InteractionType.HUG).get();

        Assertions.assertEquals(1, ID_1_INTERACTIONS.size());
        Assertions.assertEquals(1, ID_2_INTERACTIONS.size());
    }

    @Test
    @DisplayName("can get all sent interactions")
    public void canGetAllSentInteractions() throws ExecutionException, InterruptedException {
        ArrayList<Interaction> ID_1_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractionsSent(ID_1).get();
        ArrayList<Interaction> ID_2_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractionsSent(ID_2).get();

        Assertions.assertEquals(ID_1_SENT, ID_1_INTERACTIONS.size());
        Assertions.assertEquals(ID_2_SENT, ID_2_INTERACTIONS.size());
    }

    @Test
    @DisplayName("can get all interactions sent of type")
    public void canGetAllInteractionsSentOfType() throws ExecutionException, InterruptedException {
        ArrayList<Interaction> ID_1_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractionsSent(ID_1, InteractionType.values()[0]).get();
        ArrayList<Interaction> ID_2_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractionsSent(ID_2, InteractionType.values()[InteractionType.values().length - 1]).get();

        Assertions.assertEquals(1, ID_1_INTERACTIONS.size());
        Assertions.assertEquals(1, ID_2_INTERACTIONS.size());
    }

    @Test
    @DisplayName("can get all received interactions")
    public void canGetAllReceivedInteractions() throws ExecutionException, InterruptedException {
        ArrayList<Interaction> ID_1_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractionsReceived(ID_1).get();
        ArrayList<Interaction> ID_2_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractionsReceived(ID_2).get();

        Assertions.assertEquals(ID_2_SENT, ID_1_INTERACTIONS.size());
        Assertions.assertEquals(ID_1_SENT, ID_2_INTERACTIONS.size());
    }

    @Test
    @DisplayName("can get all interactions received of type")
    public void canGetAllInteractionsReceivedOfType() throws ExecutionException, InterruptedException {
        ArrayList<Interaction> ID_1_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractionsReceived(ID_1, InteractionType.values()[InteractionType.values().length - 1]).get();
        ArrayList<Interaction> ID_2_INTERACTIONS = cafeAPI.getInteractionsApi().getInteractionsReceived(ID_2, InteractionType.values()[0]).get();

        Assertions.assertEquals(1, ID_1_INTERACTIONS.size());
        Assertions.assertEquals(1, ID_2_INTERACTIONS.size());
    }

    @Test
    @DisplayName("can create an interaction of all specified types")
    public void createInteractionsOfAllTypes() throws ExecutionException, InterruptedException {
        String user1 = generateSnowflake().toString();
        String user2 = generateSnowflake().toString();

        for (InteractionType type : InteractionType.values()) {
            Interaction interaction = cafeAPI.getInteractionsApi().createInteraction(user1, user2, type).get();

            Assertions.assertNotNull(interaction);
            Assertions.assertEquals(1, interaction.getNumSentFrom());
            Assertions.assertEquals(1, interaction.getNumSentTo());
        }
    }

}
