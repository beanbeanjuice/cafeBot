package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.cafebot.utility.helper.Helper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NumberFormatTest {

    @Test
    @DisplayName("can format small number")
    public void canFormatSmallNumber() {
        long number = 10;
        String formattedNumber = Helper.formatNumber(number);

        Assertions.assertEquals("10", formattedNumber);
    }

    @Test
    @DisplayName("can format large number")
    public void canFormatLargeNumber() {
        long number = 1_000_000_000;
        String formattedNumber = Helper.formatNumber(number);
        Assertions.assertEquals("1,000,000,000", formattedNumber);
    }

    @Test
    @DisplayName("can format number")
    public void canFormatNumber() {
        long number = 1000;
        String formattedNumber = Helper.formatNumber(number);
        Assertions.assertEquals("1,000", formattedNumber);
    }

}
