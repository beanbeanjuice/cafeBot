package com.beanbeanjuice.cafebot.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class KeyTest {

    @Test
    @DisplayName("can get default messages")
    public void canGetDefaultMessages() {
        I18N bundle = I18N.getBundle();
        Assertions.assertEquals("Hello, world!", bundle.getString("info.bot.greeting"));
    }

    @Test
    @DisplayName("different language works")
    public void differentLanguageWorks() {
        I18N bundle = I18N.getBundle(Locale.UK);
        Assertions.assertEquals("Hello, eh?!", bundle.getString("info.bot.greeting"));
    }

    @Test
    @DisplayName("fallback works if language file exists but does not contain key")
    public void fallbackWorksIfLanguageDoesNotContainKey() {
        I18N bundle = I18N.getBundle(Locale.UK);
        Assertions.assertEquals("cafeBot", bundle.getString("info.bot.name"));
    }

    @Test
    @DisplayName("fallback works if language file does not exist")
    public void fallbackWorksIfLanguageDoesNotExist() {
        I18N bundle = I18N.getBundle(Locale.TRADITIONAL_CHINESE);

        Assertions.assertEquals("Hello, world!", bundle.getString("info.bot.greeting"));
    }

    @Test
    @DisplayName("returns path if does not exist")
    public void returnsPathIfDoesNotExist() {
        I18N bundle = I18N.getBundle();
        String result = bundle.getString("path.that.does.not.exist");

        Assertions.assertEquals("path.that.does.not.exist", result);
    }

    @Test
    @DisplayName("returns string if does exist")
    public void returnsPathIfDoesExist() {
        I18N bundle = I18N.getBundle();
        String result = bundle.getString("info.bot.name");

        Assertions.assertEquals("cafeBot", result);
    }

}
