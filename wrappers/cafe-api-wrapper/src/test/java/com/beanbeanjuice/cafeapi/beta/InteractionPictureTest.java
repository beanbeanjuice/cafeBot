package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import com.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link com.beanbeanjuice.cafeapi.cafebot.interactions.pictures.InteractionPictures InteractionPictures}.
 * This confirms that the {@link CafeAPI} and {@link com.beanbeanjuice.KawaiiAPI KawaiiAPI} are both working.
 *
 * @author beanbeanjuice
 * @since 1.3.1
 */
public class InteractionPictureTest {

    @Test
    @DisplayName("Interaction Pictures API Test")
    public void testInteractionPicturesAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Goes through every interaction type.
        Assertions.assertDoesNotThrow(() -> {
            for (InteractionType type : InteractionType.values()) {
                type.getKawaiiAPIString().ifPresentOrElse(
                        (kawaiiAPIString) -> Assertions.assertTrue(cafeAPI.INTERACTION_PICTURE.getRandomInteractionPicture(type).startsWith("https://api.kawaii.red/gif/")),
                        () -> Assertions.assertNotNull(cafeAPI.INTERACTION_PICTURE.getRandomInteractionPicture(type))
                );
            }
        });
    }

}
