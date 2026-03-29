package com.beanbeanjuice.cafebot.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class I18NResourceVerificationTest {

    private Set<String> getLanguageFilePaths(String initialPath) throws URISyntaxException {
        URL resource = I18NResourceVerificationTest.class.getClassLoader().getResource(initialPath);
        if (resource == null) Assertions.fail("Resource not found: " + initialPath);

        URL i18nResources =  I18NResourceVerificationTest.class.getClassLoader().getResource("i18n");
        if (i18nResources == null) Assertions.fail("Failed to find i18n resources");
        Path relativePath = Path.of(i18nResources.toURI());

        try (Stream<Path> walk = Files.walk(Paths.get(resource.toURI()))) {
            return walk.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .map((string) -> string.replace(relativePath.toString(), ""))
                    .map((string) -> string.replaceFirst("\\\\", ""))
                    .collect(Collectors.toSet());
        } catch (IOException | URISyntaxException e) {
            Assertions.fail(e);
        }

        Assertions.fail("Resource not found: " + initialPath);
        return null;
    }

    @Test
    @DisplayName("Verify Lowercase Paths")
    public void verifyLowercasePaths() throws URISyntaxException {
        // Verify that all file paths in all languages are lowercase.
        Set<String> allPaths = getLanguageFilePaths("i18n");
        Assertions.assertNotNull(allPaths);

        for (String path : allPaths) {
            String[] splitPathOrig = path.split("\\\\");
            String[] splitPath = Arrays.copyOfRange(splitPathOrig, 1, splitPathOrig.length);

            for (String split : splitPath) {
                Assertions.assertEquals(split.toLowerCase(), split);
            }
        }
    }

    private List<String> getAllKeysInMap(Map<String, Object> map) {
        // If value at key is a map, go deeper.
        List<String> keys = new ArrayList<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            keys.add(key);
            if (value instanceof Map<?,?>) keys.addAll(getAllKeysInMap((Map<String, Object>) value));
        }

        return keys;
    }

    @Test
    @DisplayName("Verify Lowercase Keys")
    public void verifyLowercaseKeys() throws URISyntaxException {
        // Verify that all key paths in all languages are lowercase.
        Set<String> paths = getLanguageFilePaths("i18n"); // en/info.yml

        for (String path : paths) {
            String fullPath = "i18n\\" + path;

            URL resource = I18NResourceVerificationTest.class.getClassLoader().getResource(fullPath);
            if (resource == null) Assertions.fail("Resource not found: " + fullPath);

            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(fullPath)) {
                Yaml yaml = new Yaml();
                Map<String, Object> map = yaml.load(is);

                List<String> keys = getAllKeysInMap(map);

                for (String key : keys) Assertions.assertEquals(key, key.toLowerCase(), String.format("key '%s' is incorrect in %s", key, fullPath));
            } catch (IOException e) {
                Assertions.fail(e);
            }
        }
    }

    @Test
    @DisplayName("Verify Valid YAML Files")
    public void verifyValidYamlFiles() throws URISyntaxException {
        Set<String> paths = getLanguageFilePaths("i18n"); // en/info.yml

        for (String path : paths) {
            String fullPath = "i18n\\" + path; // i18n/en/info.yml

            URL resource = I18NResourceVerificationTest.class.getClassLoader().getResource(fullPath);
            if (resource == null) Assertions.fail("Resource not found: " + fullPath);

            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(fullPath)) {

                try {
                    Yaml yaml = new Yaml();
                    Map<String, Object> map = yaml.load(is);
                } catch (Exception e) {
                    Assertions.fail(e);
                }
            } catch (IOException e) {
                Assertions.fail(e);
            }
        }
    }

    @Test
    @DisplayName("Verify Foreign Files are Subset of English Files")
    @Disabled
    public void testForeignFilesAreSubsetOfEnglishFiles() {
        // Get a list of all file paths in the i18n/en directory.
        Assertions.fail("Not yet implemented");
    }

    @Test
    @DisplayName("Verify Foreign Keys are Subset of English Keys")
    @Disabled
    public void testForeignKeysAreSubsetOfEnglishKeys() {
        Assertions.fail("Not yet implemented");
    }

}
