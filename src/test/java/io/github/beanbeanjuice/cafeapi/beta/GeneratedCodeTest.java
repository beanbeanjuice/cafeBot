package io.github.beanbeanjuice.cafeapi.beta;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GeneratedCodeTest {

    @Test
    @DisplayName("Test Generated Codes API")
    public void testGeneratedCodesAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the code doesn't exist beforehand.
        Assertions.assertTrue(cafeAPI.generatedCodes().deleteUserGeneratedCode("738590591767543921"));

        // Makes sure a NotFoundException is thrown when trying to search for a user that doesn't exist.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.generatedCodes().getUserGeneratedCode("738590591767543921"));

        // Makes sure a generated code can be created.
        Assertions.assertTrue(cafeAPI.generatedCodes().createUserGeneratedCode("738590591767543921", "testCode"));

        // Makes sure a duplicate code can't be created.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.generatedCodes().createUserGeneratedCode("738590591767543921", "testCod#"));

        // Makes sure the code retrieved is the one entered.
        Assertions.assertEquals("testCode", cafeAPI.generatedCodes().getAllGeneratedCodes().get("738590591767543921"));

        // Makes sure the code retrieved from the hashmap is the one entered.
        Assertions.assertEquals("testCode", cafeAPI.generatedCodes().getUserGeneratedCode("738590591767543921"));

        // Makes sure the generated code can be updated.
        Assertions.assertTrue(cafeAPI.generatedCodes().updateUserGeneratedCode("738590591767543921", "bruh_moment"));

        // Makes sure the retrieved generated code is the same as the one entered.
        Assertions.assertEquals("bruh_moment", cafeAPI.generatedCodes().getUserGeneratedCode("738590591767543921"));

        // Makes sure the code can be deleted.
        Assertions.assertTrue(cafeAPI.generatedCodes().deleteUserGeneratedCode("738590591767543921"));

        // Makes sure the code no longer exists.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.generatedCodes().getUserGeneratedCode("738590591767543921"));
    }

}
