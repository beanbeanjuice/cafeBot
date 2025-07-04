package com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import lombok.Getter;

/**
 * A static {@link CafeType} used for the {@link CafeUsersEndpoint} API.
 *
 * @author beanbeanjuice
 */
public enum CafeType {

    BEAN_COINS ("bean_coins"),
    LAST_SERVING_TIME ("last_serving_time"),
    ORDERS_BOUGHT ("orders_bought"),
    ORDERS_RECEIVED ("orders_received");

    @Getter private final String type;

    /**
     * Creates a new {@link CafeType} static object.
     * @param type The {@link String type} used for the {@link CafeAPI CafeAPI}.
     */
    CafeType(final String type) {
        this.type = type;
    }

}
