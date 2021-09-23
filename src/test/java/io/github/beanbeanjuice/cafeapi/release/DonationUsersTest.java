package io.github.beanbeanjuice.cafeapi.release;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

public class DonationUsersTest {

    @Test
    @DisplayName("Donation Users API Test")
    public void testDonationUsersAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("RELEASE_API_PASSWORD"), RequestLocation.RELEASE);

        Assertions.assertTrue(cafeAPI.donationUsers().deleteDonationUser("817975989547040795"));
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.donationUsers().getUserDonationTime("817975989547040795"));
        Timestamp currentTimestamp = CafeGeneric.parseTimestamp((new Timestamp(System.currentTimeMillis())).toString());
        Assertions.assertTrue(cafeAPI.donationUsers().addDonationUser("817975989547040795", currentTimestamp));
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.donationUsers().addDonationUser("817975989547040795", currentTimestamp));
        Assertions.assertEquals(currentTimestamp, cafeAPI.donationUsers().getAllUserDonationTimes().get("817975989547040795"));
        Assertions.assertEquals(currentTimestamp, cafeAPI.donationUsers().getUserDonationTime("817975989547040795"));

        Assertions.assertTrue(cafeAPI.donationUsers().deleteDonationUser("817975989547040795"));
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.donationUsers().getUserDonationTime("817975989547040795"));
    }

}
