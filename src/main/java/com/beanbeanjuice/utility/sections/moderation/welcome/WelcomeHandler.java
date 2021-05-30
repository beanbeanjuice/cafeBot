package com.beanbeanjuice.utility.sections.moderation.welcome;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A handler used for handling welcomes.
 */
public class WelcomeHandler {

    /**
     * Gets the {@link GuildWelcome} for the specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @return The requested {@link GuildWelcome}.
     */
    @NotNull
    public GuildWelcome getGuildWelcome(@NotNull String guildID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.welcome_information WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));

            ResultSet resultSet = statement.executeQuery();
            resultSet.first();

            String description = resultSet.getString(2);
            String thumbnailURL = resultSet.getString(3);
            String imageURL = resultSet.getString(4);

            return new GuildWelcome(description, thumbnailURL, imageURL);
        } catch (SQLException e) {
            return new GuildWelcome(null, null, null);
        }
    }

    /**
     * Checks if the {@link GuildWelcome} exists for the specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @return Whether or not the {@link GuildWelcome} exists for the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @throws SQLException An {@link SQLException} is thrown if it cannot connect to the database.
     */
    @NotNull
    private Boolean welcomeExists(@NotNull String guildID) throws SQLException {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.welcome_information WHERE guild_id = (?);";

        PreparedStatement statement = connection.prepareStatement(arguments);
        statement.setLong(1, Long.parseLong(guildID));
        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    /**
     * Sets the {@link GuildWelcome} for the specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param guildWelcome The {@link GuildWelcome} to add.
     * @return Whether or not it was successfully added.
     */
    @NotNull
    public Boolean setGuildWelcome(@NotNull String guildID, @NotNull GuildWelcome guildWelcome) {
        boolean welcomeExists = false;

        try {
            welcomeExists = welcomeExists(guildID);
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Checking Welcome: " + e.getMessage());
            return false;
        }

        if (!welcomeExists) {
            Connection connection = CafeBot.getSQLServer().getConnection();
            String arguments = "INSERT INTO cafeBot.welcome_information (guild_id, description, thumbnail_url, image_url) VALUES (?,?,?,?);";

            try {
                PreparedStatement statement = connection.prepareStatement(arguments);
                statement.setLong(1, Long.parseLong(guildID));
                statement.setString(2, guildWelcome.getDescription());
                statement.setString(3, guildWelcome.getThumbnailURL());
                statement.setString(4, guildWelcome.getImageURL());

                statement.execute();
                return true;
            } catch (SQLException e) {
                CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Creating Guild Welcome: " + e.getMessage());
                return false;
            }
        } else {
            Connection connection = CafeBot.getSQLServer().getConnection();
            String arguments = "UPDATE cafeBot.welcome_information SET description = (?), thumbnail_url = (?), image_url = (?) WHERE guild_id = (?);";

            try {
                PreparedStatement statement = connection.prepareStatement(arguments);
                statement.setString(1, guildWelcome.getDescription());
                statement.setString(2, guildWelcome.getThumbnailURL());
                statement.setString(3, guildWelcome.getImageURL());
                statement.setLong(4, Long.parseLong(guildID));

                statement.execute();
                return true;
            } catch (SQLException e) {
                CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Guild Welcome: " + e.getMessage());
                return false;
            }
        }
    }

}
