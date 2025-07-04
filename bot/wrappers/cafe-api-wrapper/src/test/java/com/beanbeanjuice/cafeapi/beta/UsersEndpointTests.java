package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.user.UsersEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

/**
 * A test class used to test all aspects of the {@link UsersEndpoint Users} module.
 *
 * @author beanbeanjuice
 */
public class UsersEndpointTests {

    @Test
    @DisplayName("Users Endpoint Test")
    public void testUsersEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Making sure the first user's ID is 1.
        Assertions.assertEquals(1, cafeAPI.getUsersEndpoint().getUsers().get().getFirst().getID());

        // Checking if getUsersEndpoint() ID is correct.
        Assertions.assertEquals(1, cafeAPI.getUsersEndpoint().getUser("beanbeanjuice").get().getID());

        // Making sure that exception is thrown when a user does not exist.
        cafeAPI.getUsersEndpoint().getUser("beanbeanjuiceTest")
                .thenAcceptAsync((user) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(AuthorizationException.class, exception.getCause());
                    return null;
                }).join();

        // Making sure that an exception is thrown when trying to sign up with an existing user.
        cafeAPI.getUsersEndpoint().signUp("beanbeanjuice", "passwordtesttest")
                .thenAcceptAsync((isSuccess) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(AuthorizationException.class, exception.getCause());
                    return null;
                }).join();

        // Making sure sign up is true when user does not exist.
        Assertions.assertTrue(cafeAPI.getUsersEndpoint().signUp("beanbeanjuiceTest", "passwordTest").get());

        // Making sure nothing is thrown when the user does exist.
        Assertions.assertDoesNotThrow(() -> {
            cafeAPI.getUsersEndpoint().getUser("beanbeanjuiceTest").get();
        });

        // Making sure it is true when deleting a user.
        Assertions.assertTrue(cafeAPI.getUsersEndpoint().deleteUser("beanbeanjuiceTest").get());
    }

}
