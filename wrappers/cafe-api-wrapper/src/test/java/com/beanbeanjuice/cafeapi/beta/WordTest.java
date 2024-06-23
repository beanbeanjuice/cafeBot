package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.words.WordsEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A test class used to test all aspects of the {@link WordsEndpoint Words} module.
 *
 * @author beanbeanjuice
 */
public class WordTest {

    @Test
    @DisplayName("Words Endpoint Test")
    public void testWordsEndpoint() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the ENTIRE word list is retrieved.
        Assertions.assertTrue(cafeAPI.getWordsEndpoint().getAllWords().size() > 370100);

        // Makes sure that the first word in the word list is the correct word.
        Assertions.assertEquals("a", cafeAPI.getWordsEndpoint().getAllWords().get(0).getWord());

        // Makes sure the uses for the word "a" is greater than 10
        Assertions.assertTrue(cafeAPI.getWordsEndpoint().getWord("a").getUses() > 10);

        // Makes sure that a NotFoundException is thrown when a word is not found.
        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.getWordsEndpoint().getWord("FJLSDFJfjsljfsldkf");
        });

        // Makes sure that nothing is thrown when searching for the word "Harmony"
        Assertions.assertDoesNotThrow(() -> {
            cafeAPI.getWordsEndpoint().getWord("Harmony");
        });

        // Makes sure that a "404" is thrown.
        Assertions.assertTrue(() -> {
            try {
                cafeAPI.getWordsEndpoint().getWord("FJDLSfjsdlfjsdfJLDKS");
                return false;
            } catch (NotFoundException e) {
                return e.getStatusCode() == 404;
            }
        });

        // Makes sure that word counts are updated.
        Assertions.assertTrue(() -> {
            int currentAmount = cafeAPI.getWordsEndpoint().getWord("a").getUses();
            cafeAPI.getWordsEndpoint().updateWord("a", currentAmount + 1);

            return (currentAmount < cafeAPI.getWordsEndpoint().getWord("a").getUses());
        });

        // Makes sure that a NotFoundException is thrown when trying to update a word that does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.getWordsEndpoint().updateWord("LSKDFJSDKLFJSDLfjsdkl", 100);
        });
    }
}
