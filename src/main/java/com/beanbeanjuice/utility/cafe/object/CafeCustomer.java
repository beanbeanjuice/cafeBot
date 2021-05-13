package com.beanbeanjuice.utility.cafe.object;

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
    private int ordersBought;
    private int ordersReceived;

    /**
     * Creates a new {@link CafeCustomer} object.
     * @param userID The Discord ID of the {@link net.dv8tion.jda.api.entities.User User}.
     * @param beanCoinAmount The amount of money in the specified {@link CafeCustomer}'s bank account.
     * @param lastServingTime The {@link Timestamp} of the last serving time.
     * @param ordersBought The amount of {@link MenuItem}s that the {@link CafeCustomer} bought for others.
     * @param ordersReceived The amount of {@link MenuItem}s that the {@link CafeCustomer} received from others.
     */
    public CafeCustomer(@NotNull String userID, @NotNull Double beanCoinAmount, @Nullable Timestamp lastServingTime,
                        @NotNull Integer ordersBought, @NotNull Integer ordersReceived) {
        this.userID = userID;
        this.beanCoinAmount = beanCoinAmount;
        this.lastServingTime = lastServingTime;
        this.ordersBought = ordersBought;
        this.ordersReceived = ordersReceived;
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

    /**
     * @return The amount of orders the specified {@link CafeCustomer} has bought for others.
     */
    @NotNull
    public Integer getOrdersBought() {
        return ordersBought;
    }

    /**
     * @return The amount of orders the specified {@link CafeCustomer} has received from others.
     */
    @NotNull
    public Integer getOrdersReceived() {
        return ordersReceived;
    }

}
