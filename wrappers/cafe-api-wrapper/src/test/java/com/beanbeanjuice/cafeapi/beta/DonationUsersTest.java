package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

public class DonationUsersTest {

    @Test
    @DisplayName("Donation Users API Test")
    public void testDonationUsersAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        Assertions.assertTrue(cafeAPI.DONATION_USER.deleteDonationUser("738590591767543921"));
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.DONATION_USER.getUserDonationTime("738590591767543921"));
        Timestamp currentTimestamp = CafeGeneric.parseTimestamp((new Timestamp(System.currentTimeMillis())).toString()).orElse(null);
        Assertions.assertTrue(cafeAPI.DONATION_USER.addDonationUser("738590591767543921", currentTimestamp));
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.DONATION_USER.addDonationUser("738590591767543921", currentTimestamp));
        Assertions.assertEquals(currentTimestamp, cafeAPI.DONATION_USER.getAllUserDonationTimes().get("738590591767543921"));
        Assertions.assertEquals(currentTimestamp, cafeAPI.DONATION_USER.getUserDonationTime("738590591767543921").orElse(null));

        Assertions.assertTrue(cafeAPI.DONATION_USER.deleteDonationUser("738590591767543921"));
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.DONATION_USER.getUserDonationTime("738590591767543921"));
    }

}
