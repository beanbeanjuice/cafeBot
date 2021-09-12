package com.beanbeanjuice.utility.guild;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sections.music.custom.CustomGuildSongQueueHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A {@link CustomGuild} that contains {@link net.dv8tion.jda.api.entities.Guild Guild} information.
 *
 * @author beanbeanjuice
 */
public class CustomGuild {

    private final String guildID;
    private String prefix;
    private String moderatorRoleID;
    private final ArrayList<String> twitchChannels;
    private String mutedRoleID;
    private String liveNotificationsRoleID;
    private Boolean notifyOnUpdate;
    private Boolean aiState;

    private TextChannel lastMusicChannel;

    private final ArrayList<TextChannel> deletingMessagesChannels;

    private final CustomGuildSongQueueHandler customGuildSongQueueHandler;

    private final HashMap<CustomChannel, String> customChannelIDs;

    /**
     * Creates a new {@link CustomGuild} object.
     * @param guildID The ID of the {@link Guild}.
     * @param prefix The prefix the bot will use for the {@link Guild}.
     * @param moderatorRoleID The ID of the moderator {@link Role} for the {@link Guild}.
     * @param liveChannelID The ID of the {@link TextChannel} to send twitch notifications to in the {@link Guild}.
     * @param twitchChannels The {@link ArrayList<String>} of twitch channels for the {@link Guild}.
     * @param mutedRoleID The ID of the muted {@link Role} for the {@link Guild}.
     * @param liveNotificationsRoleID The ID of the live notifications {@link Role} for the {@link Guild}.
     * @param notifyOnUpdate The {@link Boolean} of whether or not to notify the {@link Guild} on an update to the Bot.
     * @param updateChannelID The ID of the {@link TextChannel} to send the bot update notifications to.
     * @param pollChannelID The ID of the {@link TextChannel} being used for {@link com.beanbeanjuice.utility.sections.fun.poll.Poll Poll}s.
     * @param birthdayChannelID The ID of the {@link TextChannel} being used for {@link com.beanbeanjuice.utility.sections.fun.birthday.BirthdayHandler Birthday} notifications.
     * @param welcomeChannelID The ID of the {@link TextChannel} being used for the Welcome notifications.
     * @param ventingChannelID The ID of the {@link TextChannel} being used for anonymous venting.
     * @param aiState Whether or not the AI portion should be enabled for this {@link CustomGuild}.
     */
    public CustomGuild(@NotNull String guildID, @NotNull String prefix, @NotNull String moderatorRoleID,
                       @NotNull String liveChannelID, @NotNull ArrayList<String> twitchChannels, @NotNull String mutedRoleID,
                       @NotNull String liveNotificationsRoleID, @NotNull Boolean notifyOnUpdate, @NotNull String updateChannelID,
                       @NotNull String countingChannelID, @NotNull String pollChannelID, @NotNull String raffleChannelID,
                       @NotNull String birthdayChannelID, @NotNull String welcomeChannelID, @NotNull String logChannelID,
                       @NotNull String ventingChannelID, @NotNull Boolean aiState, @NotNull String dailyChannelID) {
        customChannelIDs = new HashMap<>();

        this.guildID = guildID;
        this.prefix = prefix;
        this.moderatorRoleID = moderatorRoleID;
        customChannelIDs.put(CustomChannel.LIVE, liveChannelID);
        this.twitchChannels = twitchChannels;
        this.mutedRoleID = mutedRoleID;
        this.liveNotificationsRoleID = liveNotificationsRoleID;
        this.notifyOnUpdate = notifyOnUpdate;
        customChannelIDs.put(CustomChannel.UPDATE, updateChannelID);
        customChannelIDs.put(CustomChannel.COUNTING, countingChannelID);
        customChannelIDs.put(CustomChannel.POLL, pollChannelID);
        customChannelIDs.put(CustomChannel.RAFFLE, raffleChannelID);
        customChannelIDs.put(CustomChannel.BIRTHDAY, birthdayChannelID);
        customChannelIDs.put(CustomChannel.WELCOME, welcomeChannelID);
        customChannelIDs.put(CustomChannel.LOG, logChannelID);
        customChannelIDs.put(CustomChannel.VENTING, ventingChannelID);
        this.aiState = aiState;
        customChannelIDs.put(CustomChannel.DAILY, dailyChannelID);

        // Checks if a Listener has already been created for that guild.
        // This is so that if the cache is reloaded, it does not need to recreate the Listeners.
        CafeBot.getTwitchHandler().addTwitchChannels(this.twitchChannels);

        deletingMessagesChannels = new ArrayList<>();

        customGuildSongQueueHandler = new CustomGuildSongQueueHandler(guildID);
    }

