package com.beanbeanjuice.cafebot.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

public class I18NTest {

    @Test
    @DisplayName("Can Get I18N from Folder")
    public void canGetI18N() {
        I18N i18n = new I18N(Locale.ENGLISH);

        String description = i18n.getString("command.ai.description");
        Assertions.assertEquals("Want a sassy AI that will serve you some coffee?", description);
    }

    @Test
    @DisplayName("Can Get I18N String Array")
    public void canGetI18NStringArray() {
        I18N i18n = new I18N(Locale.ENGLISH);

        List<String> answers = i18n.getStringArray("command.eightball.answers.positive");

        Assertions.assertFalse(answers.isEmpty());
        Assertions.assertEquals("It is likely...", answers.getFirst());
    }

    @Test
    @DisplayName("Can Get I18N Fallback String Array")
    public void canGetI18NFallbackStringArray() {
        I18N i18n = new I18N(Locale.CHINA);

        List<String> answers = i18n.getStringArray("command.eightball.answers.positive");

        Assertions.assertFalse(answers.isEmpty());
        Assertions.assertEquals("It is likely...", answers.getFirst());
    }

    @Test
    @DisplayName("Can Get I18N without Folder")
    public void canGetI18NWithoutFolder() {
        I18N i18n = new I18N(Locale.ENGLISH);

        String description = i18n.getString("info.bot.name");
        Assertions.assertEquals("cafeBot", description);
    }

    @Test
    @DisplayName("Test With Invalid Key")
    public void testWithInvalidKey() {
        I18N i18n = new I18N(Locale.ENGLISH);

        String description = i18n.getString("this.key.does.not.exist");
        Assertions.assertEquals("this.key.does.not.exist", description);
    }

    @Test
    @DisplayName("Test Foreign Language")
    public void testForeignLanguage() {
        I18N i18n = new I18N(Locale.UK);

        String description = i18n.getString("info.bot.greeting");
        Assertions.assertEquals("Hello, eh?!", description);
    }

    @Test
    @DisplayName("Test Valid English Fallback - Missing Foreign, Valid English")
    public void testValidEnglishFallback() {
        I18N i18n = new I18N(Locale.CHINA);

        String description = i18n.getString("info.bot.name");
        Assertions.assertEquals("cafeBot", description);
    }

    @Test
    @DisplayName("Test Invalid English Fallback - Missing Foreign, Missing English")
    public void testInvalidEnglishFallback() {
        I18N i18n = new I18N(Locale.CHINA);

        String description = i18n.getString("this.key.does.not.exist");
        Assertions.assertEquals("this.key.does.not.exist", description);
    }

    @Test
    @DisplayName("Test Valid English Fallback - Valid Foreign, Missing Key, Valid English")
    public void testValidEnglishFallbackWithKey() {
        I18N i18n = new I18N(Locale.UK);

        String description = i18n.getString("info.bot.name");
        Assertions.assertEquals("cafeBot", description);
    }

}
