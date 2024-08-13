package com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * A class used for {@link CafeUser} objects.
 *
 * @author beanbeanjuice
 */
public class CafeUser {

    @Getter private final String userID;
    @Getter private final double beanCoins;
    private final Timestamp lastServingTime;
    @Getter private final int ordersBought;
    @Getter private final int ordersReceived;

    /**
     * Creates a new {@link CafeUser} object.
     * @param userID The {@link String userID}.
     * @param beanCoins The {@link Double beanCoins}.
     * @param lastServingTime The {@link Timestamp lastServingTime}.
     * @param ordersBought The {@link Integer ordersBought}.
     * @param ordersReceived The {@link Integer ordersReceived}.
     */
    public CafeUser(final String userID, final double beanCoins, @Nullable final Timestamp lastServingTime,
                    final int ordersBought, final int ordersReceived) {
        this.userID = userID;
        this.beanCoins = beanCoins;
        this.lastServingTime = lastServingTime;
        this.ordersBought = ordersBought;
        this.ordersReceived = ordersReceived;
    }

    /**
     * @return The {@link Timestamp lastServingTime}.
     */
    public Optional<Timestamp> getLastServingTime() {
        return Optional.ofNullable(lastServingTime);
    }

}
