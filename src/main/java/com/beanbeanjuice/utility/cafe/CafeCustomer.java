package com.beanbeanjuice.utility.cafe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;

/**
 * A custom class for {@link CafeCustomer}.
 *
 * @author beanbeanjuice
 */
public class CafeCustomer {

    private String userID;
    private double beanCoinAmount;
    private Timestamp lastServingTime;

    /**
     * Creates a new {@link CafeCustomer} object.
     * @param userID The Discord ID of the {@link net.dv8tion.jda.api.entities.User User}.
     * @param beanCoinAmount The amount of money in the specified {@link CafeCustomer}'s bank account.
     * @param lastServingTime The {@link Timestamp} of the last serving time.
     */
    public CafeCustomer(@NotNull String userID, @NotNull Double beanCoinAmount, @NotNull Timestamp lastServingTime) {
        this.userID = userID;
        this.beanCoinAmount = beanCoinAmount;
        this.lastServingTime = lastServingTime;
    }

    /**
     * @return The Discord user ID of the {@link CafeCustomer}.
     */
    @NotNull
    public String getUserID() {
        return userID;
    }

    /**
     * @return The amount of money in the specified {@link CafeCustomer}'s bank account.
     */
    @NotNull
    public Double getBeanCoinAmount() {
        return beanCoinAmount;
    }

    /**
     * @return The time of the last serving for the {@link CafeCustomer}.
     */
    @Nullable
    public Timestamp getLastServingTime() {
        return lastServingTime;
    }

}
