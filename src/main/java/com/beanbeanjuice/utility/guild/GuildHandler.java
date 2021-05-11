package com.beanbeanjuice.utility.guild;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
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
                String twitchChannelID = String.valueOf(resultSet.getLong(4));
                String mutedRoleID = String.valueOf(resultSet.getLong(5));
                ArrayList<String> twitchChannels = getTwitchChannels(guildID);
                String liveNotificationsRoleID = String.valueOf(resultSet.getLong(6));
                Boolean notifyOnUpdate = resultSet.getBoolean(7);
                String updateChannelID = String.valueOf(resultSet.getLong(8));

                guildDatabase.put(guildID, new CustomGuild(guildID, prefix, moderatorRoleID,
                        twitchChannelID, twitchChannels, mutedRoleID,
                        liveNotificationsRoleID, notifyOnUpdate, updateChannelID));
            }
        } catch (SQLException e) {
            BeanBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Unable to update Guild Cache: " + e.getMessage());
        }
    }

    /**
     * Gets the {@link ArrayList<String>} of Twitch Channels for the {@link Guild}.
     * @param guildID The ID of the {@link Guild}.
     * @return the {@link ArrayList<String>} of Twitch Channel Names
     */
    public ArrayList<String> getTwitchChannels(String guildID) {
        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM beanbot.guild_twitch WHERE guild_id = ?;";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            ResultSet resultSet = statement.executeQuery();

            ArrayList<String> twitchNames = new ArrayList<>();

            while (resultSet.next()) {
                twitchNames.add(resultSet.getString(2));
            }

            return twitchNames;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to retrieve twitch channels from database.", true, false);
            return new ArrayList<>();
        }
    }

    /**
     * Sets the {@link Boolean} for if the {@link Guild} should be notified on an update.
     * @param guildID The ID of the {@link Guild} provided.
     * @param answer The {@link Boolean} provided.
     * @return Whether or not updating it was successful.
     */
    protected Boolean setNotifyOnUpdate(@NotNull String guildID, @NotNull Boolean answer) {
        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.guild_information SET notify_on_update = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setBoolean(1, answer);
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Unable to Update the Notify On Update Parameter: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sets the {@link Boolean} for if the {@link Guild} should be notified on an update.
     * @param guild The {@link Guild} provided.
     * @param answer The {@link Boolean} provided.
     * @return Whether or not updating it was successful.
     */
    protected Boolean setNotifyOnUpdate(@NotNull Guild guild, @NotNull Boolean answer) {
        return setNotifyOnUpdate(guild.getId(), answer);
    }

    /**
     * Sets the Live Notifications {@link Role} for the {@link Guild}.
     * @param guildID The ID of the {@link Guild}.
     * @param roleID The ID of the Live Notifications {@link Role}.
     * @return Whether or not it was successfully updated in the database.
     */
    protected Boolean setLiveNotificationsRoleID(@NotNull String guildID, @NotNull String roleID) {
        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.guild_information SET live_notifications_role_id = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(roleID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Unable to Update the Live Notifications Role ID: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sets the Live Notifications {@link Role} for the {@link Guild}.
     * @param guild The {@link Guild}.
     * @param roleID The ID of the Live Notifications {@link Role}.
     * @return Whether or not it was successfully updated in the database.
     */
    protected Boolean setLiveNotificationsRoleID(@NotNull Guild guild, @NotNull String roleID) {
        return setLiveNotificationsRoleID(guild.getId(), roleID);
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
                "(guild_id, prefix) " +
                "VALUES (?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            statement.setString(2, BeanBot.getPrefix());

            statement.execute();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Unable to add Guild to SQL database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the muted {@link Role} in the specified {@link Guild}.
     * @param guildID The ID of the {@link Guild} to have the {@link Role} updated in.
     * @param roleID The ID of the {@link Role} to be given to the muted {@link net.dv8tion.jda.api.entities.Member Member}.
     * @return Whether or not updating the {@link Role} in the database was successful.
     */
    @NotNull
    public Boolean updateGuildMutedRole(@NotNull String guildID, @NotNull String roleID) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.guild_information " +
                "SET muted_role_id = (?) " +
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
     * Updates the muted {@link Role} in the specified {@link Guild}.
     * @param guild The {@link Guild} to have the {@link Role} updated in.
     * @param role The {@link Role} to be given to muted {@link net.dv8tion.jda.api.entities.Member Member}.
     * @return Whether or not updating the {@link Role} in the database was successful.
     */
    @NotNull
    public Boolean updateGuildMutedRole(@NotNull Guild guild, @NotNull Role role) {
        return updateGuildMutedRole(guild.getId(), role.getId());
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
     * Adds a twitch channel for a specified {@link Guild}.
     * @param guildID The ID of the specified {@link Guild}.
     * @param twitchChannel Thw twitch channel to be added.
     * @return Whether or not adding the twitch channel was successful.
     */
    @NotNull
    public Boolean addTwitchChannel(@NotNull String guildID, @NotNull String twitchChannel) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "INSERT INTO beanbot.guild_twitch " +
                "(guild_id, twitch_channel) " +
                "VALUES (?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            statement.setString(2, twitchChannel);

            statement.execute();
            updateGuildCache();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Unable to reach the SQL database.");
            return false;
        }

    }

    /**
     * Adds a twitch channel for a specified {@link Guild}.
     * @param guild The specified {@link Guild}.
     * @param twitchChannel The twitch channel to be added.
     * @return Whether or not adding the twitch channel was successful.
     */
    @NotNull
    public Boolean addTwitchChannel(@NotNull Guild guild, @NotNull String twitchChannel) {
        return addTwitchChannel(guild.getId(), twitchChannel);
    }

    /**
     * Remove a twitch channel for a specified {@link Guild}.
     * @param guildID The ID of the specified {@link Guild}.
     * @param twitchChannel The twitch channel to be removed.
     * @return Whether or not removing the twitch channel was successful.
     */
    @NotNull
    public Boolean removeTwitchChannel(@NotNull String guildID, @NotNull String twitchChannel) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "DELETE FROM beanbot.guild_twitch " +
                "WHERE guild_id = (?) AND twitch_channel = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            statement.setString(2, twitchChannel);

            statement.execute();
            updateGuildCache();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to reach the SQL database.");
            return false;
        }

    }

    /**
     * Removes a twitch channel for a specified {@link Guild}.
     * @param guild The specified {@link Guild}.
     * @param twitchChannel The twitch channel to be removed.
     * @return Whether or not removing the twitch channel was successful.
     */
    @NotNull
    public Boolean removeTwitchChannel(@NotNull Guild guild, @NotNull String twitchChannel) {
        return removeTwitchChannel(guild.getId(), twitchChannel);
    }

    /**
     * Update the Twitch Notification Channel for the specified {@link Guild}.
     * @param guildID The ID for the {@link Guild} specified.
     * @param textChannelID The ID for the {@link TextChannel} specified.
     * @return Whether or not updating was successful.
     */
    @NotNull
    public Boolean updateTwitchDiscordChannel(@NotNull String guildID, @NotNull String textChannelID) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.guild_information " +
                "SET twitch_channel_id = (?) " +
                "WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(textChannelID));
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
     * Update the Twitch Notification Channel for the specified {@link Guild}.
     * @param guild The {@link Guild} specified.
     * @param textChannel The {@link TextChannel} specified.
     * @return Whether or not it was successfully updated.
     */
    @NotNull
    public Boolean updateTwitchDiscordChannel(@NotNull Guild guild, @NotNull TextChannel textChannel) {
        return updateTwitchDiscordChannel(guild.getId(), textChannel.getId());
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
