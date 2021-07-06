package com.beanbeanjuice.utility.sections.music.lavaplayer;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class used for handling the music playing.
 */
public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    /**
     * Creates a new instance of the {@link PlayerManager} object.
     */
    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    /**
     * Gets the music manager for the {@link Guild} specified.
     * @param guild The {@link Guild} that has music playing.
     * @return The {@link GuildMusicManager}.
     */
    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildID) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager, guild);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    /**
     * A method used for loading and playing music.
     * @param trackURL The link for the track to be played.
     * @param guild The {@link Guild} for the music.
     */
    public void loadAndPlay(String trackURL, Guild guild) {
        final GuildMusicManager musicManager = this.getMusicManager(guild);

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                if (trackURL.startsWith("ytsearch:")) {
                    trackLoaded(audioPlaylist.getTracks().get(0));
                }
            }

            @Override
            public void noMatches() {
                try {
                    CafeBot.getGuildHandler().getCustomGuild(guild).getLastMusicChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                            "Error Playing Song",
                            "There was an error playing: `" + trackURL + "`."
                    )).queue();
                } catch (NullPointerException ignored) {}
            }

            @Override
            public void loadFailed(FriendlyException e) {
                try {
                    CafeBot.getGuildHandler().getCustomGuild(guild).getLastMusicChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                            "Error Playing Song",
                            "There has been a catastrophic error playing: `" + trackURL + "`.\n\n**ERROR**: " + e.getMessage()
                    )).queue();
                } catch (NullPointerException ignored) {}

                CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Catastrophic Error for URL: " + trackURL + "\nError Message: " + e.getMessage(), false, false, e);
            }
        });
    }

    /**
     * @return An instance of the {@link PlayerManager}.
     */
    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

}