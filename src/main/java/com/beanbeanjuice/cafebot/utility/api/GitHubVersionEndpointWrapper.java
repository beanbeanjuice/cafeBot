package com.beanbeanjuice.cafebot.utility.api;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class GitHubVersionEndpointWrapper {

    private final String GITHUB_API_URL = "https://api.github.com/repos/beanbeanjuice/cafeBot/releases/tags/";
    private String github_url;
    private String github_tag;
    private String github_name;
    private String github_body;
    private final CafeBot cafeBot;

    public GitHubVersionEndpointWrapper(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
    }

    public CompletableFuture<MessageEmbed> getVersion(final String versionTag) {
        github_tag = (versionTag.startsWith("v")) ? versionTag : "v" + versionTag;

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder().setHeader("User-Agent", cafeBot.getBotUserAgent()).uri(URI.create(GITHUB_API_URL + github_tag)).build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::parse)
                    .thenApply(this::updateEmbed);
        }
    }

    private String parse(String responseBody) {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        try {
            JsonNode node = defaultObjectMapper.readTree(responseBody);
            github_url = node.get("html_url").textValue();
            github_name = node.get("name").textValue();
            github_body = node.get("body").textValue();

            return node.toString();
        } catch (JsonProcessingException e) {
            throw new CompletionException(e);
        }
    }

    private MessageEmbed updateEmbed(String responseBody) {
        try {
            ObjectMapper defaultObjectMapper = new ObjectMapper();
            JsonNode node = defaultObjectMapper.readTree(responseBody);
            github_url = node.get("html_url").textValue();
            github_name = node.get("name").textValue();
            github_body = node.get("body").textValue();

            return getEmbed().build();
        } catch (JsonProcessingException e) {
            throw new CompletionException(e);
        }


    }

    private @NotNull EmbedBuilder getEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(github_name, github_url);
        embedBuilder.setFooter(github_url);
        embedBuilder.setTitle("cafeBot Update");
        embedBuilder.setDescription(github_body);
        embedBuilder.addField("Extra Information", "Getting bot update " + github_tag + "! " +
                "To request a feature or report bugs, please click this [link](https://github.com/beanbeanjuice/cafeBot/issues).", true);
        embedBuilder.setColor(Helper.getRandomColor());
        return embedBuilder;
    }

}
