package com.beanbeanjuice.utility.cafe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;

public class CafeCustomer {

    private String userID;
    private double beanCoinAmount;
    private Timestamp lastServingTime;

    public CafeCustomer(@NotNull String userID, @NotNull Double beanCoinAmount, @NotNull Timestamp lastServingTime) {
        this.userID = userID;
        this.beanCoinAmount = beanCoinAmount;
        this.lastServingTime = lastServingTime;
    }

    @NotNull
    public String getUserID() {
        return userID;
    }

    @NotNull
    public Double getBeanCoinAmount() {
        return beanCoinAmount;
    }

    @Nullable
    public Timestamp getLastServingTime() {
        return lastServingTime;
    }

}
