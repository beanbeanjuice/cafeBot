package com.beanbeanjuice.cafebot.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class YamlControl extends ResourceBundle.Control {

    public static final YamlControl INSTANCE = new YamlControl();
    private YamlControl() {}

    @Override
    public List<String> getFormats(String baseName) {
        return List.of("yaml");
    }

    @Override
    public ResourceBundle newBundle(
            String baseName,
            Locale locale,
            String format,
            ClassLoader loader,
            boolean reload
    ) throws IOException {
        // Build locale string: "en_GB", "en_US", or just "en"
        String localeString = locale.getLanguage();
        if (!locale.getCountry().isEmpty()) {
            localeString += "_" + locale.getCountry();
        }

        // Check if at least one YAML file exists for this locale
        // Try a common base file to see if the locale directory exists
        String testPath = "i18n/" + localeString + "/";

        // We need to check if ANY file exists for this locale
        // Try some common paths
        boolean localeExists = false;
        String[] commonFiles = {"info.yml", "generic.yml", "commands.yml"};

        for (String file : commonFiles) {
            try (InputStream stream = loader.getResourceAsStream(testPath + file)) {
                if (stream != null) {
                    localeExists = true;
                    break;
                }
            }
        }

        // If no files exist for this locale, return null so ResourceBundle tries fallback
        if (!localeExists) {
            return null;
        }

        // Create and return the bundle
        return new I18N(locale, loader);
    }

    @Override
    public Locale getFallbackLocale(String baseName, Locale locale) {
        // Always fall back to English, unless we're already in English
        if (locale.equals(Locale.ENGLISH)) {
            return null; // No further fallback
        }
        return Locale.ENGLISH;
    }

}
