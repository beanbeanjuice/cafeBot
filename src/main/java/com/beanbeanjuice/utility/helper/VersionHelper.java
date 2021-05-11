package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.guild.CustomGuild;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashMap;

/**
 * A class used for version handling for the Bot.
 *
 * @author beanbeanjuice
 */
public class VersionHelper {

    /**
     * Updates the Bot Version Number in the Database
     * @param currentVersion The {@link String} of the current version.
     * @return Whether or not updating was successful.
     */
    public Boolean updateVersionInDatabase(@NotNull String currentVersion) {
        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.bot_information SET version = (?) WHERE id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setString(1, currentVersion);
            statement.setInt(2, 1);

            statement.execute();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Update Version in Database: " + e.getMessage());
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
        return currentVersion.equals(lastVersion);
    }

    /**
     * @return The last version number that is in the SQL database.
     */
    public String getLastVersion() {
        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM beanbot.bot_information WHERE id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setInt(1, 1);
            ResultSet resultSet = statement.executeQuery(arguments);

            return resultSet.getString(2);
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Get Previous Version From Database: " + e.getMessage());
            return null;
        }
    }

    /**
     * A method used for contacting the guilds who have enabled the bot update notification.
     */
    public void contactGuilds() {
        HashMap<String, CustomGuild> customGuilds = BeanBot.getGuildHandler().getGuilds();

        customGuilds.forEach((guildID, customGuild) -> {
            if (customGuild.getNotifyOnUpdate()) {
                Guild guild = BeanBot.getGuildHandler().getGuild(guildID);
                TextChannel mainChannel = guild.getDefaultChannel();
                Member owner = guild.getOwner();

                // TODO: Notify Them
                mainChannel.sendMessage(owner.getAsMention() + " there is a new update!").queue();
            }
        });
    }

}
