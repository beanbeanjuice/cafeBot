package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.main.CafeBot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RedditAPI {

    private String reddit_api_url = "https://www.reddit.com/r/{SUBREDDIT}/random/.json";
    private String reddit_url;
    private String reddit_image_url;
    private String reddit_title;
    private String reddit_username;
    private String reddit_subreddit;
    private String reddit_description;

    /**
     * Get the completed {@link MessageEmbed} for the subreddit.
     * @param subreddit The subreddit specified.
     * @return The new {@link MessageEmbed} to be sent.
     */
    @NotNull
    public MessageEmbed getRedditEmbed(@NotNull String subreddit) {
        reddit_subreddit = subreddit;
        reddit_api_url = reddit_api_url.replace("{SUBREDDIT}", reddit_subreddit);
        contactRedditAPI();

        // Making sure it's not a video
        while (reddit_image_url.contains("v.red") || reddit_image_url.contains("youtube.") || reddit_image_url.contains("youtu-be")) {
            contactRedditAPI();
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(reddit_title, reddit_url);
        embedBuilder.setImage(reddit_image_url);
        embedBuilder.setFooter("By: " + reddit_username + " - r/" + reddit_subreddit);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription(reddit_description);
        return embedBuilder.build();
    }

    /**
     * A method used for contacting the guilds who have enabled the bot update notification.
     */
    private void contactRedditAPI() {
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(reddit_api_url)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parse)
                .join();
    }

    private String parse(String responseBody) {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        try {
            JsonNode node = defaultObjectMapper.readTree(responseBody).get(0).get("data").get("children").get(0).get("data");
            reddit_url = "https://www.reddit.com" + node.get("permalink").textValue();
            reddit_image_url = node.get("url").textValue();
            reddit_title = node.get("title").textValue();
            reddit_username = node.get("author").textValue();
            reddit_description = node.get("selftext").textValue();
            return node.toString();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
