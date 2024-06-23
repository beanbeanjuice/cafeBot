package com.beanbeanjuice.cafebot.utility.handler.guild;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafebot.utility.section.fun.BirthdayHandler;
import com.beanbeanjuice.cafebot.utility.section.moderation.poll.Poll;
import com.beanbeanjuice.cafebot.utility.section.moderation.raffle.Raffle;
import com.beanbeanjuice.cafebot.utility.section.twitch.TwitchHandler;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A {@link CustomGuild} that contains {@link net.dv8tion.jda.api.entities.Guild Guild} information.
 *
 * @author beanbeanjuice
 */
public class CustomGuild {

    @Getter private final String guildID;
    @Getter private String prefix;
    private String moderatorRoleID;
    private final ArrayList<String> twitchChannels;
    private String mutedRoleID;
    private String liveNotificationsRoleID;
    private boolean notifyOnUpdate;
    private boolean aiState;

    @Getter private final HashMap<CustomChannel, String> customChannelIDs;

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
     * @param pollChannelID           The ID of the {@link TextChannel} being used for {@link Poll Polls}.
     * @param birthdayChannelID       The ID of the {@link TextChannel} being used for {@link BirthdayHandler Birthday} notifications.
     * @param welcomeChannelID        The ID of the {@link TextChannel} being used for the Welcome notifications.
     * @param goodbyeChannelID        The ID of the {@link TextChannel} being used for the Goodbye notifications.
     * @param ventingChannelID        The ID of the {@link TextChannel} being used for anonymous venting.
     * @param aiState                 True, if the AI portion should be enabled for this {@link CustomGuild}.
     */
    public CustomGuild(String guildID, String prefix, String moderatorRoleID,
                       String liveChannelID, ArrayList<String> twitchChannels, String mutedRoleID,
                       String liveNotificationsRoleID, boolean notifyOnUpdate, String updateChannelID,
                       String countingChannelID, String pollChannelID, String raffleChannelID,
                       String birthdayChannelID, String welcomeChannelID, String goodbyeChannelID,
                       String logChannelID, String ventingChannelID, boolean aiState,
                       String dailyChannelID) {
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
    public void log(ICommand command, LogLevel level, String title, String description) {
        this.getLogChannel().ifPresent(channel -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(title + " - " + level.getCode());
            embedBuilder.setDescription(description);
            embedBuilder.setThumbnail(level.getImageURL());
            embedBuilder.setColor(level.getColor());
            embedBuilder.setFooter(command + " command");
            embedBuilder.setTimestamp(new Date().toInstant());
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        });
    }

    /**
     * @return The current {@link Boolean} for the AI state.
     */
    public boolean getAIState() {
        return aiState;
    }

    /**
     * Updates the AI status for the {@link CustomGuild}.
     *
     * @param aiState The new {@link Boolean} status to set it to.
     * @return True, if it was successfully updated.
     */
    public boolean setAIState(boolean aiState) {
        if (GuildHandler.setAIState(guildID, aiState)) {
            this.aiState = aiState;
            return true;
        }
        return false;
    }

