package com.beanbeanjuice.cafeapi.wrapper.user;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ResponseException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;

import java.util.ArrayList;

/**
 * A class used for everything to do with {@link UsersEndpoint} in the API.
 *
 * @author beanbeanjuice
 */
public class UsersEndpoint extends CafeEndpoint {

    /**
     * @return The {@link ArrayList} of {@link User users} in the API database.
     * @throws AuthorizationException Thrown when the logged in account does not have access to view all users.
     */
    public ArrayList<User> getUsers() throws AuthorizationException, ResponseException {
        ArrayList<User> users = new ArrayList<>();

        Request request = new RequestBuilder(RequestRoute.CAFE, RequestType.GET)
                .setRoute("/users")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        request.getData().get("users").forEach(user -> {
            int id = user.get("user_id").intValue();
            String username = user.get("username").textValue();
            UserType userType = UserType.valueOf(user.get("user_type").textValue());
            users.add(new User(id, username, userType));
        });

        return users;
    }

    /**
     * Signup a {@link User}.
     * @param username The {@link String username} of the {@link User}.
     * @param password The {@link String password} of the {@link User}.
     * @return True, if successfully signed up.
     */
    public boolean signUp(final String username, final String password) {
        Request request = new RequestBuilder(RequestRoute.CAFE, RequestType.POST)
                .setRoute("/user/signup")
                .addParameter("username", username)
                .addParameter("password", password)
                .build().orElseThrow();

        return request.getStatusCode() == 201;
    }

    /**
     * Retrieves a {@link User} from the {@link com.beanbeanjuice.cafeapi.wrapper.CafeAPI CafeAPI}.
     * @param username The {@link String username} of the {@link User}.
     * @return The specified {@link User}.
     * @throws AuthorizationException Thrown when the {@link String apiKey} is invalid.
     * @throws ResponseException Thrown when there is a generic server-side {@link CafeException CafeException}.
     */
    public User getUser(final String username) throws AuthorizationException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFE, RequestType.GET)
                .setRoute("/user/" + username)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        int ID = request.getData().get("user").get("user_id").intValue();
        UserType userType = UserType.valueOf(request.getData().get("user").get("user_type").textValue());
        return new User(ID, username, userType);
    }

    /**
     * Deletes a {@link User} from the {@link com.beanbeanjuice.cafeapi.wrapper.CafeAPI CafeAPI}.
     * @param username The {@link String username} of the {@link User}.
     * @return True, if the {@link User} was successfully deleted.
     */
    public boolean deleteUser(final String username) {
        Request request = new RequestBuilder(RequestRoute.CAFE, RequestType.DELETE)
                .setRoute("/user/" + username)
                .setAuthorization(apiKey).build().orElseThrow();
        return request.getStatusCode() == 200;
    }

}
