package com.beanbeanjuice.cafebot.utility.handler.guild;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class used for handling {@link Guild Guilds}.
 *
 * @author beanbeanjuice
 */
public class GuildHandler {

    private static HashMap<String, CustomGuild> guildDatabase;
    private static HashMap<String, ArrayList<String>> twitchChannels;

    /**
     * Start the {@link GuildHandler}.
     */
    public static void start() {
        guildDatabase = new HashMap<>();
        getAllTwitchChannels();

        checkGuilds();
    }

    private static void getAllTwitchChannels() {
        twitchChannels = Bot.getCafeAPI().TWITCH.getAllTwitches();
    }

    /**
     * Updates the current {@link Guild} cache.
     */
    public static void updateGuildCache() {
        guildDatabase.clear();

        try {
            Bot.getCafeAPI().GUILD.getAllGuildInformation().forEach((guildID, guildInformation) -> {
                String prefix = guildInformation.getSetting(GuildInformationType.PREFIX);
                String moderationRoleID = guildInformation.getSetting(GuildInformationType.MODERATOR_ROLE_ID);
                String twitchChannelID = guildInformation.getSetting(GuildInformationType.TWITCH_CHANNEL_ID);

                ArrayList<String> twitchChannelsInGuild = twitchChannels.get(guildID);
                if (twitchChannelsInGuild == null) {
                    twitchChannelsInGuild = new ArrayList<>();
                }
                String mutedRoleID = guildInformation.getSetting(GuildInformationType.MUTED_ROLE_ID);
                String liveNotificationsRoleID = guildInformation.getSetting(GuildInformationType.LIVE_NOTIFICATIONS_ROLE_ID);
                boolean notifyOnUpdate = Boolean.parseBoolean(guildInformation.getSetting(GuildInformationType.NOTIFY_ON_UPDATE));
                String updateChannelID = guildInformation.getSetting(GuildInformationType.UPDATE_CHANNEL_ID);
                String countingChannelID = guildInformation.getSetting(GuildInformationType.COUNTING_CHANNEL_ID);
                String pollChannelID = guildInformation.getSetting(GuildInformationType.POLL_CHANNEL_ID);
                String raffleChannelID = guildInformation.getSetting(GuildInformationType.RAFFLE_CHANNEL_ID);
                String birthdayChannelID = guildInformation.getSetting(GuildInformationType.BIRTHDAY_CHANNEL_ID);
                String welcomeChannelID = guildInformation.getSetting(GuildInformationType.WELCOME_CHANNEL_ID);
                String goodbyeChannelID = guildInformation.getSetting(GuildInformationType.GOODBYE_CHANNEL_ID);
                String logChannelID = guildInformation.getSetting(GuildInformationType.LOG_CHANNEL_ID);
                String ventingChannelID = guildInformation.getSetting(GuildInformationType.VENTING_CHANNEL_ID);
                boolean aiState = Boolean.parseBoolean(guildInformation.getSetting(GuildInformationType.AI_RESPONSE));
                String dailyChannelID = guildInformation.getSetting(GuildInformationType.DAILY_CHANNEL_ID);

                guildDatabase.put(guildID, new CustomGuild(
                        guildID, prefix, moderationRoleID,
                        twitchChannelID, twitchChannelsInGuild, mutedRoleID,
                        liveNotificationsRoleID, notifyOnUpdate, updateChannelID,
                        countingChannelID, pollChannelID, raffleChannelID,
                        birthdayChannelID, welcomeChannelID, goodbyeChannelID,
                        logChannelID, ventingChannelID, aiState,
                        dailyChannelID
                ));
            });
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.ERROR, "Error Updating Custom Guild Cache: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves an {@link ArrayList} of {@link String twitchChannelName} for a specified {@link String guildID}.
     *
     * @param guildID The specified {@link String guildID}.
     * @return The {@link ArrayList} of {@link String twitchChannelName} associated with the specified {@link String guildID}.
     */
    @NotNull
    public static  ArrayList<String> getTwitchChannels(@NotNull String guildID) {
        try {
            return Bot.getCafeAPI().TWITCH.getGuildTwitches(guildID);
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.ERROR, "Error Retrieving Guild Twitch: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Sets the {@link Boolean aiState} of the {@link String guildID}.
     *
     * @param guildID The specified {@link String guildID}.
     * @param aiState The new {@link Boolean aiState}.
     * @return True, if the {@link Boolean aiState} was updated successfully.
     */
    @NotNull
    protected static Boolean setAIState(@NotNull String guildID, @NotNull Boolean aiState) {
        try {
            return Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.AI_RESPONSE, aiState);
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating AI Response Status: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String dailyChannelID} for the specified {@link String guildID}.
     *
     * @param guildID        The specified {@link String guildID}.
     * @param dailyChannelID The new {@link String dailyChannelID}.
     * @return True, if the {@link String dailyChannelID} was successfully updated.
     */
    @NotNull
    protected static Boolean setDailyChannelID(@NotNull String guildID, @NotNull String dailyChannelID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.DAILY_CHANNEL_ID, dailyChannelID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Daily Channel ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String birthdayChannelID} for the specified {@link String guildID}.
     *
     * @param guildID           The specified {@link String guildID}.
     * @param birthdayChannelID The new {@link String birthdayChannelID}.
     * @return True, if the {@link String birthdayChannelID} was successfully updated.
     */
    @NotNull
    protected static Boolean setBirthdayChannelID(@NotNull String guildID, @NotNull String birthdayChannelID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.BIRTHDAY_CHANNEL_ID, birthdayChannelID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Birthday Channel ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String raffleChannelID} for the specified {@link String guildID}.
     *
     * @param guildID         The specified {@link String guildID}.
     * @param raffleChannelID The new {@link String raffleChannelID}.
     * @return True, if the {@link String raffleChannelID} was successfully updated.
     */
    @NotNull
    protected static Boolean setRaffleChannelID(@NotNull String guildID, @NotNull String raffleChannelID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.RAFFLE_CHANNEL_ID, raffleChannelID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Raffle Channel ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String pollChannelID} for the specified {@link String guildID}.
     *
     * @param guildID       The specified {@link String guildID}.
     * @param pollChannelID The new {@link String pollChannelID}.
     * @return True, if the {@link String pollChannelID} was successfully updated.
     */
    @NotNull
    protected static Boolean setPollChannelID(@NotNull String guildID, @NotNull String pollChannelID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.POLL_CHANNEL_ID, pollChannelID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Poll Channel ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String countingChannelID} for the specified {@link String guildID}.
     *
     * @param guildID           The specified {@link String guildID}.
     * @param countingChannelID The new {@link String countingChannelID}.
     * @return True, if the {@link String countingChannelID} was successfully updated.
     */
    @NotNull
    protected static Boolean setCountingChannelID(@NotNull String guildID, @NotNull String countingChannelID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.COUNTING_CHANNEL_ID, countingChannelID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Counting Channel ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String updateChannelID} for the specified {@link String guildID}.
     *
     * @param guildID         The specified {@link String guildID}.
     * @param updateChannelID The new {@link String countingChannelID}.
     * @return True, if the {@link String countingChannelID} was successfully updated.
     */
    @NotNull
    protected static Boolean setUpdateChannelID(@NotNull String guildID, @NotNull String updateChannelID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.UPDATE_CHANNEL_ID, updateChannelID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Counting Channel ID: " + e.getMessage(), e);
            return true;
        }
    }

    /**
     * Sets the {@link Boolean notifyOnUpdate} state for the specified {@link String guildID}.
     *
     * @param guildID        The specified {@link String guildID}.
     * @param notifyOnUpdate The new {@link Boolean notifyOnUpdate} state.
     * @return True, if the {@link Boolean notifyOnUpdate} state was successfully updated.
     */
    @NotNull
    protected static Boolean setNotifyOnUpdate(@NotNull String guildID, @NotNull Boolean notifyOnUpdate) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.NOTIFY_ON_UPDATE, notifyOnUpdate);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Notify On Update: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String liveNotificationsRoleID} for the specified {@link String guildID}.
     *
     * @param guildID                 The specified {@link String guildID}.
     * @param liveNotificationsRoleID The new {@link String liveNotificationsRoleID}.
     * @return True, if the {@link String liveNotificationsRoleID} was successfully updated.
     */
    @NotNull
    protected static Boolean setLiveNotificationsRoleID(@NotNull String guildID, @NotNull String liveNotificationsRoleID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.LIVE_NOTIFICATIONS_ROLE_ID, liveNotificationsRoleID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Live Notifications Role ID: " + e.getMessage(), e);
            return true;
        }
    }

