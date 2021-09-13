package com.beanbeanjuice.utility.helper.api;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.guild.CustomGuild;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

/**
 * A class used for version handling for the Bot.
 *
 * @author beanbeanjuice
 */
public class GitHubUpdateChecker {

    private final String GITHUB_API_URL = "https://api.github.com/repos/beanbeanjuice/cafeBot/releases";
    private String github_url;
    private String github_tag;
    private String github_name;
    private String github_body;

    /**
     * Compares the current version to the one stored in the database.
     * @param currentVersion The {@link String} of the current version.
     * @return True, if the version in the database, is the one that is current.
     */
    private Boolean compareVersions(@NotNull String currentVersion) {
        try {
            String lastVersion = CafeBot.getCafeAPI().versions().getCurrentCafeBotVersion();
            return currentVersion.startsWith(lastVersion);
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Getting Current Bot Version: " + e.getMessage(), e);
            return true;
        }
    }

    /**
     * A method used for contacting the guilds who have enabled the bot update notification.
     */
    public void contactGuilds() {

        // Comparing the Version to the Database
        if (compareVersions(CafeBot.getBotVersion())) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.INFO, "Not sending messages that there is an update as the versions match.");
            return;
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().setHeader("User-Agent", CafeBot.getBotUserAgent()).uri(URI.create(GITHUB_API_URL)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parse)
                .join();

        // Checking if the GitHub tag does not equal the current version.
        if (!github_tag.startsWith(CafeBot.getBotVersion())) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "There is a mismatch between GitHub tag and Bot Version.");
            return;
        }

        // Updating the Version in the Database
        try {
            CafeBot.getCafeAPI().versions().updateCurrentCafeBotVersion(CafeBot.getBotVersion());
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Version in Database: " + e.getMessage(), e);
            return;
        }

        MessageEmbed updateEmbed = updateEmbed();
        HashMap<String, CustomGuild> customGuilds = CafeBot.getGuildHandler().getGuilds();

        customGuilds.forEach((guildID, customGuild) -> {
            if (customGuild.getNotifyOnUpdate()) {
                Guild guild = CafeBot.getGuildHandler().getGuild(guildID);

                TextChannel mainChannel = guild.getDefaultChannel();

                if (customGuild.getUpdateChannel() != null) {
                    mainChannel = customGuild.getUpdateChannel();
                }

                Member owner = guild.getOwner();

                try {
                    mainChannel.sendMessage(owner.getAsMention() + " I've been updated!").embed(updateEmbed).queue();
                } catch (NullPointerException | InsufficientPermissionException | UnsupportedOperationException ignored) {}
            }
        });
    }

    /**
     * @return The {@link MessageEmbed updateEmbed}.
     */
    @NotNull
    private MessageEmbed updateEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(github_name, github_url);
        embedBuilder.setFooter(github_url);
        embedBuilder.setTitle("New cafeBot Update");
        embedBuilder.setDescription(github_body);
        embedBuilder.addField("Extra Information", "The bot has been updated to " + github_tag + "! " +
                "To request a feature or report bugs, please click this [link](https://github.com/beanbeanjuice/cafeBot/issues).", true);
        embedBuilder.addField("How to Disable Update Notifications", "To disable these update notifications, " +
                "the default command would be `!!notify-on-update disable`.", false);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    /**
     * Parses the {@link JsonNode responseBody}.
     * @param responseBody The {@link JsonNode responseBody} as a {@link String}.
     * @return The {@link String parsedBody}.
     */
    @Nullable
    private String parse(String responseBody) {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        try {
            JsonNode node = defaultObjectMapper.readTree(responseBody);
            github_url = node.get(0).get("html_url").textValue();
            github_tag = node.get(0).get("tag_name").textValue();
            github_name = node.get(0).get("name").textValue();
            github_body = node.get(0).get("body").textValue();
            return node.get(0).toString();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
