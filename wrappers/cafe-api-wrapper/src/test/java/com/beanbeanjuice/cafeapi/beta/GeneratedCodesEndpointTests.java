package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class GeneratedCodesEndpointTests {

    @Test
    @DisplayName("Generated Codes Endpoint Test")
    public void testGeneratedCodesEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the code doesn't exist beforehand.
        Assertions.assertTrue(cafeAPI.getGeneratedCodesEndpoint().deleteUserGeneratedCode("738590591767543921").get());

        // Makes sure a NotFoundException is thrown when trying to search for a user that doesn't exist.
        try {
            cafeAPI.getGeneratedCodesEndpoint().getUserGeneratedCode("738590591767543921").get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(NotFoundException.class, e.getCause());
        }

        // Makes sure a generated code can be created.
        Assertions.assertTrue(cafeAPI.getGeneratedCodesEndpoint().createUserGeneratedCode("738590591767543921", "testCode").get());

        // Makes sure a duplicate code can't be created.
        try {
            cafeAPI.getGeneratedCodesEndpoint().createUserGeneratedCode("738590591767543921", "testCod#").get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(ConflictException.class, e.getCause());
        }

        // Makes sure the code retrieved is the one entered.
        Assertions.assertEquals("testCode", cafeAPI.getGeneratedCodesEndpoint().getAllGeneratedCodes().get().get("738590591767543921"));

        // Makes sure the code retrieved from the hashmap is the one entered.
        Assertions.assertEquals("testCode", cafeAPI.getGeneratedCodesEndpoint().getUserGeneratedCode("738590591767543921").get());

        // Makes sure the generated code can be updated.
        Assertions.assertTrue(cafeAPI.getGeneratedCodesEndpoint().updateUserGeneratedCode("738590591767543921", "bruh_moment").get());

        // Makes sure the retrieved generated code is the same as the one entered.
        Assertions.assertEquals("bruh_moment", cafeAPI.getGeneratedCodesEndpoint().getUserGeneratedCode("738590591767543921").get());

        // Makes sure the code can be deleted.
        Assertions.assertTrue(cafeAPI.getGeneratedCodesEndpoint().deleteUserGeneratedCode("738590591767543921").get());

        // Makes sure the code no longer exists.
        try {
            cafeAPI.getGeneratedCodesEndpoint().getUserGeneratedCode("738590591767543921").get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(NotFoundException.class, e.getCause());
        }
    }

}
