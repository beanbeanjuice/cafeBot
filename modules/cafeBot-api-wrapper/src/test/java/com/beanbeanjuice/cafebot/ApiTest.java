package com.beanbeanjuice.cafebot;

import com.beanbeanjuice.cafebot.api.wrapper.CafeAPI;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ApiTest {

    protected static CafeAPI cafeAPI;
    protected static Dotenv dotenv;

    private static String getKey(String apiUrl) throws IOException, InterruptedException {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("%s/api/v4/testing", apiUrl)))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        String token = root.get("token").get("key").asString();
        System.out.println("USING: " + token);

        return token;
    }

    private static final long DISCORD_EPOCH = 1420070400000L;

    public static Long generateSnowflake() {
        long timestamp = (Instant.now().toEpochMilli() - DISCORD_EPOCH) << 22;

        // Random 5-bit worker ID and 5-bit process ID
        long workerId = ThreadLocalRandom.current().nextLong(0, 32) << 17;
        long processId = ThreadLocalRandom.current().nextLong(0, 32) << 12;

        // Random 12-bit increment
        long increment = ThreadLocalRandom.current().nextLong(0, 4096);

        return timestamp | workerId | processId | increment;
    }

    @BeforeAll
    public static synchronized void login() throws IOException, InterruptedException {
        if (dotenv == null) { // ensure initialization happens once
            // Try loading .env from ../../, fallback gracefully if not found
            try {
                dotenv = Dotenv.configure()
                        .directory("../../")
                        .ignoreIfMissing()  // don't throw if .env is missing
                        .load();
            } catch (Exception e) {
                // fallback to empty dotenv
                dotenv = Dotenv.configure().ignoreIfMissing().load();
            }

            // Read CAFEBOT_API_URL: first from .env, then system environment
            String apiUrl = dotenv.get("CAFEBOT_API_URL");
            if (apiUrl == null || apiUrl.isEmpty()) {
                apiUrl = System.getenv("CAFEBOT_API_URL");
            }

            if (apiUrl == null || apiUrl.isEmpty()) {
                throw new RuntimeException("CAFEBOT_API_URL must be set in .env or system environment!");
            }

            String token = getKey(apiUrl);
            cafeAPI = new CafeAPI(apiUrl, token);
        }
    }

}
