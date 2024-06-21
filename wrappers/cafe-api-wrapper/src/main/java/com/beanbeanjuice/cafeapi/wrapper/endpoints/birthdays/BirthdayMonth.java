package com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays;

import lombok.Getter;

/**
 * A static {@link BirthdayMonth} class used for {@link Birthday} months.
 *
 * @author beanbeanjuice
 */
public enum BirthdayMonth {

    JANUARY (1, 31),
    FEBRUARY (2, 29),
    MARCH (3, 31),
    APRIL (4, 30),
    MAY (5, 31),
    JUNE (6, 30),
    JULY (7, 31),
    AUGUST (8, 31),
    SEPTEMBER (9, 30),
    OCTOBER (10, 31),
    NOVEMBER (11, 30),
    DECEMBER (12, 31),
    ERROR (13, 31);

    @Getter private final int monthNumber;
    @Getter private final int daysInMonth;

    /**
     * Creates a new {@link BirthdayMonth} static class.
     * @param monthNumber The {@link Integer monthNumber}.
     * @param daysInMonth The amount of {@link Integer daysInMonth}.
     */
    BirthdayMonth(final int monthNumber, final int daysInMonth) {
        this.monthNumber = monthNumber;
        this.daysInMonth = daysInMonth;
    }

}
