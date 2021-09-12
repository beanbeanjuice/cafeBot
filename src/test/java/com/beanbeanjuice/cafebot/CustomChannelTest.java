package com.beanbeanjuice.cafebot;

import com.beanbeanjuice.utility.guild.CustomChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomChannelTest {

    @Test
    @DisplayName("Custom Channel Enumeration Test")
    public void customChannelTest() {
        Assertions.assertEquals(CustomChannel.BIRTHDAY.getName(), "Birthday Channel", "Testing the Custom Channel Enumeration");
    }

}
