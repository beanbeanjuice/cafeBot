package com.beanbeanjuice.cafebot.i18n;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class I18N {

    private final Locale locale;

    private static final Map<String, Map<String, Object>> YAML_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Optional<String>> FILE_PATH_CACHE = new ConcurrentHashMap<>();

    public I18N() {
        this.locale = Locale.ENGLISH;
    }

    public I18N(Locale locale) {
        this.locale = locale;
    }

    public String getString(String key) {
        return getString(key, this.locale);
    }

    public List<String> getStringArray(String key) {
        return getStringArray(key, this.locale);
    }

    private static String getString(String key, Locale locale) {
        // There are 2 paths we need to find. First, the path to the file. Next, the path within the file.
        // For example, i18n/en/command/ai.yml, and command.ai.description
        // Then we know command.ai is the file path, and description is the path within the file.
        // Question is... how do we do this? There might be a dynamic number of folders.
        Optional<String> filePath = findFilePath(key, locale)
                .or(() -> findFilePath(key, Locale.ENGLISH));

        if (filePath.isEmpty()) return key;

        /*
        Now we need to remove the overlap from the key and the filePath.
        For example, the filePath might be i18n/en/command/ai.yml and
        the key might be command.ai.description

        This means we need to remove "command.ai."
         */

        String pathInFile = parseKey(filePath.get(), key);
        Map<String, Object> map = loadYaml(filePath.get());

        if (map.isEmpty()) return key;

        Optional<String> flattenedString = getFlattenedString(map, pathInFile);

        // If the string wasn't found, and the current locale is English, then it truly does not exist.
        if (flattenedString.isEmpty() && locale.equals(Locale.ENGLISH)) return key;

        return flattenedString.orElseGet(() -> getString(key, Locale.ENGLISH));
    }

    private static List<String> getStringArray(String key, Locale locale) {
        Optional<String> filePath = findFilePath(key, locale)
                .or(() -> findFilePath(key, Locale.ENGLISH));

        if (filePath.isEmpty()) return new ArrayList<>();

        /*
        Now we need to remove the overlap from the key and the filePath.
        For example, the filePath might be i18n/en/command/ai.yml and
        the key might be command.ai.description

        This means we need to remove "command.ai."
         */

        String pathInFile = parseKey(filePath.get(), key);
        Map<String, Object> map = loadYaml(filePath.get());
        if (map.isEmpty()) return new ArrayList<>();

        List<String> flattenedStringArray = getFlattenedStringArray(map, pathInFile);

        // If the string wasn't found, and the current locale is English, then it truly does not exist.
        if (flattenedStringArray.isEmpty() && locale.equals(Locale.ENGLISH)) return flattenedStringArray;

        if (!flattenedStringArray.isEmpty()) return flattenedStringArray;
        return getStringArray(key, Locale.ENGLISH);
    }

    @SuppressWarnings("unchecked")
    private static Optional<String> getFlattenedString(Map<String, Object> map, String key) {
        String[] split = key.split("\\.");

        for (int i = 0; i < split.length - 1; i++) {
            if (!map.containsKey(split[i])) return Optional.empty();

            map = (Map<String, Object>) map.get(split[i]);
        }

        String finalKey = split[split.length - 1];
        return (map.containsKey(finalKey)) ? ((String) map.get(finalKey)).describeConstable() : Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private static List<String> getFlattenedStringArray(Map<String, Object> map, String key) {
        String[] split = key.split("\\.");

        for (int i = 0; i < split.length - 1; i++) {
            if (!map.containsKey(split[i])) return new ArrayList<>();

            map = (Map<String, Object>) map.get(split[i]);
        }

        String finalKey = split[split.length - 1];
        return (map.containsKey(finalKey)) ? ((List<String>) map.get(finalKey)) : new ArrayList<>();
    }

    private static String parseKey(String filePath, String key) {
        String[] splitFilePath = filePath              // i18n/en/command/ai.yml
                .replace(".yml", "") // i18n/en/command/ai
                .split("/");                    // ["i18n", "en", "command", "ai"]

        String[] splitKey = key.split("\\.");   // ["ai", "embed", "title"]

        // Two pointer approach
        int fileIndex = 0;
        int keyIndex = 0;

        while (fileIndex < splitFilePath.length && keyIndex < splitKey.length) {
            if (!splitFilePath[fileIndex].equals(splitKey[keyIndex])) {
                fileIndex++;
                continue;
            }

            fileIndex++;
            keyIndex++;
        }

        // Key starts at keyIndex
        return String.join(".", Arrays.copyOfRange(splitKey, keyIndex, splitKey.length));
    }

    private static Optional<String> findFilePathUncached(String key, Locale locale) {
        StringBuilder sb = new StringBuilder();
        sb.append("i18n/").append(locale.toString()).append("/"); // i18n/en/

        String[] splitKey = key.split("\\.");

        for (String split : splitKey) {
            sb.append(split); // i18n/en/command

            URL resource = I18N.class.getClassLoader().getResource(sb.toString());
            if (resource == null) {
                // Directory with that name not found. Does a .yml exist in current directory?
                resource = I18N.class.getClassLoader().getResource(sb + ".yml");
                if (resource == null && locale.equals(Locale.ENGLISH)) return Optional.empty(); // if english, and not found, it doesn't exist.
                if (resource == null) return findFilePath(key, Locale.ENGLISH); // default to english.
                return Optional.of(sb.append(".yml").toString());
            }

            sb.append("/");
        }

        return Optional.empty();
    }

    private static Optional<String> findFilePath(String key, Locale locale) {
        String cacheKey = locale + ":" + key;
        return FILE_PATH_CACHE.computeIfAbsent(cacheKey, k -> findFilePathUncached(key, locale));
    }

    private static Map<String, Object> loadYaml(String filePath) {
        return YAML_CACHE.computeIfAbsent(filePath, path -> {
            try (InputStream is = I18N.class.getClassLoader().getResourceAsStream(path)) {
                return new Yaml().load(is);
            } catch (IOException e) { return Map.of(); }
        });
    }

}
