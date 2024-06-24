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
import java.util.concurrent.ExecutionException;

public class DonationUsersEndpointTests {

    @Test
    @DisplayName("Donation Users Endpoint Test")
    public void testDonationUsersEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        Assertions.assertTrue(cafeAPI.getDonationUsersEndpoint().deleteDonationUser("738590591767543921").get());

        try {
            cafeAPI.getDonationUsersEndpoint().getUserDonationTime("738590591767543921").get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(NotFoundException.class, e.getCause());
        }

        Timestamp currentTimestamp = CafeGeneric.parseTimestamp((new Timestamp(System.currentTimeMillis())).toString()).orElse(null);
        Assertions.assertTrue(cafeAPI.getDonationUsersEndpoint().addDonationUser("738590591767543921", currentTimestamp).get());

        try {
            cafeAPI.getDonationUsersEndpoint().addDonationUser("738590591767543921", currentTimestamp).get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(ConflictException.class, e.getCause());
        }
        Assertions.assertEquals(currentTimestamp, cafeAPI.getDonationUsersEndpoint().getAllUserDonationTimes().get().get("738590591767543921"));
        Assertions.assertEquals(currentTimestamp, cafeAPI.getDonationUsersEndpoint().getUserDonationTime("738590591767543921").get().orElse(null));

        Assertions.assertTrue(cafeAPI.getDonationUsersEndpoint().deleteDonationUser("738590591767543921").get());

        try {
            cafeAPI.getDonationUsersEndpoint().getUserDonationTime("738590591767543921").get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(NotFoundException.class, e.getCause());
        }
    }

}
