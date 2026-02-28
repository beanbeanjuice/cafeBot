package com.beanbeanjuice.cafebot.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FormattingTest {

    private static final String I18N_PATH = "i18n";
    private static final String ENGLISH_LOCALE = "en";

    @Test
    @DisplayName("all translations only contain keys present in English")
    void allTranslationsOnlyContainEnglishKeys() throws IOException, URISyntaxException {
        // Get all locale directories
        Set<String> locales = getLocaleDirectories();
        locales.remove(ENGLISH_LOCALE); // We'll compare against English

        // Get all English files and their keys
        Map<String, Set<String>> englishKeysPerFile = loadAllKeysForLocale(ENGLISH_LOCALE);

        List<String> errors = new ArrayList<>();

        for (String locale : locales) {
            Map<String, Set<String>> localeKeysPerFile = loadAllKeysForLocale(locale);

            for (Map.Entry<String, Set<String>> entry : localeKeysPerFile.entrySet()) {
                String filePath = entry.getKey();
                Set<String> localeKeys = entry.getValue();
                Set<String> englishKeys = englishKeysPerFile.get(filePath);

                if (englishKeys == null) {
                    // This will be caught by the file structure test
                    continue;
                }

                // Check for keys in locale that don't exist in English
                Set<String> extraKeys = new HashSet<>(localeKeys);
                extraKeys.removeAll(englishKeys);

                if (!extraKeys.isEmpty()) {
                    errors.add(String.format("Locale '%s' file '%s' contains extra keys not in English: %s",
                            locale, filePath, extraKeys));
                }
            }
        }

        if (!errors.isEmpty()) {
            Assertions.fail("Translation key validation failed:\n" + String.join("\n", errors));
        }
    }

    @Test
    @DisplayName("all translations only contain files present in English")
    void allTranslationsOnlyContainEnglishFiles() throws IOException, URISyntaxException {
        // Get all locale directories
        Set<String> locales = getLocaleDirectories();
        locales.remove(ENGLISH_LOCALE);

        // Get all English file paths
        Set<String> englishFiles = getAllFilesForLocale(ENGLISH_LOCALE);

        List<String> errors = new ArrayList<>();

        for (String locale : locales) {
            Set<String> localeFiles = getAllFilesForLocale(locale);

            // Check for files in locale that don't exist in English
            Set<String> extraFiles = new HashSet<>(localeFiles);
            extraFiles.removeAll(englishFiles);

            if (!extraFiles.isEmpty()) {
                errors.add(String.format("Locale '%s' contains extra files not in English: %s",
                        locale, extraFiles));
            }
        }

        if (!errors.isEmpty()) {
            Assertions.fail("File structure validation failed:\n" + String.join("\n", errors));
        }
    }

    @Test
    @DisplayName("all translations contain bot.discord-locale in info.yml")
    void allTranslationsContainDiscordLocale() throws IOException, URISyntaxException {
        // Get all locale directories
        Set<String> locales = getLocaleDirectories();

        List<String> errors = new ArrayList<>();

        for (String locale : locales) {
            String resourcePath = I18N_PATH + "/" + locale + "/info.yml";
            Set<String> keys = loadKeysFromFile(resourcePath);

            if (keys.isEmpty()) {
                errors.add(String.format("Locale '%s' is missing info.yml file", locale));
            } else if (!keys.contains("bot.discord-locale")) {
                errors.add(String.format("Locale '%s' is missing 'bot.discord-locale' in info.yml", locale));
            }
        }

        if (!errors.isEmpty()) {
            Assertions.fail("Discord locale validation failed:\n" + String.join("\n", errors));
        }
    }

    private Set<String> getLocaleDirectories() throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource(I18N_PATH).toURI();

        if (uri.getScheme().equals("jar")) {
            try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                Path path = fileSystem.getPath("/" + I18N_PATH);
                try (Stream<Path> paths = Files.list(path)) {
                    return paths
                            .filter(Files::isDirectory)
                            .map(p -> p.getFileName().toString())
                            .collect(Collectors.toSet());
                }
            }
        } else {
            Path path = Paths.get(uri);
            try (Stream<Path> paths = Files.list(path)) {
                return paths
                        .filter(Files::isDirectory)
                        .map(p -> p.getFileName().toString())
                        .collect(Collectors.toSet());
            }
        }
    }

    private Set<String> getAllFilesForLocale(String locale) throws IOException, URISyntaxException {
        Set<String> files = new HashSet<>();
        String localePath = I18N_PATH + "/" + locale;

        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource(localePath).toURI();

        if (uri.getScheme().equals("jar")) {
            try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                Path path = fileSystem.getPath("/" + localePath);
                collectYamlFiles(path, "", files);
            }
        } else {
            Path path = Paths.get(uri);
            collectYamlFiles(path, "", files);
        }

        return files;
    }

    private void collectYamlFiles(Path dir, String prefix, Set<String> files) throws IOException {
        try (Stream<Path> paths = Files.list(dir)) {
            List<Path> pathList = paths.collect(Collectors.toList());
            for (Path path : pathList) {
                String fileName = path.getFileName().toString();
                String relativePath = prefix.isEmpty() ? fileName : prefix + "/" + fileName;

                if (Files.isDirectory(path)) {
                    collectYamlFiles(path, relativePath, files);
                } else if (fileName.endsWith(".yml") || fileName.endsWith(".yaml")) {
                    files.add(relativePath);
                }
            }
        }
    }

    private Map<String, Set<String>> loadAllKeysForLocale(String locale) throws IOException, URISyntaxException {
        Map<String, Set<String>> keysPerFile = new HashMap<>();
        Set<String> files = getAllFilesForLocale(locale);

        for (String file : files) {
            String resourcePath = I18N_PATH + "/" + locale + "/" + file;
            Set<String> keys = loadKeysFromFile(resourcePath);
            keysPerFile.put(file, keys);
        }

        return keysPerFile;
    }

    private Set<String> loadKeysFromFile(String resourcePath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream stream = classLoader.getResourceAsStream(resourcePath)) {
            if (stream == null) {
                return Collections.emptySet();
            }

            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(stream);

            if (data == null) {
                return Collections.emptySet();
            }

            Set<String> keys = new HashSet<>();
            flattenKeys("", data, keys);
            return keys;
        }
    }

    private void flattenKeys(String prefix, Map<String, Object> source, Set<String> keys) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map<?, ?> map) {
                flattenKeys(key, (Map<String, Object>) map, keys);
            } else {
                keys.add(key);
            }
        }
    }

}
