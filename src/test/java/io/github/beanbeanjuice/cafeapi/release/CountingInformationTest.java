package io.github.beanbeanjuice.cafeapi.release;

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
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("RELEASE_API_PASSWORD"), RequestLocation.RELEASE);

        // Makes sure the counting information for the guild is deleted beforehand.
        Assertions.assertTrue(cafeAPI.countingInformations().deleteGuildCountingInformation("817975989547040795"));

        // Makes sure the counting information for the guild does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.countingInformations().getGuildCountingInformation("817975989547040795"));

        // Makes sure the counting information for the guild can be created.
        Assertions.assertTrue(cafeAPI.countingInformations().createGuildCountingInformation("817975989547040795"));

        // Makes sure duplicate counting informations cannot be created.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.countingInformations().createGuildCountingInformation("817975989547040795"));

        // Makes sure the counting information's highest number by default is 0.
        Assertions.assertEquals(0, cafeAPI.countingInformations().getGuildCountingInformation("817975989547040795").getHighestNumber());

        // Makes sure the counting information's last number by default is 0.
        Assertions.assertEquals(0, cafeAPI.countingInformations().getAllCountingInformation().get("817975989547040795").getLastNumber());

        // Makes sure the counting information's last user ID by default is "0".
        Assertions.assertEquals("0", cafeAPI.countingInformations().getGuildCountingInformation("817975989547040795").getLastUserID());

        // Makes sure the counting information can be updated.
        Assertions.assertTrue(cafeAPI.countingInformations().updateGuildCountingInformation("817975989547040795", new CountingInformation(
                133, 100, "817975989547040795", "817975989547040795")));

        // Confirms the new highest number is 133.
        Assertions.assertEquals(133, cafeAPI.countingInformations().getGuildCountingInformation("817975989547040795").getHighestNumber());

        // Confirms the new last number is 100.
        Assertions.assertEquals(100, cafeAPI.countingInformations().getGuildCountingInformation("817975989547040795").getLastNumber());

        // Confirms the new last user ID is "178272524533104642".
        Assertions.assertEquals("817975989547040795", cafeAPI.countingInformations().getGuildCountingInformation("817975989547040795").getLastUserID());

        // Confirms the failure role ID is "165193414181126145"
        Assertions.assertEquals("817975989547040795", cafeAPI.countingInformations().getGuildCountingInformation("817975989547040795").getFailureRoleID());

        // Makes sure the counting information can be deleted.
        Assertions.assertTrue(cafeAPI.countingInformations().deleteGuildCountingInformation("817975989547040795"));

        // Makes sure the counting information throws a NotFoundException when trying to retrieve it again.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.countingInformations().getGuildCountingInformation("817975989547040795"));
    }
}
