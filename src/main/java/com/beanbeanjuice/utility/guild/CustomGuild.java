package com.beanbeanjuice.utility.guild;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.listener.twitch.TwitchChannelNamesHandler;
import com.beanbeanjuice.utility.listener.twitch.TwitchListener;
import com.beanbeanjuice.utility.listener.twitch.TwitchMessageEventHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A {@link CustomGuild} that contains {@link net.dv8tion.jda.api.entities.Guild Guild} information.
 *
 * @author beanbeanjuice
 */
public class CustomGuild {

    private String guildID;
    private String prefix;
    private String moderatorRoleID;
    private String liveChannelID;
    private ArrayList<String> twitchChannels;
    private String mutedRoleID;

    private TwitchListener twitchListener;
    private TwitchChannelNamesHandler twitchChannelNamesHandler;
    private TwitchMessageEventHandler twitchMessageEventHandler;

    /**
     * Creates a new {@link CustomGuild} object.
     * @param guildID The ID of the {@link Guild}.
     * @param prefix The prefix the bot will use for the {@link Guild}.
     * @param moderatorRoleID The ID of the moderator {@link Role} for the {@link Guild}.
     * @param liveChannelID The ID of the {@link TextChannel} to send twitch notifications to in the {@link Guild}.
     * @param twitchChannels The {@link ArrayList<String>} of twitch channels for the {@link Guild}.
     * @param mutedRoleID The ID of the muted {@link Role} for the {@link Guild}.
     */
    public CustomGuild(@NotNull String guildID, @NotNull String prefix, @NotNull String moderatorRoleID, @NotNull String liveChannelID,
                       @NotNull String twitchChannels, @NotNull String mutedRoleID) {
        this.guildID = guildID;
        this.prefix = prefix;
        this.moderatorRoleID = moderatorRoleID;
        this.liveChannelID = liveChannelID;
        this.twitchChannels = new ArrayList<>(Arrays.asList(twitchChannels.split(",")));
        this.mutedRoleID = mutedRoleID;

        twitchListener = new TwitchListener();
        twitchChannelNamesHandler = new TwitchChannelNamesHandler(this);

        twitchListener.addEventHandler(new TwitchMessageEventHandler(guildID, liveChannelID));

        if (!this.twitchChannels.isEmpty()) {
            for (String channel : this.twitchChannels) {
                twitchChannelNamesHandler.addTwitchChannelName(channel);
            }
        }

    }

    /**
     * @return The {@link TwitchListener} for the specified {@link Guild}.
     */
    @NotNull
    public TwitchListener getTwitchListener() {
        return twitchListener;
    }

    /**
     * @return The {@link TwitchChannelNamesHandler} for the specified {@link Guild}.
     */
    @NotNull
    public TwitchChannelNamesHandler getTwitchChannelNamesHandler() {
        return twitchChannelNamesHandler;
    }

    /**
     * Update the muted {@link Role} for the {@link Guild}.
     * @param mutedRoleID The ID of the muted {@link Role}.
     * @return Whether or not the {@link Role} was successfully updated in the database.
     */
    @NotNull
    public Boolean updateMutedRole(String mutedRoleID) {
        return BeanBot.getGuildHandler().updateGuildMutedRole(guildID, mutedRoleID);
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
        return BeanBot.getGuildHandler().updateGuildPrefix(guildID, newPrefix);
    }

    /**
     * @return The muted {@link Role} for the current {@link Guild}.
     */
    @Nullable
    public Role getMutedRole() {
        try {
            return BeanBot.getGuildHandler().getGuild(guildID).getRoleById(mutedRoleID);
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
            return BeanBot.getGuildHandler().getGuild(guildID).getRoleById(moderatorRoleID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Checks if the {@link CustomGuild} contains the specified twitch channel.
     * @param twitchChannel The twitch channel specified/
     * @return Whether or not the {@link CustomGuild} contains the twitch channel.
     */
    @NotNull
    public Boolean containsChannel(String twitchChannel) {

        for (String channel : twitchChannels) {

            if (channel.equalsIgnoreCase(twitchChannel)) {
                return true;
            }

        }

        return false;

    }

    /**
     * Add a twitch channel to the {@link Guild}.
     * @param twitchChannel The name of the twitch channel to add.
     * @return Whether or not the twitch channel was successfully added.
     */
    @NotNull
    public Boolean addTwitchChannel(String twitchChannel) {
        twitchChannels.add(twitchChannel.toLowerCase());

        StringBuilder stringBuilder = new StringBuilder();

        for (String string : twitchChannels) {
            stringBuilder.append(string).append(",");
        }

        return BeanBot.getGuildHandler().updateTwitchChannels(guildID, stringBuilder.toString());

    }

    /**
     * Removes a twitch channel from the {@link Guild}.
     * @param twitchChannel The name of the twitch channel to be removed.
     * @return Whether or not the twitch channel was successfully removed.
     */
    @NotNull
    public Boolean removeTwitchChannel(String twitchChannel) {

        twitchChannels.remove(twitchChannel.toLowerCase());

        StringBuilder stringBuilder = new StringBuilder();

        for (String string : twitchChannels) {
            stringBuilder.append(string).append(",");
        }

        return BeanBot.getGuildHandler().updateTwitchChannels(guildID, stringBuilder.toString());

    }

    /**
     * Update the twitch channel for the {@link CustomGuild}.
     * @param newLiveChannelID The new channel ID for the channel.
     * @return Whether or not the channel was successfully updated.
     */
    @NotNull
    public Boolean updateTwitchChannel(String newLiveChannelID) {
        return BeanBot.getGuildHandler().updateTwitchChannel(guildID, newLiveChannelID);
    }

}