    /**
     * Sets the {@link String prefix} for the specified {@link String guildID}.
     *
     * @param guildID The specified {@link String guildID}.
     * @param prefix  The new {@link String prefix}.
     * @return True, if the {@link String prefix} was successfully updated.
     */
    @NotNull
    protected static Boolean setPrefix(@NotNull String guildID, @NotNull String prefix) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.PREFIX, prefix);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Guild Prefix: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Checks all the current {@link Guild} in the database.
     */
    public static void checkGuilds() {
        updateGuildCache();

        List<Guild> guildsHasBot = Bot.getBot().getGuilds();
        ArrayList<String> guildsIDHasBot = new ArrayList<>();

        // Adds any guild that the bot is in but not in the database.
        for (Guild guild : guildsHasBot) {
            if (!guildDatabase.containsKey(guild.getId()))
                addGuild(guild);

            guildsIDHasBot.add(guild.getId());
        }

        // Checks the database for any guilds that the bot is no longer in.
        guildDatabase.forEach((k, v) -> {
            if (!guildsIDHasBot.contains(k))
                removeGuild(k);
        });

        updateGuildCache();
    }

    /**
     * Removes a specified {@link String guildID}.
     *
     * @param guildID The {@link String guildID} to remove.
     * @return True, if the {@link String guildID} was successfully removed.
     */
    @NotNull
    public static Boolean removeGuild(@NotNull String guildID) {
        try {
            Bot.getCafeAPI().GUILD.deleteGuildInformation(guildID);
            Bot.getCafeAPI().TWITCH.getGuildTwitches(guildID).forEach((channelName) -> {
                Bot.getCafeAPI().TWITCH.removeGuildTwitch(guildID, channelName);
            });
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.ERROR, "Error Removing Guild: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Removes a {@link Guild} from the database.
     *
     * @param guild The ID of the {@link Guild} to be removed.
     * @return True, if the {@link Guild} was removed successfully.
     */
    @NotNull
    public static Boolean removeGuild(@NotNull Guild guild) {
        if (removeGuild(guild.getId())) {
            guildDatabase.remove(guild.getId());
            return true;
        }
        return false;
    }

    /**
     * Adds a new {@link String guildID}.
     *
     * @param guildID The {@link String guildID} to add.
     * @return True, if the {@link String guildID} was successfully added.
     */
    @NotNull
    public static Boolean addGuild(@NotNull String guildID) {
        try {
            Bot.getCafeAPI().GUILD.createGuildInformation(guildID);

            // Create default guild.
            guildDatabase.put(guildID, new CustomGuild(guildID, "!!", "0",
                    "0", new ArrayList<>(), "0",
                    "0", true, "0",
                    "0", "0", "0",
                    "0", "0", "0",
                    "0", "0", false,
                    "0"));

            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.ERROR, "Error Adding Guild: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String logChannelID} for the specified {@link String guildID}.
     *
     * @param guildID      The specified {@link String guildID}.
     * @param logChannelID The new {@link String logChannelID}.
     * @return True, if the {@link String logChannelID} was successfully updated.
     */
    @NotNull
    protected static  Boolean setLogChannelID(@NotNull String guildID, @NotNull String logChannelID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.LOG_CHANNEL_ID, logChannelID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Log Channel ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String welcomeChannelID} for the specified {@link String guildID}.
     *
     * @param guildID          The specified {@link String guildID}.
     * @param welcomeChannelID The new {@link String welcomeChannelID}.
     * @return True, if the {@link String welcomeChannelID} was successfully updated.
     */
    @NotNull
    protected static Boolean setWelcomeChannelID(@NotNull String guildID, @NotNull String welcomeChannelID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.WELCOME_CHANNEL_ID, welcomeChannelID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Welcome Channel ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String goodbyeChannelID} for the specified {@link String guildID}.
     *
     * @param guildID The specified {@link String guildID}.
     * @param goodbyeChannelID The new {@link String goodbyeChannelID}.
     * @return True, if the {@link String goodbyeChannelID} was successfully updated.
     */
    @NotNull
    protected static Boolean setGoodbyeChannelID(@NotNull String guildID, @NotNull String goodbyeChannelID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.GOODBYE_CHANNEL_ID, goodbyeChannelID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Goodbye Channel ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String ventingChannelID} for the specified {@link String guildID}.
     *
     * @param guildID          The specified {@link String guildID}.
     * @param ventingChannelID The new {@link String ventingChannelID}.
     * @return True, if the {@link String ventingChannelID} was successfully updated.
     */
    @NotNull
    protected static Boolean setVentingChannelID(@NotNull String guildID, @NotNull String ventingChannelID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.VENTING_CHANNEL_ID, ventingChannelID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Venting Channel ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String mutedRoleID} for the specified {@link String guildID}.
     *
     * @param guildID     The specified {@link String guildID}.
     * @param mutedRoleID The new {@link String mutedRoleID}.
     * @return True, if the {@link String mutedRoleID} was successfully updated.
     */
    @NotNull
    protected static Boolean setMutedRoleID(@NotNull String guildID, @NotNull String mutedRoleID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.MUTED_ROLE_ID, mutedRoleID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Muted Role ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sets the {@link String moderatorRoleID} for the specified {@link String guildID}.
     *
     * @param guildID         The specified {@link String guildID}.
     * @param moderatorRoleID The new {@link String moderatorRoleID}.
     * @return True, if the {@link String moderatorRoleID} was successfully updated.
     */
    @NotNull
    protected static Boolean setModeratorRoleID(@NotNull String guildID, @NotNull String moderatorRoleID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.MODERATOR_ROLE_ID, moderatorRoleID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Moderator Role ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Adds a {@link String twitchChannelName} to a specified {@link String guildID}.
     *
     * @param guildID           The specified {@link String guildID}.
     * @param twitchChannelName The {@link String twitchChannelName} to add.
     * @return True, if the {@link String twitchChannelName} was successfully added.
     */
    @NotNull
    protected static Boolean addTwitchChannel(@NotNull String guildID, @NotNull String twitchChannelName) {
        try {
            Bot.getCafeAPI().TWITCH.addGuildTwitch(guildID, twitchChannelName);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Adding Twitch Channel to Guild: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Removes a {@link String twitchChannelName} from a specified {@link String guildID}.
     *
     * @param guildID           The specified {@link String guildID}.
     * @param twitchChannelName The {@link String twitchChannelName} to remove.
     * @return True, if the {@link String twitchChannelName} was successfully removed.
     */
    @NotNull
    protected static Boolean removeTwitchChannel(@NotNull String guildID, @NotNull String twitchChannelName) {
        try {
            Bot.getCafeAPI().TWITCH.removeGuildTwitch(guildID, twitchChannelName);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Removing Twitch Channel from Guild: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sets the {@link String twitchChannelID} for the specified {@link String guildID}.
     *
     * @param guildID         The specified {@link String guildID}.
     * @param twitchChannelID The new {@link String twitchChannelID}.
     * @return True, if the {@link String twitchChannelID} was successfully updated.
     */
    @NotNull
    protected static Boolean setTwitchChannelID(@NotNull String guildID, @NotNull String twitchChannelID) {
        try {
            Bot.getCafeAPI().GUILD.updateGuildInformation(guildID, GuildInformationType.TWITCH_CHANNEL_ID, twitchChannelID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(GuildHandler.class, LogLevel.WARN, "Error Updating Twitch Channel ID: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Adds a {@link Guild} to the database.
     *
     * @param guild The {@link Guild} to be added.
     * @return True, if the {@link Guild} was added successfully.
     */
    @NotNull
    public static Boolean addGuild(@NotNull Guild guild) {
        return addGuild(guild.getId());
    }

    /**
     * Gets a {@link CustomGuild} from its ID.
     *
     * @param guildID The ID of the {@link CustomGuild}.
     * @return The {@link CustomGuild}.
     */
    @NotNull
    public static CustomGuild getCustomGuild(@NotNull String guildID) {
        return guildDatabase.get(guildID);
    }

    /**
     * Gets a {@link CustomGuild}.
     *
     * @param guild The {@link Guild} of the {@link CustomGuild}.
     * @return The {@link CustomGuild}.
     */
    @NotNull
    public static CustomGuild getCustomGuild(@NotNull Guild guild) {
        return getCustomGuild(guild.getId());
    }

    /**
     * Gets a {@link Guild} by its ID.
     *
     * @param guildID The ID of the {@link Guild}.
     * @return The {@link Guild}.
     */
    @NotNull
    public static Guild getGuild(@NotNull String guildID) {
        return Bot.getBot().getGuildById(guildID);
    }

    /**
     * Gets the {@link GuildHandler} database cache.
     *
     * @return The {@link HashMap} containing the database cache.
     */
    @NotNull
    public static HashMap<String, CustomGuild> getGuilds() {
        return guildDatabase;
    }

    /**
     * Check if the bot is in a specified guild.
     * @param guildID The {@link String guildID}.
     * @return True, if the bot is in the guild.
     */
    @NotNull
    public static Boolean guildContainsBot(@NotNull String guildID) {
        return guildDatabase.containsKey(guildID) && Bot.getBot().getGuildById(guildID) != null;
    }

}
