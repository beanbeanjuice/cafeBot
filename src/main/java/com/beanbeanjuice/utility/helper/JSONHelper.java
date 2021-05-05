package com.beanbeanjuice.utility.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class JSONHelper {

    private static ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        // ---
        return defaultObjectMapper;
    }

    public static JsonNode parse(@NotNull String fileName) {
        try {
            return objectMapper.readTree(Paths.get(fileName).toFile());
        } catch (IOException e) {
            System.out.println("Unable to find file.");
            return null;
        }
    }

    /**
     * Gets a specific value from the JSON.
     * @param fileName The name for the JSON {@link File}.
     * @param key The key for the {@link JsonNode}.
     * @param code The code for the {@link JsonNode}.
     * @return The {@link JsonNode} value.
     */
    @Nullable
    public static JsonNode getValue(@NotNull String fileName, @NotNull String key, @NotNull String code) {
        return parse(fileName).get(key).findValue(code);
    }

}
