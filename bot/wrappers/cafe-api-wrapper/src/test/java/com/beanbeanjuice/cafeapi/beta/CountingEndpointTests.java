package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.counting.CountingInformation;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class CountingEndpointTests {

    @Test
    @DisplayName("Counting Endpoint Test")
    public void testCountingEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the counting information for the guild is deleted beforehand.
        Assertions.assertTrue(cafeAPI.getCountingEndpoint().deleteGuildCountingInformation("605489113323536433").get());

        // Makes sure the counting information for the guild does not exist.
        try {
            cafeAPI.getCountingEndpoint().getGuildCountingInformation("605489113323536433").get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(NotFoundException.class, e.getCause());
        }

        // Makes sure the counting information for the guild can be created.
        Assertions.assertTrue(cafeAPI.getCountingEndpoint().createGuildCountingInformation("605489113323536433").get());

        // Makes sure duplicate counting informations cannot be created.
        try {
            cafeAPI.getCountingEndpoint().createGuildCountingInformation("605489113323536433").get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(ConflictException.class, e.getCause());
        }

        // Makes sure the counting information's highest number by default is 0.
        Assertions.assertEquals(0, cafeAPI.getCountingEndpoint().getGuildCountingInformation("605489113323536433").get().getHighestNumber());

        // Makes sure the counting information's last number by default is 0.
        Assertions.assertEquals(0, cafeAPI.getCountingEndpoint().getAllCountingInformation().get().get("605489113323536433").getLastNumber());

        // Makes sure the counting information's last user ID by default is "0".
        Assertions.assertEquals("0", cafeAPI.getCountingEndpoint().getGuildCountingInformation("605489113323536433").get().getLastUserID());

        // Makes sure the counting information can be updated.
        Assertions.assertTrue(cafeAPI.getCountingEndpoint().updateGuildCountingInformation("605489113323536433", new CountingInformation(
                133, 100, "178272524533104642", "165193414181126145")).get());

        // Confirms the new highest number is 133.
        Assertions.assertEquals(133, cafeAPI.getCountingEndpoint().getGuildCountingInformation("605489113323536433").get().getHighestNumber());

        // Confirms the new last number is 100.
        Assertions.assertEquals(100, cafeAPI.getCountingEndpoint().getGuildCountingInformation("605489113323536433").get().getLastNumber());

        // Confirms the new last user ID is "178272524533104642".
        Assertions.assertEquals("178272524533104642", cafeAPI.getCountingEndpoint().getGuildCountingInformation("605489113323536433").get().getLastUserID());

        // Confirms the failure role ID is "165193414181126145"
        Assertions.assertEquals("165193414181126145", cafeAPI.getCountingEndpoint().getGuildCountingInformation("605489113323536433").get().getFailureRoleID());

        // Makes sure the counting information can be deleted.
        Assertions.assertTrue(cafeAPI.getCountingEndpoint().deleteGuildCountingInformation("605489113323536433").get());

        // Makes sure the counting information throws a NotFoundException when trying to retrieve it again.
        try {
            cafeAPI.getCountingEndpoint().getGuildCountingInformation("605489113323536433").get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(NotFoundException.class, e.getCause());
        }
    }
}
