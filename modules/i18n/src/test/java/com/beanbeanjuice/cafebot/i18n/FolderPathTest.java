package com.beanbeanjuice.cafebot.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;

public class FolderPathTest {

    @Test
    @DisplayName("Test Single Folder")
    public void testSingleFolder() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        I18N i18n = new I18N(Locale.ENGLISH);

        Method method = I18N.class.getDeclaredMethod("findFilePath", String.class, Locale.class);
        method.setAccessible(true);

        Optional<String> result = (Optional<String>) method.invoke(i18n, "command.ai", Locale.ENGLISH);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("i18n/en/command/ai.yml", result.get());
    }

    @Test
    @DisplayName("Test No Folder")
    public void testNoFolder() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        I18N i18n = new I18N(Locale.ENGLISH);

        Method method = I18N.class.getDeclaredMethod("findFilePath", String.class, Locale.class);
        method.setAccessible(true);

        Optional<String> result = (Optional<String>) method.invoke(i18n, "generic", Locale.ENGLISH);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("i18n/en/generic.yml", result.get());
    }

    @Test
    @DisplayName("Test With Extra Key")
    public void testWithExtraKey() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        I18N i18n = new I18N(Locale.ENGLISH);

        Method method = I18N.class.getDeclaredMethod("findFilePath", String.class, Locale.class);
        method.setAccessible(true);

        Optional<String> result = (Optional<String>) method.invoke(i18n, "command.ai.description", Locale.ENGLISH);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("i18n/en/command/ai.yml", result.get());
    }

    @Test
    @DisplayName("Test File English Fallback")
    public void testEnglishFallback() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        I18N i18n = new I18N(Locale.CHINA);

        Method method = I18N.class.getDeclaredMethod("findFilePath", String.class, Locale.class);
        method.setAccessible(true);

        Optional<String> result = (Optional<String>) method.invoke(i18n, "command.ai.description", Locale.CHINA);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("i18n/en/command/ai.yml", result.get());
    }

    @Test
    @DisplayName("Test File with Valid Language")
    public void testValidLanguage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        I18N i18n = new I18N(Locale.UK);

        Method method = I18N.class.getDeclaredMethod("findFilePath", String.class, Locale.class);
        method.setAccessible(true);

        Optional<String> result = (Optional<String>) method.invoke(i18n, "info.bot.greeting", Locale.UK);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("i18n/en_GB/info.yml", result.get());
    }

    @Test
    @DisplayName("Test Missing English File")
    public void testMissingEnglishFile() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        I18N i18n = new I18N(Locale.ENGLISH);

        Method method = I18N.class.getDeclaredMethod("findFilePath", String.class, Locale.class);
        method.setAccessible(true);

        Optional<String> result = (Optional<String>) method.invoke(i18n, "this.path.does.not.exist", Locale.ENGLISH);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Test Missing Foreign and English Language File")
    public void testMissingForeignLanguageFile() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        I18N i18n = new I18N(Locale.UK);

        Method method = I18N.class.getDeclaredMethod("findFilePath", String.class, Locale.class);
        method.setAccessible(true);

        Optional<String> result = (Optional<String>) method.invoke(i18n, "this.path.does.not.exist", Locale.UK);

        Assertions.assertFalse(result.isPresent());
    }

}
