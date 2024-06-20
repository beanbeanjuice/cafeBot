package com.beanbeanjuice.cafeapi.cafebot.cafe;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.requests.Request;

/**
 * A static {@link CafeType} used for the {@link CafeUsers} API.
 *
 * @author beanbeanjuice
 */
public enum CafeType {

    BEAN_COINS ("bean_coins"),
    LAST_SERVING_TIME ("last_serving_time"),
    ORDERS_BOUGHT ("orders_bought"),
    ORDERS_RECEIVED ("orders_received");

    private final String type;

    /**
     * Creates a new {@link CafeType} static object.
     * @param type The {@link String type} used for the {@link CafeAPI CafeAPI}.
     */
    CafeType(String type) {
        this.type = type;
    }

    /**
     * @return The {@link String type} used for {@link CafeAPI CafeAPI} {@link Request Requests}.
     */
    public String getType() {
        return type;
    }

}
