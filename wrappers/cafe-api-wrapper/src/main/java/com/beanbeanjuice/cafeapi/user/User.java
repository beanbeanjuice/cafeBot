package com.beanbeanjuice.cafeapi.user;

import lombok.Getter;

/**
 * A {@link User} class used for contextualising users in the API database.
 *
 * @author beanbeanjuice
 */
public class User {

    @Getter private final int ID;
    @Getter private final String username;
    @Getter private final UserType userType; // TODO: Make something for updating the UserType

    /**
     * Creates a new {@link User}.
     * @param id The {@link Integer ID} of the {@link User}.
     * @param username The {@link String username} of the {@link User}.
     * @param userType The {@link UserType user type} of the {@link User}.
     */
    public User(int id, String username, UserType userType) {
        this.ID = id;
        this.username = username;
        this.userType = userType;
    }

}
