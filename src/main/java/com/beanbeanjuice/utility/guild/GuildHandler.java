package com.beanbeanjuice.utility.guild;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class used for handling {@link Guild Guilds}.
 *
 * @author beanbeanjuice
 */
public class GuildHandler {
    private HashMap<String, CustomGuild> guildDatabase;

    /**
     * Creates a new {@link GuildHandler} object.
     */
    public GuildHandler() {
        guildDatabase = new HashMap<>();

        checkGuilds();
    }

    /**
     * Updates the current {@link Guild} cache.
     */
    public void updateGuildCache() {
        guildDatabase.clear();

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM beanbot.guild_information;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(arguments);

            while (resultSet.next()) {
                String guildID = String.valueOf(resultSet.getLong(1));
                String prefix = resultSet.getString(2);
                String moderatorRoleID = String.valueOf(resultSet.getLong(3));

                guildDatabase.put(guildID, new CustomGuild(guildID, prefix, moderatorRoleID));
            }
        } catch (SQLException e) {
            BeanBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Unable to reach the SQL database: " + e.getMessage());
        }
    }

    /**
     * Updates the prefix for a specific {@link Guild}.
     * @param guildID The ID of the {@link Guild}.
     * @param newPrefix The new prefix for the {@link Guild}.
     * @return Whether or not the prefix was updated successfully.
     */
    @NotNull
    public Boolean updateGuildPrefix(@NotNull String guildID, @NotNull String newPrefix) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.guild_information SET prefix = (?) WHERE guild_id = " + guildID + ";";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setString(1, newPrefix);

            statement.execute();
            updateGuildCache();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(CustomGuild.class, LogLevel.ERROR, "Unable to reach the SQL database.");
            return false;
        }

    }

    /**
     * Updates the prefix for a specific {@link Guild}.
     * @param guild The {@link Guild}.
     * @param newPrefix The new prefix for the {@link Guild}.
     * @return Whether or not the prefix was updated successfully.
     */
    @NotNull
    public Boolean updateGuildPrefix(@NotNull Guild guild, @NotNull String newPrefix) {
        return updateGuildPrefix(guild.getId(), newPrefix);
    }

    /**
     * Checks all the current {@link Guild} in the database.
     */
    public void checkGuilds() {

        updateGuildCache();

        List<Guild> guildsHasBot = BeanBot.getJDA().getGuilds();
        ArrayList<String> guildsIDHasBot = new ArrayList<>();

        // Adds any guild that the bot is in but not in the database.
        for (Guild guild : guildsHasBot) {
            if (!guildDatabase.containsKey(guild.getId())) {
                addGuild(guild);
            }

            guildsIDHasBot.add(guild.getId());
        }

        // Checks the database for any guilds that the bot is no longer in.
        guildDatabase.forEach((k, v) -> {
            if (!guildsIDHasBot.contains(k)) {
                removeGuild(k);
            }
        });

        updateGuildCache();

    }

    /**
     * Removes a {@link Guild} from the database.
     * @param guildID The ID of the {@link Guild}.
     * @return Whether or not the {@link Guild} was removed successfully.
     */
    @NotNull
    public Boolean removeGuild(@NotNull String guildID) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "DELETE FROM beanbot.guild_information " +
                "WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setString(1, guildID);

            statement.execute();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Unable to reach the SQL database.");
            return false;
        }
    }

    /**
     * Removes a {@link Guild} from the database.
     * @param guild The ID of the {@link Guild} to be removed.
     * @return Whether or not the {@link Guild} was removed successfully.
     */
    @NotNull
    public Boolean removeGuild(@NotNull Guild guild) {
        return removeGuild(guild.getId());
    }

    /**
     * Adds a {@link Guild} to the database.
     * @param guildID The ID of the {@link Guild} to be added.
     * @return Whether or not the {@link Guild} was added successfully.
     */
    @NotNull
    public Boolean addGuild(@NotNull String guildID) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "INSERT INTO beanbot.guild_information " +
                "(guild_id, prefix, moderator_role_id) " +
                "VALUES (?,?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            statement.setString(2, BeanBot.getPrefix());
            statement.setLong(3, 0L);

            statement.execute();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Unable to reach the SQL database.");
            return false;
        }
    }

    /**
     * Updates the moderator {@link Role} for the {@link Guild}.
     * @param guildID The ID of the {@link Guild} to have the {@link Role} updated.
     * @param roleID The ID of the {@link Role} to set as the moderator {@link Role}.
     * @return Whether or not updating the moderator {@link Role} was successful.
     */
    @NotNull
    public Boolean updateGuildModeratorRole(@NotNull String guildID, @NotNull String roleID) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.guild_information " +
                "SET moderator_role_id = (?) " +
                "WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(roleID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            updateGuildCache();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Unable to reach the SQL database.");
            return false;
        }

    }

    /**
     * Updates the moderator {@link Role} for the {@link Guild}.
     * @param guild The {@link Guild} to have the {@link Role} updated.
     * @param role The {@link Role} to set as the moderator {@link Role}.
     * @return Whether or not updating the moderator {@link Role} was successful.
     */
    @NotNull
    public Boolean updateGuildModeratorRole(@NotNull Guild guild, @NotNull Role role) {
        return updateGuildModeratorRole(guild.getId(), role.getId());
    }

    /**
     * Adds a {@link Guild} to the database.
     * @param guild The {@link Guild} to be added.
     * @return Whether or not the {@link Guild} was added successfully.
     */
    @NotNull
    public Boolean addGuild(@NotNull Guild guild) {
        return addGuild(guild.getId());
    }

    /**
     * Gets a {@link CustomGuild} from its ID.
     * @param guildID The ID of the {@link CustomGuild}.
     * @return The {@link CustomGuild}.
     */
    @NotNull
    public CustomGuild getCustomGuild(@NotNull String guildID) {
        return guildDatabase.get(guildID);
    }

    /**
     * Gets a {@link CustomGuild}.
     * @param guild The {@link Guild} of the {@link CustomGuild}.
     * @return The {@link CustomGuild}.
     */
    @NotNull
    public CustomGuild getCustomGuild(@NotNull Guild guild) {
        return getCustomGuild(guild.getId());
    }

    /**
     * Gets a {@link Guild} by its ID.
     * @param guildID The ID of the {@link Guild}.
     * @return The {@link Guild}.
     */
    @NotNull
    public Guild getGuild(@NotNull String guildID) {
        return BeanBot.getJDA().getGuildById(guildID);
    }

    /**
     * Gets the {@link GuildHandler} database cache.
     * @return The {@link HashMap} containing the database cache.
     */
    @NotNull
    public HashMap<String, CustomGuild> getGuilds() {
        return guildDatabase;
    }

}
