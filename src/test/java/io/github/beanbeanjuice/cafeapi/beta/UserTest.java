package io.github.beanbeanjuice.cafeapi.beta;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.exception.AuthorizationException;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A test class used to test all aspects of the {@link io.github.beanbeanjuice.cafeapi.user.Users Users} module.
 *
 * @author beanbeanjuice
 */
public class UserTest {

    @Test
    @DisplayName("Test Users API")
    public void userAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Making sure the first user's ID is 1.
        Assertions.assertEquals(1, cafeAPI.users().getUsers().get(0).getID());

        // Checking if USER ID is correct.
        Assertions.assertEquals(1, cafeAPI.users().getUser("beanbeanjuice").getID());

        // Making sure that exception is thrown when a user does not exist.
        Assertions.assertThrows(AuthorizationException.class, () -> {
            cafeAPI.users().getUser("beanbeanjuiceTest");
        });

        // Making sure that an exception is thrown when trying to sign up with an existing user.
        Assertions.assertThrows(AuthorizationException.class, () -> {
            cafeAPI.users().signUp("beanbeanjuice", "passwordtesttest");
        });

        // Making sure sign up is true when user does not exist.
        Assertions.assertTrue(cafeAPI.users().signUp("beanbeanjuiceTest", "passwordTest"));

        // Making sure nothing is thrown when the user does exist.
        Assertions.assertDoesNotThrow(() -> {
            cafeAPI.users().getUser("beanbeanjuiceTest");
        });

        // Making sure it is true when deleting a user.
        Assertions.assertTrue(cafeAPI.users().deleteUser("beanbeanjuiceTest"));
    }

}
