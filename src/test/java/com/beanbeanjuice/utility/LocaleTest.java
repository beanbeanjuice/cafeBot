package com.beanbeanjuice.utility;

import net.dv8tion.jda.api.interactions.DiscordLocale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LocaleTest {

    @Test
    @DisplayName("can format discord locale")
    public void canFormatDiscordLocaleFromLowerCase() {
        String locale = "en-GB";
        Assertions.assertEquals(DiscordLocale.ENGLISH_UK, DiscordLocale.from(locale));
    }

}
