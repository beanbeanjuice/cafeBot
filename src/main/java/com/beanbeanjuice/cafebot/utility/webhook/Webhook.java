package com.beanbeanjuice.cafebot.utility.webhook;

import lombok.Getter;
import lombok.Setter;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * A class used for sending {@link Webhook Webhooks}.
 *
 * @author beanbeanjuice
 */
public class Webhook {

    private final String url;
    @Setter private String content;
    @Setter private String username;
    @Setter private String avatarUrl;
    @Setter private boolean tts;
    private final List<EmbedObject> embeds = new ArrayList<>();

    /**
     * Constructs a new DiscordWebhook instance
     *
     * @param url The webhook URL obtained in Discord
     */
    public Webhook(final String url) {
        this.url = url;
    }

    public void execute() throws IOException {
        if (this.content == null && this.embeds.isEmpty())
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");

        JSONObject json = new JSONObject();

        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);

        List<JSONObject> embedObjects = new ArrayList<>();
        this.embeds.forEach((embed) -> {
            JSONObject jsonEmbed = new JSONObject();

            jsonEmbed.put("title", embed.getTitle());
            jsonEmbed.put("description", embed.getDescription());
            jsonEmbed.put("url", embed.getUrl());

            embed.getColor().ifPresent((color) -> {
                int rgb = color.getRed();
                rgb = (rgb << 8) + color.getGreen();
                rgb = (rgb << 8) + color.getBlue();

                jsonEmbed.put("color", rgb);
            });

            List<EmbedObject.Field> fields = embed.getFields();

            embed.getFooter().ifPresent((footer) -> {
                JSONObject jsonFooter = new JSONObject();

                jsonFooter.put("text", footer.text());
                jsonFooter.put("icon_url", footer.iconUrl());
                jsonEmbed.put("footer", jsonFooter);
            });

            embed.getImage().ifPresent((image) -> {
                JSONObject jsonImage = new JSONObject();

                jsonImage.put("url", image.url());
                jsonEmbed.put("image", jsonImage);
            });

            embed.getThumbnail().ifPresent((thumbnail) -> {
                JSONObject jsonThumbnail = new JSONObject();

                jsonThumbnail.put("url", thumbnail.url());
                jsonEmbed.put("thumbnail", jsonThumbnail);
            });

            embed.getAuthor().ifPresent((author) -> {
                JSONObject jsonAuthor = new JSONObject();

                jsonAuthor.put("name", author.name());
                jsonAuthor.put("url", author.url());
                jsonAuthor.put("icon_url", author.iconUrl());
                jsonEmbed.put("author", jsonAuthor);
            });

            Stream<JSONObject> jsonFields = fields.stream().map((field) -> {
                JSONObject jsonField = new JSONObject();

                jsonField.put("name", field.name());
                jsonField.put("value", field.value());
                jsonField.put("inline", field.inline());

                return jsonField;
            });

            jsonEmbed.put("fields", jsonFields.toArray());
            embedObjects.add(jsonEmbed);
        });
        if (!embedObjects.isEmpty()) json.put("embeds", embedObjects.toArray());

        URL url = URI.create(this.url).toURL();
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();
        stream.write(json.toString().getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }

    @Setter
    private static class EmbedObject {
        @Getter private String title;
        @Getter private String description;
        @Getter private String url;
        @Getter private List<Field> fields = new ArrayList<>();

        private Color color;
        private Footer footer;
        private Thumbnail thumbnail;
        private Image image;
        private Author author;

        public Optional<Color> getColor() {
            return Optional.ofNullable(this.color);
        }

        public Optional<Footer> getFooter() {
            return Optional.ofNullable(this.footer);
        }

        public Optional<Thumbnail> getThumbnail() {
            return Optional.ofNullable(this.thumbnail);
        }

        public Optional<Image> getImage() {
            return Optional.ofNullable(this.image);
        }

        public Optional<Author> getAuthor() {
            return Optional.ofNullable(this.author);
        }

        private record Footer(String text, String iconUrl) { }
        private record Thumbnail(String url) { }
        private record Image(String url) { }
        private record Author(String name, String url, String iconUrl) { }
        private record Field(String name, String value, boolean inline) { }
    }

    private static class JSONObject {

        private final HashMap<String, Object> map = new HashMap<>();

        void put(String key, Object value) {
            if (value != null) map.put(key, value);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            builder.append("{");

            int i = 0;
            for (Map.Entry<String, Object> entry : entrySet) {
                Object val = entry.getValue();
                builder.append(quote(entry.getKey())).append(":");

                if (val instanceof String) {
                    builder.append(quote(String.valueOf(val)));
                } else if (val instanceof Integer) {
                    builder.append(Integer.valueOf(String.valueOf(val)));
                } else if (val instanceof Boolean) {
                    builder.append(val);
                } else if (val instanceof JSONObject) {
                    builder.append(val);
                } else if (val.getClass().isArray()) {
                    builder.append("[");
                    int len = Array.getLength(val);
                    for (int j = 0; j < len; j++) {
                        builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                    }
                    builder.append("]");
                }

                builder.append(++i == entrySet.size() ? "}" : ",");
            }

            return builder.toString();
        }

        private String quote(String string) {
            return "\"" + string + "\"";
        }
    }

}
