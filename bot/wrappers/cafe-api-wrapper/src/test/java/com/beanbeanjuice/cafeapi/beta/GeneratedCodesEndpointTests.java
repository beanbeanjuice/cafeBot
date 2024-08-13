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
        String code = cafeAPI.getGeneratedCodesEndpoint().createUserGeneratedCode("738590591767543921").get();
        Assertions.assertNotNull(code);

        // Makes sure a duplicate code can't be created.
        try {
            cafeAPI.getGeneratedCodesEndpoint().createUserGeneratedCode("738590591767543921").get();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(ConflictException.class, e.getCause());
        }

        // Makes sure the code retrieved is the one entered.
        Assertions.assertEquals(code, cafeAPI.getGeneratedCodesEndpoint().getAllGeneratedCodes().get().get("738590591767543921"));

        // Makes sure the code retrieved from the hashmap is the one entered.
        Assertions.assertEquals(code, cafeAPI.getGeneratedCodesEndpoint().getUserGeneratedCode("738590591767543921").get());

        // Makes sure the generated code can be updated.
        String updatedCode = cafeAPI.getGeneratedCodesEndpoint().updateUserGeneratedCode("738590591767543921").get();
        Assertions.assertNotNull(updatedCode);

        // Makes sure the retrieved generated code is the same as the one entered.
        Assertions.assertEquals(updatedCode, cafeAPI.getGeneratedCodesEndpoint().getUserGeneratedCode("738590591767543921").get());

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

    @Test
    @DisplayName("Generated Codes Endpoint Test - Testing Update")
    public void testUpdate() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the code doesn't exist beforehand.
        Assertions.assertTrue(cafeAPI.getGeneratedCodesEndpoint().deleteUserGeneratedCode("738590591767543921").get());

        // Making sure it can't be updated if it does not exist.
        cafeAPI.getGeneratedCodesEndpoint().updateUserGeneratedCode("738590591767543921")
                .thenAcceptAsync((code) -> Assertions.fail())
                .exceptionallyAsync((e) -> {
                    Assertions.assertInstanceOf(NotFoundException.class, e.getCause());
                    return null;
                }).join();

        cafeAPI.getGeneratedCodesEndpoint().updateUserGeneratedCodeIfExists("738590591767543921")
                .thenAcceptAsync(Assertions::assertNotNull)
                .exceptionallyAsync(Assertions::fail)
                .join();

        Assertions.assertTrue(cafeAPI.getGeneratedCodesEndpoint().deleteUserGeneratedCode("738590591767543921").get());
    }

}
