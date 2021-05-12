package com.beanbeanjuice.utility.cafe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class CafeCustomer {

    private String userID;
    private double beanCoinAmount;
    private Date lastServingTime;

    public CafeCustomer(@NotNull String userID, @NotNull Double beanCoinAmount, @NotNull Date lastServingTime) {
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
    public Date getLastServingTime() {
        return lastServingTime;
    }

}
