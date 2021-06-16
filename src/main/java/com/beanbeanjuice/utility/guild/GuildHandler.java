package com.beanbeanjuice.utility.guild;

import com.beanbeanjuice.CafeBot;
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

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.guild_information;";

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
                String countingChannelID = String.valueOf(resultSet.getLong(9));
                String pollChannelID = String.valueOf(resultSet.getLong(10));
                String raffleChannelID = String.valueOf(resultSet.getLong(11));
                String birthdayChannelID = String.valueOf(resultSet.getLong(12));
                String welcomeChannelID = String.valueOf(resultSet.getLong(13));
                String logChannelID = String.valueOf(resultSet.getLong(14));
                String ventingChannelID = String.valueOf(resultSet.getLong(15));

                guildDatabase.put(guildID, new CustomGuild(guildID, prefix, moderatorRoleID,
                        twitchChannelID, twitchChannels, mutedRoleID,
                        liveNotificationsRoleID, notifyOnUpdate, updateChannelID,
                        countingChannelID, pollChannelID, raffleChannelID,
                        birthdayChannelID, welcomeChannelID, logChannelID,
                        ventingChannelID));
            }
        } catch (SQLException e) {
            CafeBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Unable to update Guild Cache: " + e.getMessage());
        }
    }

    /**
     * Gets the {@link ArrayList<String>} of Twitch Channels for the {@link Guild}.
     * @param guildID The ID of the {@link Guild}.
     * @return the {@link ArrayList<String>} of Twitch Channel Names
     */
    public ArrayList<String> getTwitchChannels(String guildID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.guild_twitch WHERE guild_id = ?;";

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
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to retrieve twitch channels from database.", true, false);
            return new ArrayList<>();
        }
    }

    @NotNull
    protected Boolean setBirthdayChannelID(@NotNull String guildID, @NotNull String birthdayChannelID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information SET birthday_channel_id = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(birthdayChannelID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Birthday Channel: " + e.getMessage());
            return false;
        }
    }

    @NotNull
    protected Boolean setRaffleChannelID(@NotNull String guildID, @NotNull String raffleChannelID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information SET raffle_channel_id = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(raffleChannelID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Raffle Channel: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sets the poll {@link TextChannel} for the {@link Guild}.
     * @param guildID The ID for the {@link Guild} to update.
     * @param pollChannelID The ID of the {@link TextChannel} used for polls.
     * @return Whether or not updating the poll ID was successful.
     */
    @NotNull
    protected Boolean setPollChannelID(@NotNull String guildID, @NotNull String pollChannelID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information SET poll_channel_id = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(pollChannelID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Poll Channel: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sets the counting {@link TextChannel} for the {@link Guild}.
     * @param guildID The ID for the {@link Guild} to update.
     * @param countingChannelID The ID of the {@link TextChannel} used for counting.
     * @return Whether or not updating the counting ID was successful.
     */
    @NotNull
    protected Boolean setCountingChannelID(@NotNull String guildID, @NotNull String countingChannelID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information SET counting_channel_id = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(countingChannelID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Counting Channel: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sets the update {@link TextChannel} for the {@link Guild}.
     * @param guildID The ID of the {@link Guild} to update.
     * @param updateChannelID The ID of the {@link TextChannel} to send bot updates.
     * @return Whether or not updating the update channel ID was successful.
     */
    @NotNull
    protected Boolean setUpdateChannelID(@NotNull String guildID, @NotNull String updateChannelID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information SET update_channel_id = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(updateChannelID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Update the Update Channel ID: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sets the {@link Boolean} for if the {@link Guild} should be notified on an update.
     * @param guildID The ID of the {@link Guild} provided.
     * @param answer The {@link Boolean} provided.
     * @return Whether or not updating it was successful.
     */
    @NotNull
    protected Boolean setNotifyOnUpdate(@NotNull String guildID, @NotNull Boolean answer) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information SET notify_on_update = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setBoolean(1, answer);
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Unable to Update the Notify On Update Parameter: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sets the Live Notifications {@link Role} for the {@link Guild}.
     * @param guildID The ID of the {@link Guild}.
     * @param roleID The ID of the Live Notifications {@link Role}.
     * @return Whether or not it was successfully updated in the database.
     */
    @NotNull
    protected Boolean setLiveNotificationsRoleID(@NotNull String guildID, @NotNull String roleID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information SET live_notifications_role_id = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(roleID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Unable to Update the Live Notifications Role ID: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the prefix for a specific {@link Guild}.
     * @param guildID The ID of the {@link Guild}.
     * @param newPrefix The new prefix for the {@link Guild}.
     * @return Whether or not the prefix was updated successfully.
     */
    @NotNull
    protected Boolean updateGuildPrefix(@NotNull String guildID, @NotNull String newPrefix) {

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information SET prefix = (?) WHERE guild_id = " + guildID + ";";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setString(1, newPrefix);

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(CustomGuild.class, LogLevel.ERROR, "Unable to reach the SQL database.");
            return false;
        }

    }

    /**
     * Checks all the current {@link Guild} in the database.
     */
    public void checkGuilds() {

        updateGuildCache();

        List<Guild> guildsHasBot = CafeBot.getJDA().getGuilds();
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
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "DELETE FROM cafeBot.guild_information " +
                "WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            statement.execute();
        } catch (SQLException e) {
            CafeBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Error Removing Guild: " + e.getMessage());
            return false;
        }

        arguments = "DELETE FROM cafeBot.guild_twitch WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            statement.execute();
        } catch (SQLException e) {
            CafeBot.getLogManager().log(GuildHandler.class, LogLevel.WARN, "Error Removing Guild's Twitch: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Removes a {@link Guild} from the database.
     * @param guild The ID of the {@link Guild} to be removed.
     * @return Whether or not the {@link Guild} was removed successfully.
     */
    @NotNull
    public Boolean removeGuild(@NotNull Guild guild) {
        if (removeGuild(guild.getId())) {
            guildDatabase.remove(guild.getId());
            return true;
        }
        return false;
    }

    /**
     * Adds a {@link Guild} to the database.
     * @param guildID The ID of the {@link Guild} to be added.
     * @return Whether or not the {@link Guild} was added successfully.
     */
    @NotNull
    public Boolean addGuild(@NotNull String guildID) {

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "INSERT INTO cafeBot.guild_information " +
                "(guild_id, prefix) " +
                "VALUES (?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            statement.setString(2, CafeBot.getPrefix());

            statement.execute();
            guildDatabase.put(guildID, new CustomGuild(guildID, CafeBot.getPrefix(), "0",
                    "0", new ArrayList<>(), "0",
                    "0", true, "0",
                    "0", "0", "0",
                    "0", "0", "0",
                    "0"));
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Unable to add Guild to SQL database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update the log {@link TextChannel} for the specified {@link Guild}.
     * @param guildID The ID of the {@link Guild}.
     * @param logChannelID The ID of the {@link TextChannel}.
     * @return Whether or not updating the log {@link TextChannel} was successful.
     */
    @NotNull
    protected Boolean updateLogChannelID(@NotNull String guildID, @NotNull String logChannelID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information SET log_channel_id = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(logChannelID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Updated the welcome {@link TextChannel} in the specified {@link Guild}.
     * @param guildID The ID of the {@link Guild} to update the {@link TextChannel} in.
     * @param welcomeChannelID The ID of the {@link TextChannel} to set the welcome {@link TextChannel} to in the {@link Guild}.
     * @return Whether or not the welcome {@link TextChannel} ID was updated successfully.
     */
    @NotNull
    protected Boolean updateWelcomeChannelID(@NotNull String guildID, @NotNull String welcomeChannelID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information SET welcome_channel_id = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(welcomeChannelID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Welcome Channel: " + e.getMessage());
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
    protected Boolean updateGuildMutedRole(@NotNull String guildID, @NotNull String roleID) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information " +
                "SET muted_role_id = (?) " +
                "WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(roleID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Unable to reach the SQL database.");
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
    protected Boolean updateGuildModeratorRole(@NotNull String guildID, @NotNull String roleID) {

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information " +
                "SET moderator_role_id = (?) " +
                "WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(roleID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Unable to reach the SQL database.");
            return false;
        }

    }

    /**
     * Adds a twitch channel for a specified {@link Guild}.
     * @param guildID The ID of the specified {@link Guild}.
     * @param twitchChannel Thw twitch channel to be added.
     * @return Whether or not adding the twitch channel was successful.
     */
    @NotNull
    protected Boolean addTwitchChannel(@NotNull String guildID, @NotNull String twitchChannel) {

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "INSERT INTO cafeBot.guild_twitch " +
                "(guild_id, twitch_channel) " +
                "VALUES (?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            statement.setString(2, twitchChannel);

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Unable to reach the SQL database.");
            return false;
        }

    }

    /**
     * Remove a twitch channel for a specified {@link Guild}.
     * @param guildID The ID of the specified {@link Guild}.
     * @param twitchChannel The twitch channel to be removed.
     * @return Whether or not removing the twitch channel was successful.
     */
    @NotNull
    protected Boolean removeTwitchChannel(@NotNull String guildID, @NotNull String twitchChannel) {

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "DELETE FROM cafeBot.guild_twitch " +
                "WHERE guild_id = (?) AND twitch_channel = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            statement.setString(2, twitchChannel);

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to reach the SQL database.");
            return false;
        }

    }

    /**
     * Update the Twitch Notification Channel for the specified {@link Guild}.
     * @param guildID The ID for the {@link Guild} specified.
     * @param textChannelID The ID for the {@link TextChannel} specified.
     * @return Whether or not updating was successful.
     */
    @NotNull
    public Boolean updateTwitchChannelID(@NotNull String guildID, @NotNull String textChannelID) {

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.guild_information " +
                "SET twitch_channel_id = (?) " +
                "WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(textChannelID));
            statement.setLong(2, Long.parseLong(guildID));

            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(GuildHandler.class, LogLevel.ERROR, "Unable to reach the SQL database.");
            return false;
        }

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
        return CafeBot.getJDA().getGuildById(guildID);
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