    /**
     * @return The venting {@link TextChannel} for the {@link Guild}.
     */
    public Optional<TextChannel> getVentingChannel() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.VENTING)));
    }

    /**
     * Sets the venting {@link TextChannel} for the {@link Guild}.
     *
     * @param ventingChannelID The ID of the venting {@link TextChannel}.
     * @return True, if updating the venting {@link TextChannel} was successful.
     */
    public boolean setVentingChannelID(final String ventingChannelID) {
        if (GuildHandler.setVentingChannelID(guildID, ventingChannelID)) {
            customChannelIDs.put(CustomChannel.VENTING, ventingChannelID);
            return true;
        }
        return false;
    }

    public Optional<TextChannel> getDailyChannel() {
        try {
            return Optional.ofNullable(GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.DAILY)));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    /**
     * Sets the daily {@link TextChannel} for the current {@link Guild}.
     *
     * @param dailyChannelID The ID of the daily {@link TextChannel}.
     * @return True, if the daily {@link TextChannel} was updated successfully.
     */
    public boolean setDailyChannelID(final String dailyChannelID) {
        if (GuildHandler.setDailyChannelID(guildID, dailyChannelID)) {
            customChannelIDs.put(CustomChannel.DAILY, dailyChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The log {@link TextChannel} for the {@link Guild}.
     */
    public Optional<TextChannel> getLogChannel() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.LOG)));
    }

    /**
     * Sets the log {@link TextChannel} for the {@link Guild}.
     *
     * @param logChannelID The ID of the log {@link TextChannel}.
     * @return True, if updating the log {@link TextChannel} was successful.
     */
    public boolean setLogChannelID(final String logChannelID) {
        if (GuildHandler.setLogChannelID(guildID, logChannelID)) {
            customChannelIDs.put(CustomChannel.LOG, logChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The welcome {@link TextChannel} for the {@link Guild}.
     */
    public Optional<TextChannel> getWelcomeChannel() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.WELCOME)));
    }

    /**
     * @return The goodbye {@link TextChannel} for the {@link Guild}.
     */
    public Optional<TextChannel> getGoodbyeChannel() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.GOODBYE)));
    }

    /**
     * Update the welcome {@link TextChannel} for the {@link Guild}.
     *
     * @param welcomeChannelID The ID of the new welcome {@link TextChannel}.
     * @return True, if it was successfully updated.
     */
    public boolean setWelcomeChannelID(final String welcomeChannelID) {
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
    public boolean setGoodbyeChannelID(final String goodbyeChannelID) {
        if (GuildHandler.setGoodbyeChannelID(guildID, goodbyeChannelID)) {
            customChannelIDs.put(CustomChannel.GOODBYE, goodbyeChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The twitch channels for the {@link Guild}.
     */
    public ArrayList<String> getTwitchChannels() {
        return twitchChannels;
    }

    /**
     * @return The birthday {@link TextChannel}.
     */
    public Optional<TextChannel> getBirthdayChannel() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.BIRTHDAY)));
    }

    /**
     * Set the birthday {@link TextChannel} for the {@link Guild}.
     *
     * @param birthdayChannelID The ID of the specified {@link TextChannel}.
     * @return True, if setting the birthday {@link TextChannel} was successful.
     */
    public boolean setBirthdayChannelID(final String birthdayChannelID) {
        if (GuildHandler.setBirthdayChannelID(guildID, birthdayChannelID)) {
            customChannelIDs.put(CustomChannel.BIRTHDAY, birthdayChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The {@link Raffle Raffle} {@link TextChannel} for the {@link Guild}.
     */
    public Optional<TextChannel> getRaffleChannel() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.RAFFLE)));
    }

    /**
     * Sets the {@link Raffle Raffle} {@link TextChannel} for the {@link Guild}.
     *
     * @param raffleChannelID The ID of the {@link TextChannel}.
     * @return True, if setting it was successful.
     */
    public boolean setRaffleChannel(final String raffleChannelID) {
        if (GuildHandler.setRaffleChannelID(guildID, raffleChannelID)) {
            customChannelIDs.put(CustomChannel.RAFFLE, raffleChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The poll {@link TextChannel} for the {@link Guild}.
     */
    public Optional<TextChannel> getPollChannel() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.POLL)));
    }

    /**
     * Set the {@link Poll Poll} {@link TextChannel} for the {@link Guild}.
     *
     * @param pollChannelID The ID of the {@link TextChannel}.
     * @return True, if setting the poll {@link TextChannel} was successful.
     */
    public boolean setPollChannel(final String pollChannelID) {
        if (GuildHandler.setPollChannelID(guildID, pollChannelID)) {
            customChannelIDs.put(CustomChannel.POLL, pollChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The counting {@link TextChannel} for the {@link Guild}.
     */
    public Optional<TextChannel> getCountingChannel() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.COUNTING)));
    }

    /**
     * Set the Counting {@link TextChannel} for the {@link Guild}.
     *
     * @param countingChannelID The ID of the {@link TextChannel} used for counting.
     * @return True, if setting the counting {@link TextChannel} was successful.
     */
    public boolean setCountingChannel(final String countingChannelID) {
        if (GuildHandler.setCountingChannelID(guildID, countingChannelID)) {
            customChannelIDs.put(CustomChannel.COUNTING, countingChannelID);
            return true;
        }
        return false;
    }

    /**
     * @return The update {@link TextChannel} for the {@link Guild}.
     */
    public Optional<TextChannel> getUpdateChannel() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getTextChannelById(customChannelIDs.get(CustomChannel.UPDATE)));
    }

    /**
     * Set the Bot Update {@link TextChannel} for the {@link Guild}.
     *
     * @param updateChannelID The ID of the {@link TextChannel} used for sending bot updates.
     * @return True, if setting the update {@link TextChannel} was successful.
     */
    public boolean setUpdateChannel(final String updateChannelID) {
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
    public boolean setNotifyOnUpdate(final boolean answer) {
        if (GuildHandler.setNotifyOnUpdate(guildID, answer)) {
            notifyOnUpdate = answer;
            return true;
        }
        return false;
    }

    /**
     * @return The current state of if the {@link Guild} should be notified on an update.
     */
    public boolean getNotifyOnUpdate() {
        return notifyOnUpdate;
    }

    /**
     * Sets the Live Notifications {@link Role} ID for the {@link Guild}.
     *
     * @param roleID The ID for the Live Notifications {@link Role}.
     * @return True, if it was successful.
     */
    public boolean setLiveNotificationsRoleID(final String roleID) {
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
    public Optional<Role> getLiveNotificationsRole() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getRoleById(liveNotificationsRoleID));
    }

    /**
     * @return The {@link String} ID of the live channel to send messages.
     */
    public String getLiveChannelID() {
        return customChannelIDs.get(CustomChannel.LIVE);
    }

    /**
     * Update the muted {@link Role} for the {@link Guild}.
     *
     * @param mutedRoleID The ID of the muted {@link Role}.
     * @return True, if the {@link Role} was successfully updated in the database.
     */
    public boolean setMutedRoleID(final String mutedRoleID) {
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
    public boolean setModeratorRoleID(final String moderatorRoleID) {
        if (GuildHandler.setModeratorRoleID(guildID, moderatorRoleID)) {
            this.moderatorRoleID = moderatorRoleID;
            return true;
        }
        return false;
    }

    /**
     * Sets the prefix for the {@link CustomGuild}.
     *
     * @param newPrefix The new prefix.
     * @return True, if setting the prefix was successful.
     */
    public boolean setPrefix(final String newPrefix) {
        if (GuildHandler.setPrefix(guildID, newPrefix)) {
            this.prefix = newPrefix;
            return true;
        }
        return false;
    }

    /**
     * @return The muted {@link Role} for the current {@link Guild}.
     */
    public Optional<Role> getMutedRole() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getRoleById(mutedRoleID));
    }

    /**
     * @return The moderator {@link Role} for the current {@link Guild}.
     */
    public Optional<Role> getModeratorRole() {
        return Optional.ofNullable(GuildHandler.getGuild(guildID).getRoleById(moderatorRoleID));
    }

    /**
     * Add a twitch channel to the {@link Guild}.
     * @param twitchChannel The name of the twitch channel to add.
     * @return True, if the twitch channel was successfully added.
     */
    public boolean addTwitchChannel(String twitchChannel) {
        twitchChannel = twitchChannel.toLowerCase();

        // Check if the channel exists.
        if (!TwitchHandler.getTwitchListener().channelExists(twitchChannel)) {
            Bot.getCafeAPI().getTwitchEndpoint().removeGuildTwitch(guildID, twitchChannel);
            return false;
        }

        if (twitchChannels.contains(twitchChannel)) return false;

        if (GuildHandler.addTwitchChannel(guildID, twitchChannel)) {
            twitchChannels.add(twitchChannel.toLowerCase());

            try {
                TwitchHandler.addTwitchChannel(twitchChannel);
                return TwitchHandler.addCache(guildID, twitchChannel);
            } catch (HystrixRuntimeException | ContextedRuntimeException e) {

                // If this is reached, it means the twitch channel does not exist.
                Bot.getCafeAPI().getTwitchEndpoint().removeGuildTwitch(guildID, twitchChannel);
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
    public boolean removeTwitchChannel(String twitchChannel) {
        twitchChannel = twitchChannel.toLowerCase();

        if (!twitchChannels.contains(twitchChannel)) return false;

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
    public boolean updateTwitchDiscordChannel(final String liveChannelID) {
        if (GuildHandler.setTwitchChannelID(guildID, liveChannelID)) {
            customChannelIDs.put(CustomChannel.LIVE, liveChannelID);
            return true;
        }
        return false;
    }

    /**
     * Check is a specified {@link String channelID} is already a {@link CustomChannel}.
     * @param channelID The {@link String channelID} to search for.
     * @return True, if the {@link String channelID} is a {@link CustomChannel}.
     */
    public boolean isCustomChannel(final String channelID) {
        return customChannelIDs.entrySet().stream().anyMatch((entry) -> entry.getValue().equals(channelID));
    }

    /**
     * Check if a specified {@link String channelID} is a {@link CustomChannel}.
     * @param channelID The {@link String channelID} to search for.
     * @return True, if the {@link String channelID} is a {@link CustomChannel}.
     */
    public boolean isDailyChannel(final String channelID) {
        return customChannelIDs.get(CustomChannel.DAILY).equalsIgnoreCase(channelID);
    }

}
