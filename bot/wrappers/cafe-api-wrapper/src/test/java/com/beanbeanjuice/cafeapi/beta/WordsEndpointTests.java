package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.words.WordsEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

/**
 * A test class used to test all aspects of the {@link WordsEndpoint Words} module.
 *
 * @author beanbeanjuice
 */
public class WordsEndpointTests {

    @Test
    @DisplayName("Words Endpoint Test")
    public void testWordsEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the ENTIRE word list is retrieved.
        Assertions.assertTrue(cafeAPI.getWordsEndpoint().getAllWords().get().size() > 370100);

        // Makes sure that the first word in the word list is the correct word.
        Assertions.assertEquals("a", cafeAPI.getWordsEndpoint().getAllWords().get().getFirst().getWord());

        // Makes sure the uses for the word "a" is greater than 10
        Assertions.assertTrue(cafeAPI.getWordsEndpoint().getWord("a").get().getUses() > 10);

        // Makes sure that a NotFoundException is thrown when a word is not found.
        cafeAPI.getWordsEndpoint().getWord("FJLSDFJfjsljfsldkf")
                .thenAcceptAsync((word) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(NotFoundException.class, exception.getCause());
                    return null;
                }).join();

        // Makes sure that nothing is thrown when searching for the word "Harmony"
        cafeAPI.getWordsEndpoint().getWord("Harmony")
                .thenAcceptAsync(Assertions::assertNotNull)
                .exceptionallyAsync(Assertions::fail)
                .join();

        // Makes sure that a "404" is thrown.
        cafeAPI.getWordsEndpoint().getWord("FJDLSfjsdlfjsdfJLDKS")
                .thenAcceptAsync((word) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(NotFoundException.class, exception.getCause());
                    Assertions.assertEquals(404, ((NotFoundException) exception.getCause()).getStatusCode());
                    return null;
                }).join();

        // Makes sure that word counts are updated.
        cafeAPI.getWordsEndpoint().getWord("a")
                .thenAcceptAsync((word) -> {
                    int currentAmount = word.getUses();
                    try {
                        cafeAPI.getWordsEndpoint().updateWord("a", currentAmount + 1).get();
                    } catch (Exception e) {
                        Assertions.fail(e);
                    }

                    try {
                        Assertions.assertTrue(currentAmount < cafeAPI.getWordsEndpoint().getWord("a").get().getUses());
                    } catch (Exception e) {
                        Assertions.fail(e);
                    }
                })
                .exceptionallyAsync(Assertions::fail).join();

        // Makes sure that a NotFoundException is thrown when trying to update a word that does not exist.
        cafeAPI.getWordsEndpoint().updateWord("LSKDFJSDKLFJSDLfjsdkl", 100)
                .thenAcceptAsync((isSuccess) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(NotFoundException.class, exception.getCause());
                    return null;
                }).join();
    }
}
