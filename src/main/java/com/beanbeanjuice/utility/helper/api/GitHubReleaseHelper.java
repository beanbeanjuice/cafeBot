package com.beanbeanjuice.utility.helper.api;

import com.beanbeanjuice.CafeBot;
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
import java.util.concurrent.CompletionException;

/**
 * A helper used to get specific releases from GitHub.
 *
 * @author beanbeanjuice
 */
public class GitHubReleaseHelper {

    private final String GITHUB_API_URL = "https://api.github.com/repos/beanbeanjuice/cafeBot/releases/tags/";
    private String github_url;
    private String github_tag;
    private String github_name;
    private String github_body;

    /**
     * A method used to get a {@link MessageEmbed} containing all release information.
     * @param versionTag The specific version number for the GitHub release.
     * @return The completed {@link MessageEmbed} to be sent.
     * @throws CompletionException Thrown when no release exists for the specific version tag.
     */
    public MessageEmbed getVersion(@NotNull String versionTag) throws CompletionException {
        github_tag = versionTag;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().setHeader("User-Agent", CafeBot.getBotUserAgent()).uri(URI.create(GITHUB_API_URL + github_tag)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parse)
                .join();

        return updateEmbed();
    }

    private String parse(String responseBody) throws CompletionException {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        try {
            JsonNode node = defaultObjectMapper.readTree(responseBody);
            github_url = node.get("html_url").textValue();
            github_name = node.get("name").textValue();
            github_body = node.get("body").textValue();
            return node.toString();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private MessageEmbed updateEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(github_name, github_url);
        embedBuilder.setFooter(github_url);
        embedBuilder.setTitle("cafeBot Update");
        embedBuilder.setDescription(github_body);
        embedBuilder.addField("Extra Information", "Getting bot update " + github_tag + "! " +
                "To request a feature or report bugs, please click this [link](https://github.com/beanbeanjuice/cafeBot/issues).", true);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

}
