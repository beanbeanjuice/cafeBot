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
    @DisplayName("can format negative small number")
    public void canFormatNegativeSmallNumber() {
        long number = -10;
        String formattedNumber = Helper.formatNumber(number);
        Assertions.assertEquals("-10", formattedNumber);
    }

    @Test
    @DisplayName("can format large number")
    public void canFormatLargeNumber() {
        long number = 1_000_000_000;
        String formattedNumber = Helper.formatNumber(number);
        Assertions.assertEquals("1,000,000,000", formattedNumber);
    }

    @Test
    @DisplayName("can format negative large number")
    public void canFormatNegativeLargeNumber() {
        long number = -1_000_000_000;
        String formattedNumber = Helper.formatNumber(number);
        Assertions.assertEquals("-1,000,000,000", formattedNumber);
    }

    @Test
    @DisplayName("can format biggest number")
    public void canFormatNumber() {
        String formattedNumber = Helper.formatNumber(Long.MAX_VALUE);
        Assertions.assertEquals("9,223,372,036,854,775,807", formattedNumber);
    }

    @Test
    @DisplayName("can format negative biggest number")
    public void canFormatNegativeNumber() {
        String formattedNumber = Helper.formatNumber(Long.MIN_VALUE);
        Assertions.assertEquals("-9,223,372,036,854,775,808", formattedNumber);
    }

}
