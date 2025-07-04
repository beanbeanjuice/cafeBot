package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.pictures.InteractionPicturesEndpoint;
import com.beanbeanjuice.kawaiiapi.wrapper.KawaiiAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
                        (kawaiiAPIString) -> {
                            try {
                                Assertions.assertTrue(cafeAPI.getInteractionPicturesEndpoint().getRandomInteractionPicture(type).get().get().startsWith("https://api.kawaii.red/gif/"));
                            } catch (Exception e) {
                                Assertions.fail();
                            }
                        },
                        () -> Assertions.assertNotNull(cafeAPI.getInteractionPicturesEndpoint().getRandomInteractionPicture(type))
                );
            }
        });
    }

    @Test
    @DisplayName("Interaction Pictures Endpoint Authorization Error Test")
    public void testInteractionPicturesAuthorizationError() throws InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", "fake", RequestLocation.BETA);

        AtomicReference<Throwable> cause = new AtomicReference<>();

        cafeAPI.getInteractionPicturesEndpoint().getRandomInteractionPicture(InteractionType.STAB).exceptionally((exception) -> {
            cause.set(exception.getCause());
            return Optional.empty();
        });

        Thread.sleep(1000);
        Assertions.assertInstanceOf(AuthorizationException.class, cause.get());
    }

}
