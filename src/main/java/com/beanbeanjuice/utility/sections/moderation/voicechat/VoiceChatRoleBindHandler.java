package com.beanbeanjuice.utility.sections.moderation.voicechat;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A handler for {@link net.dv8tion.jda.api.entities.VoiceChannel} and their corresponding {@link net.dv8tion.jda.api.entities.Role Roles}.
 *
 * @author beanbeanjuice
 */
public class VoiceChatRoleBindHandler {

    /**
     * A map of Guild IDs containing a map of Voice Chat IDs and Role IDs.
     */
    private HashMap<String, HashMap<String, ArrayList<String>>> guildVoiceBinds;

    /**
     * Creates a new {@link VoiceChatRoleBindHandler} object.
     */
    public VoiceChatRoleBindHandler() {
        guildVoiceBinds = new HashMap<>();
        updateCache();
    }

    /**
     * Gets the bound roles for the specified {@link net.dv8tion.jda.api.entities.Guild Guild} and {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param voiceChannelID The ID of the {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}.
     * @return The {@link ArrayList<String>} of {@link net.dv8tion.jda.api.entities.Role} IDs bound to the {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}.
     */
    @NotNull
    public ArrayList<String> getBoundRoles(@NotNull String guildID, @NotNull String voiceChannelID) {
        try {
            return guildVoiceBinds.get(guildID).get(voiceChannelID);
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Updates the cache in the database.
     */
    private void updateCache() {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.voice_channel_role_binds;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(arguments);

            while (resultSet.next()) {
                String guildID = String.valueOf(resultSet.getLong(1));
                String voiceChannelID = String.valueOf(resultSet.getLong(2));
                String roleID = String.valueOf(resultSet.getLong(3));

                createIfNotExists(guildID, voiceChannelID);

                guildVoiceBinds.get(guildID).get(voiceChannelID).add(roleID);
            }
        } catch (SQLException e) {
            CafeBot.getLogManager().log(VoiceChatRoleBindHandler.class, LogLevel.WARN, "Error Updating Voice Channel Bind Cache: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a table in the cache if it does not exist.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param voiceChannelID The ID of the {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} in the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     */
    private void createIfNotExists(@NotNull String guildID, @NotNull String voiceChannelID) {
        // Checking if the guild exists in the cache.
        if (!guildVoiceBinds.containsKey(guildID)) {
            guildVoiceBinds.put(guildID, new HashMap<>());
        }

        // Checking if the voice channel ID exists in the cache.
        if (!guildVoiceBinds.get(guildID).containsKey(voiceChannelID)) {
            guildVoiceBinds.get(guildID).put(voiceChannelID, new ArrayList<>());
        }
    }

    /**
     * Bind a {@link net.dv8tion.jda.api.entities.Role Role} to a specified {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild} to remove the {@link net.dv8tion.jda.api.entities.Role Role} from.
     * @param voiceChannelID The ID of the {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} in the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param roleID The ID of the {@link net.dv8tion.jda.api.entities.Role Role} in the specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @return Whether or not binding it was successful.
     */
    @NotNull
    public Boolean bind(@NotNull String guildID, @NotNull String voiceChannelID, @NotNull String roleID) {
        createIfNotExists(guildID, voiceChannelID);

        if (!guildVoiceBinds.get(guildID).get(voiceChannelID).contains(roleID)) {

            Connection connection = CafeBot.getSQLServer().getConnection();
            String arguments = "INSERT INTO cafeBot.voice_channel_role_binds (guild_id, voice_channel_id, role_id) VALUES (?,?,?);";

            try {
                PreparedStatement statement = connection.prepareStatement(arguments);
                statement.setLong(1, Long.parseLong(guildID));
                statement.setLong(2, Long.parseLong(voiceChannelID));
                statement.setLong(3, Long.parseLong(roleID));
                statement.execute();
                guildVoiceBinds.get(guildID).get(voiceChannelID).add(roleID);
                return true;
            } catch (SQLException e) {
                CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Binding Role to Voice Channel: " + e.getMessage(), e);
                return false;
            }

        }
        return false;
    }

    /**
     * Unbind a {@link net.dv8tion.jda.api.entities.Role Role} from a specified {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild} to remove the {@link net.dv8tion.jda.api.entities.Role Role} from.
     * @param voiceChannelID The ID of the {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} in the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param roleID The ID of the {@link net.dv8tion.jda.api.entities.Role Role} in the specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @return Whether or not unbinding it was successful.
     */
    @NotNull
    public Boolean unBind(@NotNull String guildID, @NotNull String voiceChannelID, @NotNull String roleID) {

        if (!guildVoiceBinds.get(guildID).get(voiceChannelID).contains(roleID)) {
            return false;
        }

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "DELETE FROM cafeBot.voice_channel_role_binds WHERE guild_id = (?) AND voice_channel_id = (?) AND role_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            statement.setLong(2, Long.parseLong(voiceChannelID));
            statement.setLong(3, Long.parseLong(roleID));
            statement.execute();
            guildVoiceBinds.get(guildID).get(voiceChannelID).remove(roleID);
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Un-Binding Role from Voice Channel: " + e.getMessage(), e);
            return false;
        }
    }

}
