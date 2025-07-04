package com.beanbeanjuice.cafebot.utility.api;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class RedditAPIWrapper {

    private final CafeBot cafeBot;

    private String redditAPIURL = "https://www.reddit.com/r/{SUBREDDIT}/random/.json";
    private String redditURL;
    private String redditImageURL;
    private String redditTitle;
    private String redditUsername;
    private final String redditSubreddit;
    private String redditDescription;

    public RedditAPIWrapper(final CafeBot cafeBot, final String subreddit) {
        this.cafeBot = cafeBot;
        this.redditSubreddit = subreddit;
        this.redditAPIURL = this.redditAPIURL.replace("{SUBREDDIT}", subreddit);
    }

    private CompletableFuture<MessageEmbed> getRedditEmbed() {
        redditAPIURL = redditAPIURL.replace("{SUBREDDIT}", redditSubreddit);

        // Making sure it's not a video
        if (redditImageURL.contains("v.red")
                || redditImageURL.contains("youtube.")
                || redditImageURL.contains("youtu.be")
                || redditImageURL.contains("instagram.com")) {
            return this.getRandom();
        }

        // Create the embed.
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(redditTitle, redditURL)
                .setImage(redditImageURL)
                .setFooter("By: " + redditUsername + " - r/" + redditSubreddit)
                .setColor(Helper.getRandomColor())
                .setDescription(redditDescription);
        return CompletableFuture.supplyAsync(embedBuilder::build);
    }

    public CompletableFuture<MessageEmbed> getRandom() {
        try (HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()) {
            HttpRequest request = HttpRequest.newBuilder().setHeader("User-Agent", cafeBot.getBotUserAgent()).uri(URI.create(redditAPIURL)).build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::parse)
                    .thenComposeAsync((string) -> getRedditEmbed());
        }
    }

    private String parse(String responseBody) {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        try {
            JsonNode node = defaultObjectMapper.readTree(responseBody).get(0).get("data").get("children").get(0).get("data");
            redditURL = "https://www.reddit.com" + node.get("permalink").textValue();
            redditImageURL = node.get("url").textValue();
            redditTitle = node.get("title").textValue();
            redditUsername = node.get("author").textValue();
            redditDescription = node.get("selftext").textValue();
            return node.toString();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
