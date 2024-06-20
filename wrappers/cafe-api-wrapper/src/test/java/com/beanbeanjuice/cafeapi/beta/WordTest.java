package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.cafebot.words.Words;
import com.beanbeanjuice.cafeapi.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A test class used to test all aspects of the {@link Words Words} module.
 *
 * @author beanbeanjuice
 */
public class WordTest {

    @Test
    @DisplayName("Test Word API")
    public void wordAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the ENTIRE word list is retrieved.
        Assertions.assertTrue(cafeAPI.WORD.getAllWords().size() > 370100);

        // Makes sure that the first word in the word list is the correct word.
        Assertions.assertEquals("a", cafeAPI.WORD.getAllWords().get(0).getWord());

        // Makes sure the uses for the word "a" is greater than 10
        Assertions.assertTrue(cafeAPI.WORD.getWord("a").getUses() > 10);

        // Makes sure that a NotFoundException is thrown when a word is not found.
        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.WORD.getWord("FJLSDFJfjsljfsldkf");
        });

        // Makes sure that nothing is thrown when searching for the word "Harmony"
        Assertions.assertDoesNotThrow(() -> {
            cafeAPI.WORD.getWord("Harmony");
        });

        // Makes sure that a "404" is thrown.
        Assertions.assertTrue(() -> {
            try {
                cafeAPI.WORD.getWord("FJDLSfjsdlfjsdfJLDKS");
                return false;
            } catch (NotFoundException e) {
                return e.getStatusCode() == 404;
            }
        });

        // Makes sure that word counts are updated.
        Assertions.assertTrue(() -> {
            int currentAmount = cafeAPI.WORD.getWord("a").getUses();
            cafeAPI.WORD.updateWord("a", currentAmount + 1);

            return (currentAmount < cafeAPI.WORD.getWord("a").getUses());
        });

        // Makes sure that a NotFoundException is thrown when trying to update a word that does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.WORD.updateWord("LSKDFJSDKLFJSDLfjsdkl", 100);
        });
    }
}