    /**
     * @return The {@link CustomGuildSongQueueHandler} for the {@link CustomGuild}.
     */
    public CustomGuildSongQueueHandler getCustomGuildSongQueue() {
        return customGuildSongQueueHandler;
    }

    /**
     * Log certain actions.
     * @param command The {@link ICommand} that sent the log.
     * @param level The {@link LogLevel} of the log.
     * @param title The title of the log.
     * @param description The description of the log.
     */
    public void log(@NotNull ICommand command, @NotNull LogLevel level, @NotNull String title, @NotNull String description) {
        if (getLogChannel() != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(title + " - " + level.getCode());
            embedBuilder.setDescription(description);
            embedBuilder.setThumbnail(level.getImageURL());
            embedBuilder.setColor(level.getColor());
            embedBuilder.setFooter(command.getName() + " command");
            embedBuilder.setTimestamp(new Date().toInstant());
            getLogChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }

    /**
     * @return The current {@link Boolean} for the AI state.
     */
    @NotNull
    public Boolean getAIState() {
        return aiState;
    }

    /**
     * Updates the ai status for the {@link CustomGuild}.
     * @param aiState The new {@link Boolean} status to set it to.
     * @return Whether or not it was successfully updated.
     */
    @NotNull
    public Boolean setAIState(@NotNull Boolean aiState) {
        if (CafeBot.getGuildHandler().updateAIResponse(guildID, aiState)) {
            this.aiState = aiState;
            return true;
        }
        return false;
    }

    /**
     * @return The venting {@link TextChannel} for the {@link Guild}.
     */
    @Nullable
    public TextChannel getVentingChannel() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.VENTING));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Sets the venting {@link TextChannel} for the {@link Guild}.
     * @param ventingChannelID The ID of the venting {@link TextChannel}.
     * @return Whether or not updating the venting {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setVentingChannelID(@NotNull String ventingChannelID) {
        if (CafeBot.getGuildHandler().updateVentingChannelID(guildID, ventingChannelID)) {
            customChannelIDs.put(CustomChannel.VENTING, ventingChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The daily {@link TextChannel} for the {@link Guild}. Null if does not exist.
     */
    @Nullable
    public TextChannel getDailyChannel() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.DAILY));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Sets the daily {@link TextChannel} for the current {@link Guild}.
     * @param dailyChannelID The ID of the daily {@link TextChannel}.
     * @return True if the daily {@link TextChannel} was updated successfully.
     */
    @NotNull
    public Boolean setDailyChannelID(@NotNull String dailyChannelID) {
        if (CafeBot.getGuildHandler().setDailyChannelID(guildID, dailyChannelID)) {
            customChannelIDs.put(CustomChannel.DAILY, dailyChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The log {@link TextChannel} for the {@link Guild}.
     */
    @Nullable
    public TextChannel getLogChannel() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.LOG));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Sets the log {@link TextChannel} for the {@link Guild}.
     * @param logChannelID The ID of the log {@link TextChannel}.
     * @return Whether or not updating the log {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setLogChannelID(@NotNull String logChannelID) {
        if (CafeBot.getGuildHandler().updateLogChannelID(guildID, logChannelID)) {
            customChannelIDs.put(CustomChannel.LOG, logChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The welcome {@link TextChannel} for the {@link Guild}.
     */
    @Nullable
    public TextChannel getWelcomeChannel() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.WELCOME));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Update the welcome {@link TextChannel} for the {@link Guild}.
     * @param welcomeChannelID The ID of the new welcome {@link TextChannel}.
     * @return Whether or not it was successfully updated.
     */
    @NotNull
    public Boolean setWelcomeChannelID(@NotNull String welcomeChannelID) {
        if (CafeBot.getGuildHandler().updateWelcomeChannelID(guildID, welcomeChannelID)) {
            customChannelIDs.put(CustomChannel.WELCOME, welcomeChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The twitch channels for the {@link Guild}.
     */
    @NotNull
    public ArrayList<String> getTwitchChannels() {
        return twitchChannels;
    }

    /**
     * @return The birthday {@link TextChannel}.
     */
    @Nullable
    public TextChannel getBirthdayChannel() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.BIRTHDAY));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Set the birthday {@link TextChannel} for the {@link Guild}.
     * @param birthdayChannelID The ID of the specified {@link TextChannel}.
     * @return Whether or not setting it was successful.
     */
    @NotNull
    public Boolean setBirthdayChannelID(@NotNull String birthdayChannelID) {
        if (CafeBot.getGuildHandler().setBirthdayChannelID(guildID, birthdayChannelID)) {
            customChannelIDs.put(CustomChannel.BIRTHDAY, birthdayChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The {@link com.beanbeanjuice.utility.sections.fun.raffle.Raffle Raffle} {@link TextChannel} for the {@link Guild}.
     */
    @Nullable
    public TextChannel getRaffleChannel() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.RAFFLE));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Sets the {@link com.beanbeanjuice.utility.sections.fun.raffle.Raffle Raffle} {@link TextChannel}.
     * @param raffleChannelID The ID of the {@link TextChannel}.
     * @return Whether or not setting it was successful.
     */
    @NotNull
    public Boolean setRaffleChannel(@NotNull String raffleChannelID) {
        if (CafeBot.getGuildHandler().setRaffleChannelID(guildID, raffleChannelID)) {
            customChannelIDs.put(CustomChannel.RAFFLE, raffleChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The poll {@link TextChannel} for the {@link Guild}.
     */
    @Nullable
    public TextChannel getPollChannel() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.POLL));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Set the poll {@link TextChannel} for the {@link Guild}.
     * @param pollChannelID The ID of the {@link TextChannel}.
     * @return Whether or not setting the poll {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setPollChannel(@NotNull String pollChannelID) {
        if (CafeBot.getGuildHandler().setPollChannelID(guildID, pollChannelID)) {
            customChannelIDs.put(CustomChannel.POLL, pollChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The counting {@link TextChannel} for the {@link Guild}.
     */
    @Nullable
    public TextChannel getCountingChannel() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.COUNTING));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Set the Counting {@link TextChannel} for the {@link Guild}.
     * @param countingChannelID The ID of the {@link TextChannel} used for counting.
     * @return Whether or not setting the counting {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setCountingChannel(@NotNull String countingChannelID) {
        if (CafeBot.getGuildHandler().setCountingChannelID(guildID, countingChannelID)) {
            customChannelIDs.put(CustomChannel.COUNTING, countingChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The update {@link TextChannel} for the {@link Guild}.
     */
    @Nullable
    public TextChannel getUpdateChannel() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.UPDATE));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Set the Bot Update {@link TextChannel} for the {@link Guild}.
     * @param updateChannelID The ID of the {@link TextChannel} used for sending bot updates.
     * @return Whether or not setting the update {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setUpdateChannel(@NotNull String updateChannelID) {
        if (CafeBot.getGuildHandler().setUpdateChannelID(guildID, updateChannelID)) {
            customChannelIDs.put(CustomChannel.UPDATE, updateChannelID);
            return true;
        }
        return false;
    }

    /**
     * Sets the {@link Boolean} for if the {@link Guild} should be notified on an update.
     * @param answer The {@link Boolean} answer.
     * @return Whether or not updating it was successful.
     */
    @NotNull
    public Boolean setNotifyOnUpdate(@NotNull Boolean answer) {
        if (CafeBot.getGuildHandler().setNotifyOnUpdate(guildID, answer)) {
            notifyOnUpdate = answer;
            return true;
        }
        return false;
    }

    /**
     * @return The current state of whether or not the {@link Guild} should be notified on an update.
     */
    @NotNull
    public Boolean getNotifyOnUpdate() {
        return notifyOnUpdate;
    }

    /**
     * Sets the Live Notifications {@link Role} ID for the {@link Guild}.
     * @param role The {@link Role} for the Live Notifications {@link Role}.
     * @return Whether or not it was successful.
     */
    @NotNull
    public Boolean setLiveNotificationsRole(@NotNull Role role) {
        return setLiveNotificationsRoleID(role.getId());
    }

    /**
     * Sets the Live Notifications {@link Role} ID for the {@link Guild}.
     * @param roleID The ID for the Live Notifications {@link Role}.
     * @return Whether or not it was successful.
     */
    @NotNull
    public Boolean setLiveNotificationsRoleID(@NotNull String roleID) {

        // Only set it if it updates in the database.
        if (CafeBot.getGuildHandler().setLiveNotificationsRoleID(guildID, roleID)) {
            liveNotificationsRoleID = roleID;
            return true;
        }
        return false;
    }

    /**
     * @return The live notifications {@link Role}.
     */
    @Nullable
    public Role getLiveNotificationsRole() {
        return CafeBot.getGuildHandler().getGuild(guildID).getRoleById(liveNotificationsRoleID);
    }

    /**
     * Remove a {@link TextChannel} from the list of currently deleting {@link Message}s.
     * @param channel The {@link TextChannel} to remove.
     */
    public void removeTextChannelFromDeletingMessages(@NotNull TextChannel channel) {
        deletingMessagesChannels.remove(channel);
    }

    /**
     * Add a {@link TextChannel} to the list of currently deleting {@link Message}s.
     * @param channel The {@link TextChannel} to add.
     */
    public void addTextChannelToDeletingMessages(@NotNull TextChannel channel) {
        deletingMessagesChannels.add(channel);
    }

    /**
     * Checks to make sure that a {@link TextChannel} does not already have {@link Message}s being
     * currently deleted.
     * @param channel The {@link TextChannel} to check.
     * @return Whether or not it already has {@link Message}s being deleted in it.
     */
    @NotNull
    public Boolean containsTextChannelDeletingMessages(@NotNull TextChannel channel) {
        return deletingMessagesChannels.contains(channel);
    }

    /**
     * @return The {@link String} ID of the live channel to send messages.
     */
    @NotNull
    public String getLiveChannelID() {
        return customChannelIDs.get(CustomChannel.LIVE);
    }

    /**
     * Sets the last channel the music commands were sent in.
     * @param lastMusicChannel The {@link TextChannel} of the last music command.
     */
    public void setLastMusicChannel(TextChannel lastMusicChannel) {
        this.lastMusicChannel = lastMusicChannel;
    }

    /**
     * @return The last music {@link TextChannel}.
     */
    @Nullable
    public TextChannel getLastMusicChannel() {
        return lastMusicChannel;
    }

    /**
     * Sends a message in the specified last music {@link TextChannel}.
     * @param embed The {@link MessageEmbed} to be sent.
     */
    public void sendMessageInLastMusicChannel(MessageEmbed embed) {
        try {
            lastMusicChannel.sendMessageEmbeds(embed).queue();
        } catch (NullPointerException ignored) {}
    }

    /**
     * Update the muted {@link Role} for the {@link Guild}.
     * @param mutedRoleID The ID of the muted {@link Role}.
     * @return Whether or not the {@link Role} was successfully updated in the database.
     */
    @NotNull
    public Boolean setMutedRoleID(String mutedRoleID) {

        if (CafeBot.getGuildHandler().updateGuildMutedRole(guildID, mutedRoleID)) {
            this.mutedRoleID = mutedRoleID;
            return true;
        }
        return false;
    }

    /**
     * Update the moderator {@link Role} for the {@link Guild}.
     * @param moderatorRoleID The ID of the moderator {@link Role}.
     * @return Whether or not the {@link Role} was successfully updated in the database.
     */
    @NotNull
    public Boolean setModeratorRoleID(@NotNull String moderatorRoleID) {
        if (CafeBot.getGuildHandler().updateGuildModeratorRole(guildID, moderatorRoleID)) {
            this.moderatorRoleID = moderatorRoleID;
            return true;
        }
        return false;
    }

    /**
     * @return The ID for the {@link CustomGuild}.
     */
    @NotNull
    public String getGuildID() {
        return guildID;
    }

    /**
     * @return The prefix for the {@link CustomGuild}.
     */
    @NotNull
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the prefix for the {@link CustomGuild}.
     * @param newPrefix The new prefix.
     * @return Whether or not setting the prefix was successful.
     */
    @NotNull
    public Boolean setPrefix(String newPrefix) {
        if (CafeBot.getGuildHandler().updateGuildPrefix(guildID, newPrefix)) {
            this.prefix = newPrefix;
            return true;
        }
        return false;
    }

    /**
     * @return The muted {@link Role} for the current {@link Guild}.
     */
    @Nullable
    public Role getMutedRole() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getRoleById(mutedRoleID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return The moderator {@link Role} for the current {@link Guild}.
     */
    @Nullable
    public Role getModeratorRole() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getRoleById(moderatorRoleID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Add a twitch channel to the {@link Guild}.
     * @param twitchChannel The name of the twitch channel to add.
     * @return Whether or not the twitch channel was successfully added.
     */
    @NotNull
    public Boolean addTwitchChannel(String twitchChannel) {
        twitchChannel = twitchChannel.toLowerCase();

        if (twitchChannels.contains(twitchChannel)) {
            return false;
        }

        if (CafeBot.getGuildHandler().addTwitchChannel(guildID, twitchChannel)) {
            twitchChannels.add(twitchChannel.toLowerCase());
            CafeBot.getTwitchHandler().addTwitchChannel(twitchChannel);
            return true;
        }
        return false;
    }

    /**
     * Removes a twitch channel from the {@link Guild}.
     * @param twitchChannel The name of the twitch channel to be removed.
     * @return Whether or not the twitch channel was successfully removed.
     */
    @NotNull
    public Boolean removeTwitchChannel(String twitchChannel) {
        twitchChannel = twitchChannel.toLowerCase();

        if (!twitchChannels.contains(twitchChannel)) {
            return false;
        }

        if (CafeBot.getGuildHandler().removeTwitchChannel(guildID, twitchChannel)) {
            twitchChannels.remove(twitchChannel.toLowerCase());
            return true;
        }
        return false;
    }

    /**
     * Update the twitch channel for the {@link CustomGuild}.
     * @param liveChannelID The new channel ID for the channel.
     * @return Whether or not the channel was successfully updated.
     */
    @NotNull
    public Boolean updateTwitchDiscordChannel(String liveChannelID) {
        if (CafeBot.getGuildHandler().updateTwitchChannelID(guildID, liveChannelID)) {
            customChannelIDs.put(CustomChannel.LIVE, liveChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return All Custom Channel Names and IDs for the {@link Guild}.
     */
    @NotNull
    public HashMap<CustomChannel, String> getCustomChannelIDs() {
        return customChannelIDs;
    }

}
