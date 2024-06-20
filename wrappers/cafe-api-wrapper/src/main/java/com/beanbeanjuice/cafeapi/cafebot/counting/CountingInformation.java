package com.beanbeanjuice.cafeapi.cafebot.counting;

/**
 * A class used for {@link CountingInformation}.
 *
 * @author beanbeanjuice
 */
public class CountingInformation {

    private final int highestNumber;
    private final int lastNumber;
    private final String lastUserID;
    private final String failureRoleID;

    /**
     * Creates a new {@link CountingInformation} object.
     * @param highestNumber The {@link Integer highestNumber}.
     * @param lastNumber The {@link Integer lastNumber}.
     * @param lastUserID The {@link String lastUserID}.
     * @param failureRoleID The {@link String failureRoleID}.
     */
    public CountingInformation(int highestNumber, int lastNumber, String lastUserID, String failureRoleID) {
        this.highestNumber = highestNumber;
        this.lastNumber = lastNumber;
        this.lastUserID = lastUserID;
        this.failureRoleID = failureRoleID;
    }

    /**
     * @return The {@link Integer highestNumber}.
     */
    public int getHighestNumber() {
        return highestNumber;
    }

    /**
     * @return The {@link Integer lastNumber}.
     */
    public int getLastNumber() {
        return lastNumber;
    }

    /**
     * @return The {@link String lastUserID}.
     */
    public String getLastUserID() {
        return lastUserID;
    }

    /**
     * @return The {@link String failureRoleID}.
     */
    public String getFailureRoleID() {
        return failureRoleID;
    }

}
