package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.guild.CustomGuild;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.HashMap;

/**
 * A class used for version handling for the Bot.
 *
 * @author beanbeanjuice
 */
public class VersionHelper {

    private final String GITHUB_API_URL = "https://api.github.com/repos/beanbeanjuice/cafeBot/releases";
    private String github_url;
    private String github_tag;
    private String github_name;
    private String github_body;

    /**
     * Updates the Bot Version Number in the Database
     * @param currentVersion The {@link String} of the current version.
     * @return Whether or not updating was successful.
     */
    public Boolean updateVersionInDatabase(@NotNull String currentVersion) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.bot_information SET version = (?) WHERE id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setString(1, currentVersion);
            statement.setInt(2, 1);

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Update Version in Database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Compares the current version to the one stored in the database.
     * @param currentVersion The {@link String} of the current version.
     * @return Whether or not the current version is the one stored in the database.
     */
    public Boolean compareVersions(@NotNull String currentVersion) {
        String lastVersion = getLastVersion();
        if (lastVersion == null) {
            return true;
        }
        return currentVersion.startsWith(lastVersion);
    }

    /**
     * @return The last version number that is in the SQL database.
     */
    public String getLastVersion() {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.bot_information WHERE id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setInt(1, 1);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString(2);
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Get Previous Version From Database: " + e.getMessage());
            return null;
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
        if (!updateVersionInDatabase(CafeBot.getBotVersion())) {
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
