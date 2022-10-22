package com.beanbeanjuice.utility.api;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.handler.guild.CustomGuild;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.beanbeanjuice.cafeapi.exception.api.CafeException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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
public class GitHubUpdateHelper {

    private final String GITHUB_API_URL = "https://api.github.com/repos/beanbeanjuice/cafeBot/releases";
    private String github_url;
    private String github_tag;
    private String github_name;
    private String github_body;

    /**
     * Compares the current version to the one stored in the database.
     *
     * @return True, if the version in the database, is the one that is current.
     */
    private Boolean compareVersions() {
        try {
            String lastVersion = Bot.getCafeAPI().VERSION.getCurrentCafeBotVersion();
            return Bot.BOT_VERSION.startsWith(lastVersion);
        } catch (CafeException e) {
            Bot.getLogger().log(GitHubUpdateHelper.class, LogLevel.WARN, "Error Getting Current Bot Version: " + e.getMessage(), e);
            return true;
        }
    }

    /**
     * A method used for contacting the guilds who have enabled the bot update notification.
     */
    public void start() {

        // Comparing the Version to the Database
        if (compareVersions()) {
            Bot.getLogger().log(GitHubUpdateHelper.class, LogLevel.INFO, "Not sending messages that there is an update as the versions match.");
            return;
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().setHeader("User-Agent", Bot.BOT_USER_AGENT).uri(URI.create(GITHUB_API_URL)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parse)
                .join();

        // Checking if the GitHub tag does not equal the current version.
        if (!github_tag.startsWith(Bot.BOT_VERSION)) {
            Bot.getLogger().log(this.getClass(), LogLevel.WARN, "There is a mismatch between GitHub tag and Bot Version.");
            return;
        }

        // Updating the Version in the Database
        try {
            Bot.getCafeAPI().VERSION.updateCurrentCafeBotVersion(Bot.BOT_VERSION);
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Updating Version in Database: " + e.getMessage(), e);
            return;
        }

        MessageEmbed updateEmbed = updateEmbed();
        HashMap<String, CustomGuild> customGuilds = GuildHandler.getGuilds();

        customGuilds.forEach((guildID, customGuild) -> {
            if (customGuild.getNotifyOnUpdate()) {
                Guild guild = GuildHandler.getGuild(guildID);

                TextChannel mainChannel = customGuild.getUpdateChannel();

                // If the update channel does not exist, send to default channel.
                if (customGuild.getUpdateChannel() == null)
                    mainChannel = guild.getDefaultChannel().asTextChannel();

                Member owner = guild.getOwner();

                try {
                    mainChannel.sendMessage(owner.getAsMention() + " I've been updated!").setEmbeds(updateEmbed).queue();
                } catch (NullPointerException | InsufficientPermissionException | UnsupportedOperationException |
                        IllegalStateException ignored) {}
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
                "the default command would be `/bot-update notify False`.", false);
        embedBuilder.setColor(Helper.getRandomColor());
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
