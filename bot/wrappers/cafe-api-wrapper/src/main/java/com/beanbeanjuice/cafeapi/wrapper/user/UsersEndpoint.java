package com.beanbeanjuice.cafeapi.wrapper.user;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class UsersEndpoint extends CafeEndpoint {

    public CompletableFuture<ArrayList<User>> getUsers() {
        return RequestBuilder.create(RequestRoute.CAFE, RequestType.GET)
                .setRoute("/users")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    ArrayList<User> users = new ArrayList<>();

                    request.getData().get("users").forEach(user -> {
                        int id = user.get("user_id").intValue();
                        String username = user.get("username").textValue();
                        UserType userType = UserType.valueOf(user.get("user_type").textValue());
                        users.add(new User(id, username, userType));
                    });

                    return users;
                });
    }

    public CompletableFuture<Boolean> signUp(final String username, final String password) {
        return RequestBuilder.create(RequestRoute.CAFE, RequestType.POST)
                .setRoute("/user/signup")
                .addParameter("username", username)
                .addParameter("password", password)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 201);
    }

    public CompletableFuture<User> getUser(final String username) {
        return RequestBuilder.create(RequestRoute.CAFE, RequestType.GET)
                .setRoute("/user/" + username)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    int ID = request.getData().get("user").get("user_id").intValue();
                    UserType userType = UserType.valueOf(request.getData().get("user").get("user_type").textValue());
                    return new User(ID, username, userType);
                });
    }

    public CompletableFuture<Boolean> deleteUser(final String username) {
        return RequestBuilder.create(RequestRoute.CAFE, RequestType.DELETE)
                .setRoute("/user/" + username)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

}
