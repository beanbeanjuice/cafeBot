package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.raffles.Raffle;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RafflesEndpointTests {

    @Test
    @DisplayName("Raffles Endpoint Test")
    public void testRafflesEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        long currentTime = System.currentTimeMillis();
        Timestamp currentTimestamp = CafeGeneric.parseTimestamp(new Timestamp(currentTime).toString()).orElse(null);

        // Makes sure the raffle is deleted beforehand.
        Assertions.assertTrue(cafeAPI.getRafflesEndpoint().deleteRaffle("798830792938881024", "878895791081676831").get());

        // Makes sure a raffle can be created.
        Assertions.assertTrue(cafeAPI.getRafflesEndpoint().createRaffle("798830792938881024", new Raffle(
                "878895791081676831",
                currentTimestamp,
                3
        )).get());

        // Makes sure creating the same raffle throws an exception.
        cafeAPI.getRafflesEndpoint().createRaffle("798830792938881024", new Raffle(
                        "878895791081676831",
                        currentTimestamp,
                        3
                )).thenAcceptAsync((isSuccess) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(ConflictException.class, exception.getCause());
                    return null;
                }).join();

        // Makes sure that the newly created raffle shows up in the API.
        Assertions.assertTrue(() -> {
            ArrayList<Raffle> raffles = null;
            try {
                raffles = cafeAPI.getRafflesEndpoint().getAllRaffles().get().get("798830792938881024");
            } catch (Exception e) {
                Assertions.fail(e);
            }

            for (Raffle raffle : raffles) {
                if (raffle.getMessageID().equals("878895791081676831")) {
                    return true;
                }
            }

            return false;
        });

        // Makes sure that retrieving the timestamp of the poll works.
        Assertions.assertTrue(() -> {
            ArrayList<Raffle> raffles = null;
            try {
                raffles = cafeAPI.getRafflesEndpoint().getGuildRaffles("798830792938881024").get();
            } catch (Exception e) {
                Assertions.fail(e);
            }

            for (Raffle raffle : raffles) {
                if (raffle.getEndingTime().equals(currentTimestamp)) {
                    return true;
                }
            }

            return false;
        });

        // Makes sure that getting the raffle directly also works.
        Assertions.assertTrue(() -> {
            ArrayList<Raffle> raffles = null;
            try {
                raffles = cafeAPI.getRafflesEndpoint().getGuildRaffles("798830792938881024").get();
            } catch (Exception e) {
                Assertions.fail(e);
            }

            for (Raffle raffle : raffles) {
                if (raffle.getMessageID().equals("878895791081676831")) {
                    return true;
                }
            }

            return false;
        });

        // Makes sure deleting the raffle is able to happen.
        Assertions.assertTrue(cafeAPI.getRafflesEndpoint().deleteRaffle("798830792938881024", "878895791081676831").get());

        // Makes sure the newly created raffle that was deleted no longer shows up in the API.
        Assertions.assertFalse(() -> {
            ArrayList<Raffle> raffles = null;
            try {
                raffles = cafeAPI.getRafflesEndpoint().getAllRaffles().get().get("798830792938881024");
            } catch (Exception e) {
                Assertions.fail(e);
            }

            if (raffles == null) {
                return false;
            }

            for (Raffle raffle : raffles) {
                if (raffle.getMessageID().equals("878895791081676831")) {
                    return true;
                }
            }

            return false;
        });
    }

}
