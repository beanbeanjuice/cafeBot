package com.beanbeanjuice.cafeapi.cafebot.cafe;

import org.jetbrains.annotations.Nullable;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * A class used for {@link CafeUser} objects.
 *
 * @author beanbeanjuice
 */
public class CafeUser {

    private final String userID;
    private final double beanCoins;
    private final Timestamp lastServingTime;
    private final int ordersBought;
    private final int ordersReceived;

    /**
     * Creates a new {@link CafeUser} object.
     * @param userID The {@link String userID}.
     * @param beanCoins The {@link Double beanCoins}.
     * @param lastServingTime The {@link Timestamp lastServingTime}.
     * @param ordersBought The {@link Integer ordersBought}.
     * @param ordersReceived The {@link Integer ordersReceived}.
     */
    public CafeUser(String userID, double beanCoins, @Nullable Timestamp lastServingTime,
                    int ordersBought, int ordersReceived) {
        this.userID = userID;
        this.beanCoins = beanCoins;
        this.lastServingTime = lastServingTime;
        this.ordersBought = ordersBought;
        this.ordersReceived = ordersReceived;
    }

    /**
     * @return The {@link String lastUserID}.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @return The {@link Double beanCoins}.
     */
    public double getBeanCoins() {
        return beanCoins;
    }

    /**
     * @return The {@link Timestamp lastServingTime}.
     */
    public Optional<Timestamp> getLastServingTime() {
        return Optional.ofNullable(lastServingTime);
    }

    /**
     * @return The {@link Integer ordersBought}.
     */
    public int getOrdersBought() {
        return ordersBought;
    }

    /**
     * @return The {@link Integer ordersReceived}.
     */
    public int getOrdersReceived() {
        return ordersReceived;
    }

}
