package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.pictures.InteractionPicturesEndpoint;
import com.beanbeanjuice.kawaiiapi.wrapper.KawaiiAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link InteractionPicturesEndpoint InteractionPictures}.
 * This confirms that the {@link CafeAPI} and {@link KawaiiAPI KawaiiAPI} are both working.
 *
 * @author beanbeanjuice
 * @since 1.3.1
 */
public class InteractionPictureTest {

    @Test
    @DisplayName("Interaction Pictures Endpoint Test")
    public void testInteractionPicturesEndpoint() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Goes through every interaction type.
        Assertions.assertDoesNotThrow(() -> {
            for (InteractionType type : InteractionType.values()) {
                type.getKawaiiAPIString().ifPresentOrElse(
                        (kawaiiAPIString) -> Assertions.assertTrue(cafeAPI.getInteractionPicturesEndpoint().getRandomInteractionPicture(type).startsWith("https://api.kawaii.red/gif/")),
                        () -> Assertions.assertNotNull(cafeAPI.getInteractionPicturesEndpoint().getRandomInteractionPicture(type))
                );
            }
        });
    }

}
