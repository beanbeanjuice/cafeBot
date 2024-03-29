package com.beanbeanjuice.utility.handler.guild;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.beanbeanjuice.utility.section.twitch.TwitchHandler;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private final HashMap<CustomChannel, String> customChannelIDs;

    /**
     * Creates a new {@link CustomGuild} object.
     *
     * @param guildID                 The ID of the {@link Guild}.
     * @param prefix                  The prefix the bot will use for the {@link Guild}.
     * @param moderatorRoleID         The ID of the moderator {@link Role} for the {@link Guild}.
     * @param liveChannelID           The ID of the {@link TextChannel} to send twitch notifications to in the {@link Guild}.
     * @param twitchChannels          The {@link ArrayList<String>} of twitch channels for the {@link Guild}.
     * @param mutedRoleID             The ID of the muted {@link Role} for the {@link Guild}.
     * @param liveNotificationsRoleID The ID of the live notifications {@link Role} for the {@link Guild}.
     * @param notifyOnUpdate          The {@link Boolean} of whether to notify the {@link Guild} on an update to the Bot.
     * @param updateChannelID         The ID of the {@link TextChannel} to send the bot update notifications to.
     * @param pollChannelID           The ID of the {@link TextChannel} being used for {@link com.beanbeanjuice.utility.section.moderation.poll.Poll Polls}.
     * @param birthdayChannelID       The ID of the {@link TextChannel} being used for {@link com.beanbeanjuice.utility.section.fun.BirthdayHandler Birthday} notifications.
     * @param welcomeChannelID        The ID of the {@link TextChannel} being used for the Welcome notifications.
     * @param goodbyeChannelID        The ID of the {@link TextChannel} being used for the Goodbye notifications.
     * @param ventingChannelID        The ID of the {@link TextChannel} being used for anonymous venting.
     * @param aiState                 True, if the AI portion should be enabled for this {@link CustomGuild}.
     */
    public CustomGuild(@NotNull String guildID, @NotNull String prefix, @NotNull String moderatorRoleID,
                       @NotNull String liveChannelID, @NotNull ArrayList<String> twitchChannels, @NotNull String mutedRoleID,
                       @NotNull String liveNotificationsRoleID, @NotNull Boolean notifyOnUpdate, @NotNull String updateChannelID,
                       @NotNull String countingChannelID, @NotNull String pollChannelID, @NotNull String raffleChannelID,
                       @NotNull String birthdayChannelID, @NotNull String welcomeChannelID, @NotNull String goodbyeChannelID,
                       @NotNull String logChannelID, @NotNull String ventingChannelID, @NotNull Boolean aiState,
                       @NotNull String dailyChannelID) {
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
        customChannelIDs.put(CustomChannel.GOODBYE, goodbyeChannelID);
        customChannelIDs.put(CustomChannel.LOG, logChannelID);
        customChannelIDs.put(CustomChannel.VENTING, ventingChannelID);
        this.aiState = aiState;
        customChannelIDs.put(CustomChannel.DAILY, dailyChannelID);

        // Checks if a Listener has already been created for that guild.
        // This is so that if the cache is reloaded, it does not need to recreate the Listeners.
        TwitchHandler.addTwitchChannels(guildID, this.twitchChannels);
    }

    /**
     * Log certain actions.
     *
     * @param command     The {@link ICommand} that sent the log.
     * @param level       The {@link LogLevel} of the log.
     * @param title       The title of the log.
     * @param description The description of the log.
     */
    public void log(@NotNull ICommand command, @NotNull LogLevel level, @NotNull String title, @NotNull String description) {
        if (getLogChannel() != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(title + " - " + level.getCode());
            embedBuilder.setDescription(description);
            embedBuilder.setThumbnail(level.getImageURL());
            embedBuilder.setColor(level.getColor());
            embedBuilder.setFooter(command + " command");
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
     * Updates the AI status for the {@link CustomGuild}.
     *
     * @param aiState The new {@link Boolean} status to set it to.
     * @return True, if it was successfully updated.
     */
    @NotNull
    public Boolean setAIState(@NotNull Boolean aiState) {
        if (GuildHandler.setAIState(guildID, aiState)) {
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
            return GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.VENTING));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Sets the venting {@link TextChannel} for the {@link Guild}.
     *
     * @param ventingChannelID The ID of the venting {@link TextChannel}.
     * @return True, if updating the venting {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setVentingChannelID(@NotNull String ventingChannelID) {
        if (GuildHandler.setVentingChannelID(guildID, ventingChannelID)) {
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
            return GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.DAILY));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Sets the daily {@link TextChannel} for the current {@link Guild}.
     *
     * @param dailyChannelID The ID of the daily {@link TextChannel}.
     * @return True, if the daily {@link TextChannel} was updated successfully.
     */
    @NotNull
    public Boolean setDailyChannelID(@NotNull String dailyChannelID) {
        if (GuildHandler.setDailyChannelID(guildID, dailyChannelID)) {
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
            return GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.LOG));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Sets the log {@link TextChannel} for the {@link Guild}.
     *
     * @param logChannelID The ID of the log {@link TextChannel}.
     * @return True, if updating the log {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setLogChannelID(@NotNull String logChannelID) {
        if (GuildHandler.setLogChannelID(guildID, logChannelID)) {
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
            return GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.WELCOME));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return The goodbye {@link TextChannel} for the {@link Guild}.
     */
    @Nullable
    public TextChannel getGoodbyeChannel() {
        try {
            return GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.GOODBYE));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Update the welcome {@link TextChannel} for the {@link Guild}.
     *
     * @param welcomeChannelID The ID of the new welcome {@link TextChannel}.
     * @return True, if it was successfully updated.
     */
    @NotNull
    public Boolean setWelcomeChannelID(@NotNull String welcomeChannelID) {
        if (GuildHandler.setWelcomeChannelID(guildID, welcomeChannelID)) {
            customChannelIDs.put(CustomChannel.WELCOME, welcomeChannelID);
            return true;
        }
        return false;
    }

    /**
     * Update the goodbye {@link TextChannel} for the {@link Guild}.
     *
     * @param goodbyeChannelID The ID of the new goodbye {@link TextChannel}.
     * @return True, if it was successfully updated.
     */
    @NotNull
    public Boolean setGoodbyeChannelID(@NotNull String goodbyeChannelID) {
        if (GuildHandler.setGoodbyeChannelID(guildID, goodbyeChannelID)) {
            customChannelIDs.put(CustomChannel.GOODBYE, goodbyeChannelID);
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
            return GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.BIRTHDAY));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Set the birthday {@link TextChannel} for the {@link Guild}.
     *
     * @param birthdayChannelID The ID of the specified {@link TextChannel}.
     * @return True, if setting the birthday {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setBirthdayChannelID(@NotNull String birthdayChannelID) {
        if (GuildHandler.setBirthdayChannelID(guildID, birthdayChannelID)) {
            customChannelIDs.put(CustomChannel.BIRTHDAY, birthdayChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The {@link com.beanbeanjuice.utility.section.moderation.raffle.Raffle Raffle} {@link TextChannel} for the {@link Guild}.
     */
    @Nullable
    public TextChannel getRaffleChannel() {
        try {
            return GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.RAFFLE));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Sets the {@link com.beanbeanjuice.utility.section.moderation.raffle.Raffle Raffle} {@link TextChannel} for the {@link Guild}.
     *
     * @param raffleChannelID The ID of the {@link TextChannel}.
     * @return True, if setting it was successful.
     */
    @NotNull
    public Boolean setRaffleChannel(@NotNull String raffleChannelID) {
        if (GuildHandler.setRaffleChannelID(guildID, raffleChannelID)) {
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
            return GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.POLL));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Set the {@link com.beanbeanjuice.utility.section.moderation.poll.Poll Poll} {@link TextChannel} for the {@link Guild}.
     *
     * @param pollChannelID The ID of the {@link TextChannel}.
     * @return True, if setting the poll {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setPollChannel(@NotNull String pollChannelID) {
        if (GuildHandler.setPollChannelID(guildID, pollChannelID)) {
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
            return GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.COUNTING));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Set the Counting {@link TextChannel} for the {@link Guild}.
     *
     * @param countingChannelID The ID of the {@link TextChannel} used for counting.
     * @return True, if setting the counting {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setCountingChannel(@NotNull String countingChannelID) {
        if (GuildHandler.setCountingChannelID(guildID, countingChannelID)) {
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
            return GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.UPDATE));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Set the Bot Update {@link TextChannel} for the {@link Guild}.
     *
     * @param updateChannelID The ID of the {@link TextChannel} used for sending bot updates.
     * @return True, if setting the update {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setUpdateChannel(@NotNull String updateChannelID) {
        if (GuildHandler.setUpdateChannelID(guildID, updateChannelID)) {
            customChannelIDs.put(CustomChannel.UPDATE, updateChannelID);
            return true;
        }
        return false;
    }

    /**
     * Sets the {@link Boolean} for if the {@link Guild} should be notified on an update.
     *
     * @param answer The {@link Boolean} answer.
     * @return True, if updating it was successful.
     */
    @NotNull
    public Boolean setNotifyOnUpdate(@NotNull Boolean answer) {
        if (GuildHandler.setNotifyOnUpdate(guildID, answer)) {
            notifyOnUpdate = answer;
            return true;
        }
        return false;
    }

    /**
     * @return The current state of if the {@link Guild} should be notified on an update.
     */
    @NotNull
    public Boolean getNotifyOnUpdate() {
        return notifyOnUpdate;
    }

    /**
     * Sets the Live Notifications {@link Role} ID for the {@link Guild}.
     *
     * @param roleID The ID for the Live Notifications {@link Role}.
     * @return True, if it was successful.
     */
    @NotNull
    public Boolean setLiveNotificationsRoleID(@NotNull String roleID) {

        // Only set it if it updates in the database.
        if (GuildHandler.setLiveNotificationsRoleID(guildID, roleID)) {
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
        return GuildHandler.getGuild(guildID).getRoleById(liveNotificationsRoleID);
    }

    /**
     * @return The {@link String} ID of the live channel to send messages.
     */
    @NotNull
    public String getLiveChannelID() {
        return customChannelIDs.get(CustomChannel.LIVE);
    }

    /**
     * Update the muted {@link Role} for the {@link Guild}.
     *
     * @param mutedRoleID The ID of the muted {@link Role}.
     * @return True, if the {@link Role} was successfully updated in the database.
     */
    @NotNull
    public Boolean setMutedRoleID(String mutedRoleID) {

        if (GuildHandler.setMutedRoleID(guildID, mutedRoleID)) {
            this.mutedRoleID = mutedRoleID;
            return true;
        }
        return false;
    }

    /**
     * Update the moderator {@link Role} for the {@link Guild}.
     *
     * @param moderatorRoleID The ID of the moderator {@link Role}.
     * @return True, if the {@link Role} was successfully updated in the database.
     */
    @NotNull
    public Boolean setModeratorRoleID(@NotNull String moderatorRoleID) {
        if (GuildHandler.setModeratorRoleID(guildID, moderatorRoleID)) {
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
     *
     * @param newPrefix The new prefix.
     * @return True, if setting the prefix was successful.
     */
    @NotNull
    public Boolean setPrefix(String newPrefix) {
        if (GuildHandler.setPrefix(guildID, newPrefix)) {
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
            return GuildHandler.getGuild(guildID).getRoleById(mutedRoleID);
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
            return GuildHandler.getGuild(guildID).getRoleById(moderatorRoleID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Add a twitch channel to the {@link Guild}.
     * @param twitchChannel The name of the twitch channel to add.
     * @return True, if the twitch channel was successfully added.
     */
    @NotNull
    public Boolean addTwitchChannel(String twitchChannel) {
        twitchChannel = twitchChannel.toLowerCase();

        // Check if the channel exists.
        if (!TwitchHandler.getTwitchListener().channelExists(twitchChannel)) {
            Bot.getCafeAPI().TWITCH.removeGuildTwitch(guildID, twitchChannel);
            return false;
        }

        if (twitchChannels.contains(twitchChannel)) {
            return false;
        }

        if (GuildHandler.addTwitchChannel(guildID, twitchChannel)) {
            twitchChannels.add(twitchChannel.toLowerCase());

            try {
                TwitchHandler.addTwitchChannel(twitchChannel);
                return TwitchHandler.addCache(guildID, twitchChannel);
            } catch (HystrixRuntimeException | ContextedRuntimeException e) {

                // If this is reached, it means the twitch channel does not exist.
                Bot.getCafeAPI().TWITCH.removeGuildTwitch(guildID, twitchChannel);
                return false;
            }
        }

        return false;
    }

    /**
     * Removes a twitch channel from the {@link Guild}.
     * @param twitchChannel The name of the twitch channel to be removed.
     * @return True, if the twitch channel was successfully removed.
     */
    @NotNull
    public Boolean removeTwitchChannel(String twitchChannel) {
        twitchChannel = twitchChannel.toLowerCase();

        if (!twitchChannels.contains(twitchChannel)) {
            return false;
        }

        if (GuildHandler.removeTwitchChannel(guildID, twitchChannel)) {
            twitchChannels.remove(twitchChannel.toLowerCase());

            return TwitchHandler.removeCache(guildID, twitchChannel);
        }

        return false;
    }

    /**
     * Update the twitch channel for the {@link CustomGuild}.
     *
     * @param liveChannelID The new channel ID for the channel.
     * @return True, if the channel was successfully updated.
     */
    @NotNull
    public Boolean updateTwitchDiscordChannel(String liveChannelID) {
        if (GuildHandler.setTwitchChannelID(guildID, liveChannelID)) {
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

    /**
     * Check is a specified {@link String channelID} is already a {@link CustomChannel}.
     * @param channelID The {@link String channelID} to search for.
     * @return True, if the {@link String channelID} is a {@link CustomChannel}.
     */
    @NotNull
    public Boolean isCustomChannel(@NotNull String channelID) {
        for (Map.Entry<CustomChannel, String> pair : customChannelIDs.entrySet()) {
            if (pair.getValue().equalsIgnoreCase(channelID))
                return true;
        }
        return false;
    }

    /**
     * Check if a specified {@link String channelID} is a {@link CustomChannel}.
     * @param channelID The {@link String channelID} to search for.
     * @return True, if the {@link String channelID} is a {@link CustomChannel}.
     */
    @NotNull
    public Boolean isDailyChannel(@NotNull String channelID) {
        return customChannelIDs.get(CustomChannel.DAILY).equalsIgnoreCase(channelID);
    }
}
