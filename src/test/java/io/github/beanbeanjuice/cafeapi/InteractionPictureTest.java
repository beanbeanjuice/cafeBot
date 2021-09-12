package io.github.beanbeanjuice.cafeapi;

import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InteractionPictureTest {

    @Test
    @DisplayName("Interaction Pictures API Test")
    public void testInteractionPicturesAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"));

        // Goes through every interaction type.
        Assertions.assertDoesNotThrow(() -> {
            for (InteractionType type : InteractionType.values()) {
                Assertions.assertNotNull(cafeAPI.interactionPictures().getRandomInteractionPicture(type));
            }
        });
    }

}
