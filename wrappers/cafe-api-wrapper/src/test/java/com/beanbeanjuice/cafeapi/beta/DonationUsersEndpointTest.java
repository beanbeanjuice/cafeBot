package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.generic.CafeGeneric;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

public class DonationUsersEndpointTest {

    @Test
    @DisplayName("Donation Users Endpoint Test")
    public void testDonationUsersEndpoint() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        Assertions.assertTrue(cafeAPI.getDonationUsersEndpoint().deleteDonationUser("738590591767543921"));
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.getDonationUsersEndpoint().getUserDonationTime("738590591767543921"));
        Timestamp currentTimestamp = CafeGeneric.parseTimestamp((new Timestamp(System.currentTimeMillis())).toString()).orElse(null);
        Assertions.assertTrue(cafeAPI.getDonationUsersEndpoint().addDonationUser("738590591767543921", currentTimestamp));
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.getDonationUsersEndpoint().addDonationUser("738590591767543921", currentTimestamp));
        Assertions.assertEquals(currentTimestamp, cafeAPI.getDonationUsersEndpoint().getAllUserDonationTimes().get("738590591767543921"));
        Assertions.assertEquals(currentTimestamp, cafeAPI.getDonationUsersEndpoint().getUserDonationTime("738590591767543921").orElse(null));

        Assertions.assertTrue(cafeAPI.getDonationUsersEndpoint().deleteDonationUser("738590591767543921"));
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.getDonationUsersEndpoint().getUserDonationTime("738590591767543921"));
    }

}
