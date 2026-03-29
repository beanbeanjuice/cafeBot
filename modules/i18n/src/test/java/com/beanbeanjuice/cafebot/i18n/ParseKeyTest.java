package com.beanbeanjuice.cafebot.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public class ParseKeyTest {

    @Test
    @DisplayName("Test Valid Key Parse")
    public void testValidKeyParse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        I18N i18n = new I18N(Locale.US);

        Method method = I18N.class.getDeclaredMethod("parseKey", String.class, String.class);
        method.setAccessible(true);

        String result = (String) method.invoke(i18n, "i18n/en/command/ai.yml", "command.ai.embed.title");

        Assertions.assertEquals("embed.title", result);
    }

    @Test
    @DisplayName("Test Invalid Key Parse")
    public void testInvalidKeyParse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        I18N i18n = new I18N(Locale.US);

        Method method = I18N.class.getDeclaredMethod("parseKey", String.class, String.class);
        method.setAccessible(true);

        String result = (String) method.invoke(i18n, "i18n/en/command/ai.yml", "this.is.not.a.real.command");

        Assertions.assertEquals("this.is.not.a.real.command", result);
    }

}
