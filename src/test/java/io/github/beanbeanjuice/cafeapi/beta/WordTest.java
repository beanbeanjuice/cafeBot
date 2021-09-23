package io.github.beanbeanjuice.cafeapi.beta;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A test class used to test all aspects of the {@link io.github.beanbeanjuice.cafeapi.cafebot.words.Words Words} module.
 *
 * @author beanbeanjuice
 */
public class WordTest {

    @Test
    @DisplayName("Test Word API")
    public void wordAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the ENTIRE word list is retrieved.
        Assertions.assertTrue(cafeAPI.words().getAllWords().size() > 370100);

        // Makes sure that the first word in the word list is the correct word.
        Assertions.assertEquals("a", cafeAPI.words().getAllWords().get(0).getWord());

        // Makes sure the uses for the word "a" is greater than 10
        Assertions.assertTrue(cafeAPI.words().getWord("a").getUses() > 10);

        // Makes sure that a NotFoundException is thrown when a word is not found.
        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.words().getWord("FJLSDFJfjsljfsldkf");
        });

        // Makes sure that nothing is thrown when searching for the word "Harmony"
        Assertions.assertDoesNotThrow(() -> {
            cafeAPI.words().getWord("Harmony");
        });

        // Makes sure that a "404" is thrown.
        Assertions.assertTrue(() -> {
            try {
                cafeAPI.words().getWord("FJDLSfjsdlfjsdfJLDKS");
                return false;
            } catch (NotFoundException e) {
                return e.getStatusCode() == 404;
            }
        });

        // Makes sure that word counts are updated.
        Assertions.assertTrue(() -> {
            int currentAmount = cafeAPI.words().getWord("a").getUses();
            cafeAPI.words().updateWord("a", currentAmount + 1);

            return (currentAmount < cafeAPI.words().getWord("a").getUses());
        });

        // Makes sure that a NotFoundException is thrown when trying to update a word that does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.words().updateWord("LSKDFJSDKLFJSDLfjsdkl", 100);
        });
    }
}
