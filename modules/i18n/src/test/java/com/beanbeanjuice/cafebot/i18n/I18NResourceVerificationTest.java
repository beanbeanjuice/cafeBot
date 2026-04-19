package com.beanbeanjuice.cafebot.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class I18NResourceVerificationTest {

    /**
     * Returns paths of all files under {@code initialPath}, relative to the {@code i18n} root,
     * using forward slashes regardless of OS. E.g. {@code "en/command/ai.yml"}.
     */
    private Set<String> getLanguageFilePaths(String initialPath) throws URISyntaxException {
        URL resource = I18NResourceVerificationTest.class.getClassLoader().getResource(initialPath);
        if (resource == null) Assertions.fail("Resource not found: " + initialPath);

        URL i18nResource = I18NResourceVerificationTest.class.getClassLoader().getResource("i18n");
        if (i18nResource == null) Assertions.fail("Failed to find i18n resources");
        Path i18nBase = Path.of(i18nResource.toURI());

        try (Stream<Path> walk = Files.walk(Paths.get(resource.toURI()))) {
            return walk.filter(Files::isRegularFile)
                    .map(p -> i18nBase.relativize(p).toString().replace(File.separatorChar, '/'))
                    .collect(Collectors.toSet());
        } catch (IOException | URISyntaxException e) {
            Assertions.fail(e);
        }

        Assertions.fail("Unreachable");
        return null;
    }

    /** Returns the locale directory names directly under {@code i18n/}, e.g. {@code ["en", "en_GB"]}. */
    private Set<String> getLocaleDirs() throws URISyntaxException {
        URL i18nResource = I18NResourceVerificationTest.class.getClassLoader().getResource("i18n");
        if (i18nResource == null) Assertions.fail("Failed to find i18n resources");
        Path i18nBase = Path.of(i18nResource.toURI());

        try (Stream<Path> walk = Files.walk(i18nBase, 1)) {
            return walk.filter(Files::isDirectory)
                    .filter(p -> !p.equals(i18nBase))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            Assertions.fail(e);
        }

        Assertions.fail("Unreachable");
        return null;
    }

    private Map<String, Object> loadYaml(String resourcePath) throws IOException {
        try (InputStream is = I18NResourceVerificationTest.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) return Map.of();
            return new Yaml().load(is);
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> getAllKeysInMap(Map<String, Object> map) {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            keys.add(key);
            if (value instanceof Map<?,?>) keys.addAll(getAllKeysInMap((Map<String, Object>) value));
        }
        return keys;
    }

    /**
     * Recursively collects all leaf key paths in dot-notation, relative to the map root.
     * Maps are traversed; strings, lists, and other values are treated as leaves.
     */
    @SuppressWarnings("unchecked")
    private Set<String> getFlattenedLeafKeys(Map<String, Object> map, String prefix) {
        Set<String> keys = new LinkedHashSet<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String fullKey = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            if (entry.getValue() instanceof Map<?,?>) {
                keys.addAll(getFlattenedLeafKeys((Map<String, Object>) entry.getValue(), fullKey));
            } else {
                keys.add(fullKey);
            }
        }
        return keys;
    }

    @Test
    @DisplayName("Verify Lowercase Paths")
    public void verifyLowercasePaths() throws URISyntaxException {
        Set<String> allPaths = getLanguageFilePaths("i18n");
        Assertions.assertNotNull(allPaths);

        for (String path : allPaths) {
            String[] segments = path.split("/");
            // Skip index 0 (locale dir, e.g. "en_GB" has uppercase letters which is acceptable)
            for (int i = 1; i < segments.length; i++) {
                Assertions.assertEquals(segments[i].toLowerCase(), segments[i],
                        String.format("Path segment '%s' is not lowercase in: %s", segments[i], path));
            }
        }
    }

    @Test
    @DisplayName("Verify Lowercase Keys")
    public void verifyLowercaseKeys() throws URISyntaxException {
        Set<String> paths = getLanguageFilePaths("i18n");

        for (String path : paths) {
            String fullPath = "i18n/" + path;

            try {
                Map<String, Object> map = loadYaml(fullPath);
                if (map == null) continue;

                List<String> keys = getAllKeysInMap(map);
                for (String key : keys) {
                    Assertions.assertEquals(key.toLowerCase(), key,
                            String.format("Key '%s' is not lowercase in %s", key, fullPath));
                }
            } catch (IOException e) {
                Assertions.fail(e);
            }
        }
    }

    @Test
    @DisplayName("Verify Valid YAML Files")
    public void verifyValidYamlFiles() throws URISyntaxException {
        Set<String> paths = getLanguageFilePaths("i18n");

        for (String path : paths) {
            String fullPath = "i18n/" + path;

            try {
                Map<String, Object> map = loadYaml(fullPath);
                Assertions.assertNotNull(map, "YAML file parsed as null: " + fullPath);
            } catch (Exception e) {
                Assertions.fail("Failed to parse YAML '" + fullPath + "': " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Verify Foreign Files are Subset of English Files")
    public void testForeignFilesAreSubsetOfEnglishFiles() throws URISyntaxException {
        Set<String> englishFiles = getLanguageFilePaths("i18n/en");
        Assertions.assertNotNull(englishFiles);

        // Strip the leading "en/" to get bare relative paths like "command/ai.yml"
        Set<String> englishRelative = englishFiles.stream()
                .map(p -> p.replaceFirst("^en/", ""))
                .collect(Collectors.toSet());

        Set<String> localeDirs = getLocaleDirs();
        Assertions.assertNotNull(localeDirs);

        for (String locale : localeDirs) {
            if (locale.equals("en")) continue;

            Set<String> foreignFiles = getLanguageFilePaths("i18n/" + locale);
            Assertions.assertNotNull(foreignFiles);

            for (String foreignFile : foreignFiles) {
                String relative = foreignFile.replaceFirst("^" + locale + "/", "");
                Assertions.assertTrue(englishRelative.contains(relative),
                        String.format("Foreign file '%s' exists in locale '%s' but has no English counterpart", relative, locale));
            }
        }
    }

    @Test
    @DisplayName("Verify Foreign Keys are Subset of English Keys")
    public void testForeignKeysAreSubsetOfEnglishKeys() throws URISyntaxException, IOException {
        Set<String> localeDirs = getLocaleDirs();
        Assertions.assertNotNull(localeDirs);

        for (String locale : localeDirs) {
            if (locale.equals("en")) continue;

            Set<String> foreignFiles = getLanguageFilePaths("i18n/" + locale);
            Assertions.assertNotNull(foreignFiles);

            for (String foreignFile : foreignFiles) {
                String relative = foreignFile.replaceFirst("^" + locale + "/", "");
                String englishFilePath = "i18n/en/" + relative;
                String foreignFilePath = "i18n/" + foreignFile;

                Map<String, Object> englishMap = loadYaml(englishFilePath);
                Map<String, Object> foreignMap = loadYaml(foreignFilePath);

                if (englishMap == null || englishMap.isEmpty() || foreignMap == null || foreignMap.isEmpty()) continue;

                Set<String> englishKeys = getFlattenedLeafKeys(englishMap, "");
                Set<String> foreignKeys = getFlattenedLeafKeys(foreignMap, "");

                for (String foreignKey : foreignKeys) {
                    Assertions.assertTrue(englishKeys.contains(foreignKey),
                            String.format("Key '%s' in '%s/%s' does not exist in the English file 'en/%s'",
                                    foreignKey, locale, relative, relative));
                }
            }
        }
    }

}
