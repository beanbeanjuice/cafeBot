package com.beanbeanjuice.cafebot.i18n;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

public class I18N extends ResourceBundle {

    private final Map<String, Map<String, Object>> loadedFiles = new HashMap<>();
    private final Locale locale;
    private final ClassLoader classLoader;
    private final Yaml yaml = new Yaml();

    public I18N(Locale locale, ClassLoader classLoader) {
        this.locale = locale;
        this.classLoader = classLoader;
    }

    @Override
    protected Object handleGetObject(@NotNull String key) {
        // Parse the key: "commands.help.description"
        // -> file: "commands/help", nested key: "description"

        int lastDotIndex = key.lastIndexOf('.');
        if (lastDotIndex == -1) {
            // Try to load from root level files
            Object result = loadFromFile(key, "");
            if (result != null) {
                return result;
            }
        } else {
            String filePath = key.substring(0, lastDotIndex);
            String nestedKey = key.substring(lastDotIndex + 1);

            // Try to resolve as nested path first
            Object result = loadFromFile(filePath, nestedKey);
            if (result != null) {
                return result;
            }

            // If not found, try moving more parts to the file path
            // e.g., if "commands.help" didn't work, try "commands" with "help.description"
            int previousDotIndex = filePath.lastIndexOf('.');
            while (previousDotIndex != -1) {
                filePath = filePath.substring(0, previousDotIndex);
                nestedKey = key.substring(previousDotIndex + 1);
                result = loadFromFile(filePath, nestedKey);
                if (result != null) {
                    return result;
                }
                previousDotIndex = filePath.lastIndexOf('.');
            }

            // Try root level with full key as nested path
            result = loadFromFile("", key);
            if (result != null) {
                return result;
            }
        }

        // Not found in this locale
        // If we have a parent (fallback), return null to let parent handle it
        if (parent != null) {
            return null;
        }

        // We're the root bundle (English) and key not found - return the key itself
        return key;
    }

    private Object loadFromFile(String filePath, String nestedKey) {
        // Convert dot notation to folder path
        String folderPath = filePath.replace('.', '/');

        // Build locale string: "en_GB", "en_US", or just "en"
        String localeString = locale.getLanguage();
        if (!locale.getCountry().isEmpty()) {
            localeString += "_" + locale.getCountry();
        }

        // Include locale in cache key to avoid conflicts
        String fileKey = localeString + ":" + (folderPath.isEmpty() ? "" : folderPath);

        // Check if file is already loaded
        Map<String, Object> fileData = loadedFiles.get(fileKey);

        if (fileData == null) {
            // Try to load the file: "i18n/en_GB/info.yml"
            String resourcePath = "i18n/" + localeString + "/" +
                    (folderPath.isEmpty() ? "" : folderPath + ".yml");

            fileData = loadYamlFile(resourcePath);

            if (fileData == null && !folderPath.isEmpty()) {
                // Maybe it's in a parent directory file
                // e.g., "commands.yml" contains "help" section
                int lastSlash = folderPath.lastIndexOf('/');
                if (lastSlash != -1) {
                    String parentPath = folderPath.substring(0, lastSlash);
                    String section = folderPath.substring(lastSlash + 1);
                    resourcePath = "i18n/" + localeString + "/" + parentPath + ".yml";
                    Map<String, Object> parentData = loadYamlFile(resourcePath);
                    if (parentData != null && parentData.containsKey(section)) {
                        Object sectionData = parentData.get(section);
                        if (sectionData instanceof Map) {
                            fileData = flatten((Map<String, Object>) sectionData);
                        }
                    }
                }
            }

            if (fileData != null) {
                loadedFiles.put(fileKey, fileData);
            } else {
                loadedFiles.put(fileKey, Collections.emptyMap()); // Cache miss
            }
        }

        if (fileData == null || fileData.isEmpty()) {
            return null;
        }

        // Get nested key from flattened data
        return fileData.get(nestedKey);
    }

    private Map<String, Object> loadYamlFile(String resourcePath) {
        try (InputStream stream = classLoader.getResourceAsStream(resourcePath)) {
            if (stream == null) {
                return null;
            }
            Map<String, Object> raw = yaml.load(stream);
            return flatten(raw);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @NotNull Enumeration<String> getKeys() {
        // This is complex with lazy loading - for now return empty or loaded keys
        Set<String> keys = new HashSet<>();

        for (Map.Entry<String, Map<String, Object>> entry : loadedFiles.entrySet()) {
            String prefix = entry.getKey().replace('/', '.');
            for (String key : entry.getValue().keySet()) {
                if (prefix.isEmpty()) {
                    keys.add(key);
                } else {
                    keys.add(prefix + "." + key);
                }
            }
        }

        if (parent != null) {
            parent.getKeys().asIterator().forEachRemaining(keys::add);
        }

        return Collections.enumeration(keys);
    }

    private Map<String, Object> flatten(Map<String, Object> source) {
        Map<String, Object> result = new HashMap<>();
        flatten("", source, result);
        return result;
    }

    private void flatten(String prefix, Map<String, Object> source, Map<String, Object> result) {
        for (var entry : source.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map<?, ?> map) {
                flatten(key, (Map<String, Object>) map, result);
            } else {
                result.put(key, value.toString());
            }
        }
    }

    /**
     * Gets the I18N bundle for the specified locale.
     * @param locale The {@link Locale} to get the bundle for.
     * @return The {@link I18N} bundle instance.
     */
    public static I18N getBundle(Locale locale) {
        return (I18N) ResourceBundle.getBundle("messages", locale, YamlControl.INSTANCE);
    }

    /**
     * Gets the I18N bundle for English (default).
     * @return The {@link I18N} bundle instance.
     */
    public static I18N getBundle() {
        return getBundle(Locale.ENGLISH);
    }

    /**
     * Gets the description based on the language file and path.
     * @param path The {@link String path} to search for.
     * @return The proper {@link String description} or the {@link String path} if not found.
     */
    public static String getStringFromLanguageFile(String path) {
        try {
            return getStringFromLanguageFile(Locale.ENGLISH, path);
        } catch (MissingResourceException e) {
            return path;
        }
    }

    /**
     * Gets the description based on the language file and path.
     * @param locale The {@link Locale} specifying which language file to use. If not found, defaults to {@link Locale english}.
     * @param path The {@link String path} to search for.
     * @return The proper {@link String description} or the {@link String path} if not found.
     */
    public static String getStringFromLanguageFile(Locale locale, String path) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", locale, YamlControl.INSTANCE);
            return bundle.getString(path);
        } catch (MissingResourceException e) {
            return path;
        }
    }

}
