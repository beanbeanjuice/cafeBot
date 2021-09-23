package io.github.beanbeanjuice.cafeapi.beta;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.cafebot.counting.CountingInformation;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CountingInformationTest {

    @Test
    @DisplayName("Counting Information API Test")
    public void testCountingInformationAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the counting information for the guild is deleted beforehand.
        Assertions.assertTrue(cafeAPI.countingInformations().deleteGuildCountingInformation("605489113323536433"));

        // Makes sure the counting information for the guild does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.countingInformations().getGuildCountingInformation("605489113323536433"));

        // Makes sure the counting information for the guild can be created.
        Assertions.assertTrue(cafeAPI.countingInformations().createGuildCountingInformation("605489113323536433"));

        // Makes sure duplicate counting informations cannot be created.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.countingInformations().createGuildCountingInformation("605489113323536433"));

        // Makes sure the counting information's highest number by default is 0.
        Assertions.assertEquals(0, cafeAPI.countingInformations().getGuildCountingInformation("605489113323536433").getHighestNumber());

        // Makes sure the counting information's last number by default is 0.
        Assertions.assertEquals(0, cafeAPI.countingInformations().getAllCountingInformation().get("605489113323536433").getLastNumber());

        // Makes sure the counting information's last user ID by default is "0".
        Assertions.assertEquals("0", cafeAPI.countingInformations().getGuildCountingInformation("605489113323536433").getLastUserID());

        // Makes sure the counting information can be updated.
        Assertions.assertTrue(cafeAPI.countingInformations().updateGuildCountingInformation("605489113323536433", new CountingInformation(
                133, 100, "178272524533104642", "165193414181126145")));

        // Confirms the new highest number is 133.
        Assertions.assertEquals(133, cafeAPI.countingInformations().getGuildCountingInformation("605489113323536433").getHighestNumber());

        // Confirms the new last number is 100.
        Assertions.assertEquals(100, cafeAPI.countingInformations().getGuildCountingInformation("605489113323536433").getLastNumber());

        // Confirms the new last user ID is "178272524533104642".
        Assertions.assertEquals("178272524533104642", cafeAPI.countingInformations().getGuildCountingInformation("605489113323536433").getLastUserID());

        // Confirms the failure role ID is "165193414181126145"
        Assertions.assertEquals("165193414181126145", cafeAPI.countingInformations().getGuildCountingInformation("605489113323536433").getFailureRoleID());

        // Makes sure the counting information can be deleted.
        Assertions.assertTrue(cafeAPI.countingInformations().deleteGuildCountingInformation("605489113323536433"));

        // Makes sure the counting information throws a NotFoundException when trying to retrieve it again.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.countingInformations().getGuildCountingInformation("605489113323536433"));
    }
}
