package com.beanbeanjuice.utility.guild;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.lavaplayer.GuildMusicManager;
import com.beanbeanjuice.utility.lavaplayer.PlayerManager;
import com.beanbeanjuice.utility.twitch.Twitch;
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

    private String guildID;
    private String prefix;
    private String moderatorRoleID;
    private String liveChannelID;
    private ArrayList<String> twitchChannels;
    private String mutedRoleID;
    private String liveNotificationsRoleID;
    private Boolean notifyOnUpdate;
    private String updateChannelID;
    private String countingChannelID;
    private String pollChannelID;
    private String raffleChannelID;

    private Timer timer;
    private TimerTask timerTask;
    private TextChannel lastMusicChannel;

    private ArrayList<TextChannel> deletingMessagesChannels;

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
     * @param pollChannelID The ID of the {@link TextChannel} being used for {@link com.beanbeanjuice.utility.poll.Poll Poll}s.
     */
    public CustomGuild(@NotNull String guildID, @NotNull String prefix, @NotNull String moderatorRoleID,
                       @NotNull String liveChannelID, @NotNull ArrayList<String> twitchChannels, @NotNull String mutedRoleID,
                       @NotNull String liveNotificationsRoleID, @NotNull Boolean notifyOnUpdate, @NotNull String updateChannelID,
                       @NotNull String countingChannelID, @NotNull String pollChannelID, @NotNull String raffleChannelID) {
        this.guildID = guildID;
        this.prefix = prefix;
        this.moderatorRoleID = moderatorRoleID;
        this.liveChannelID = liveChannelID;
        this.twitchChannels = twitchChannels;
        this.mutedRoleID = mutedRoleID;
        this.liveNotificationsRoleID = liveNotificationsRoleID;
        this.notifyOnUpdate = notifyOnUpdate;
        this.updateChannelID = updateChannelID;
        this.countingChannelID = countingChannelID;
        this.pollChannelID = pollChannelID;
        this.raffleChannelID = raffleChannelID;

        // Checks if a Listener has already been created for that guild.
        // This is so that if the cache is reloaded, it does not need to recreate the Listeners.
        if (CafeBot.getTwitchHandler().getTwitch(guildID) == null) {
            CafeBot.getTwitchHandler().addTwitchToGuild(guildID, new Twitch(this.guildID, this.twitchChannels));
        }

        deletingMessagesChannels = new ArrayList<>();

    }

    @Nullable
    public TextChannel getRaffleChannel() {
        try {
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(raffleChannelID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @NotNull
    public Boolean setRaffleChannel(@NotNull String raffleChannelID) {
        if (CafeBot.getGuildHandler().setRaffleChannelID(guildID, raffleChannelID)) {
            this.raffleChannelID = raffleChannelID;
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
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(pollChannelID);
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
            this.pollChannelID = pollChannelID;
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
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(countingChannelID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Set the Counting {@link TextChannel} for the {@link Guild}.
     * @param countingChannel The {@link TextChannel} used for counting.
     * @return Whether or not setting the counting {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setCountingChannel(@NotNull TextChannel countingChannel) {
        if (CafeBot.getGuildHandler().setCountingChannelID(guildID, countingChannel.getId())) {
            this.countingChannelID = countingChannel.getId();
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
            return CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(updateChannelID);
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
            this.updateChannelID = updateChannelID;
            return true;
        }
        return false;
    }

    /**
     * Set the Bot Update Channel for the {@link Guild}.
     * @param textChannel The {@link TextChannel} used for sending bot updates.
     * @return Whether or not setting the update {@link TextChannel} was successful.
     */
    @NotNull
    public Boolean setUpdateChannel(@NotNull TextChannel textChannel) {
        return setUpdateChannel(textChannel.getId());
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
        return liveChannelID;
    }

    /**
     * Sets the last channel the music commands were sent in.
     * @param lastMusicChannel The {@link TextChannel} of the last music command.
     */
    public void setLastMusicChannel(TextChannel lastMusicChannel) {
        this.lastMusicChannel = lastMusicChannel;
    }

    /**
     * Starts checking for an {@link net.dv8tion.jda.internal.audio.AudioConnection AudioConnection} in the current {@link Guild}.
     */
    public void startAudioChecking() {
        timer = new Timer();
        final int[] seconds = {0};
        int secondsToLeave = 300;
        timerTask = new TimerTask() {

            @Override
            public void run() {
                boolean voicePassed = false;
                boolean queuePassed = false;
                seconds[0] += 1;
                Guild guild = CafeBot.getGuildHandler().getGuild(guildID);
                Member selfMember = guild.getSelfMember();
                GuildVoiceState selfVoiceState = selfMember.getVoiceState();

                ArrayList<Member> membersInVoiceChannel = new ArrayList<>(selfVoiceState.getChannel().getMembers());
                membersInVoiceChannel.remove(selfMember);
                GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(CafeBot.getGuildHandler().getGuild(guildID));

                // Checking if the bot is alone in the VC.
                if (membersInVoiceChannel.isEmpty() && seconds[0] >= secondsToLeave) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setAuthor("Music Bot");
                    embedBuilder.setDescription("Leaving the voice channel as it is empty...");
                    embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
                    sendMessageInLastMusicChannel(embedBuilder.build());
                    musicManager.scheduler.player.stopTrack();
                    musicManager.scheduler.queue.clear();
                    guild.getAudioManager().closeAudioConnection();
                    timer.cancel();
                    return;
                }

                // This means that they are in a Voice Channel.
                voicePassed = !membersInVoiceChannel.isEmpty();

                // Checks if the bot is currently playing something and if the queue is empty.
                if (musicManager.scheduler.queue.isEmpty() && musicManager.audioPlayer.getPlayingTrack() == null && seconds[0] >= secondsToLeave) {
                    guild.getAudioManager().closeAudioConnection();
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setAuthor("Music Bot");
                    embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
                    embedBuilder.setDescription("Leaving the voice channel as the music queue is empty...");
                    sendMessageInLastMusicChannel(embedBuilder.build());
                    guild.getAudioManager().closeAudioConnection();
                    timer.cancel();
                    return;
                }

                // This means that there are songs in the queue and a song is currently playing.
                if (!musicManager.scheduler.queue.isEmpty() || musicManager.audioPlayer.getPlayingTrack() != null) {
                    queuePassed = true;
                }

                if (voicePassed && queuePassed) {
                    seconds[0] = 0;
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    /**
     * Sends a message in the specified last music {@link TextChannel}.
     * @param embed The {@link MessageEmbed} to be sent.
     */
    private void sendMessageInLastMusicChannel(MessageEmbed embed) {
        try {
            lastMusicChannel.sendMessage(embed).queue();
        } catch (NullPointerException ignored) {}
    }

    /**
     * Stops checking for an {@link net.dv8tion.jda.internal.audio.AudioConnection AudioConnection} in the current {@link Guild}.
     */
    public void stopAudioChecking() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * @return The {@link Twitch} associated with the {@link Guild}.
     */
    @NotNull
    public Twitch getTwitch() {
        return CafeBot.getTwitchHandler().getTwitch(guildID);
    }

    /**
     * Update the muted {@link Role} for the {@link Guild}.
     * @param mutedRoleID The ID of the muted {@link Role}.
     * @return Whether or not the {@link Role} was successfully updated in the database.
     */
    @NotNull
    public Boolean updateMutedRole(String mutedRoleID) {

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
            CafeBot.getTwitchHandler().getTwitch(guildID).getTwitchChannelNamesHandler().addTwitchChannelName(twitchChannel);
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
            CafeBot.getTwitchHandler().getTwitch(guildID).getTwitchChannelNamesHandler().removeTwitchChannelName(twitchChannel);
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
            this.liveChannelID = liveChannelID;
            return true;
        }
        return false;
    }

}
