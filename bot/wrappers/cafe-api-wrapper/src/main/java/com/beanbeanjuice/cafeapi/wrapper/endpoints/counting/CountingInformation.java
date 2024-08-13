package com.beanbeanjuice.cafeapi.wrapper.endpoints.counting;

import lombok.Getter;

/**
 * A class used for {@link CountingInformation}.
 *
 * @author beanbeanjuice
 */
public class CountingInformation {

    @Getter private final int highestNumber;
    @Getter private final int lastNumber;
    @Getter private final String lastUserID;
    @Getter private final String failureRoleID;

    /**
     * Creates a new {@link CountingInformation} object.
     * @param highestNumber The {@link Integer highestNumber}.
     * @param lastNumber The {@link Integer lastNumber}.
     * @param lastUserID The {@link String lastUserID}.
     * @param failureRoleID The {@link String failureRoleID}.
     */
    public CountingInformation(final int highestNumber, final int lastNumber, final String lastUserID, final String failureRoleID) {
        this.highestNumber = highestNumber;
        this.lastNumber = lastNumber;
        this.lastUserID = lastUserID;
        this.failureRoleID = failureRoleID;
    }

}
