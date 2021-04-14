package com.beanbeanjuice.utility.lavaplayer;

import com.beanbeanjuice.main.BeanBot;
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
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    /**
     * A method used for loading and playing music.
     * @param channel The {@link TextChannel} that received the song playing message.
     * @param trackURL The link for the track to be played.
     */
    public void loadAndPlay(TextChannel channel, String trackURL, boolean isSpotifyPlaylist) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {

                if (isSpotifyPlaylist) {
                    musicManager.scheduler.queue(audioTrack);
                    return;
                }

                musicManager.scheduler.queue(audioTrack);

                BeanBot.getLogManager().log(this.getClass(), LogLevel.TEST, audioTrack.getIdentifier());
                BeanBot.getLogManager().log(this.getClass(), LogLevel.TEST, audioTrack.getInfo().identifier);
                BeanBot.getLogManager().log(this.getClass(), LogLevel.TEST, "" + audioTrack.getInfo().isStream);
                BeanBot.getLogManager().log(this.getClass(), LogLevel.TEST, audioTrack.getInfo().uri);

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("Added Song to Queue");

                String message = "`" +
                        audioTrack.getInfo().title +
                        "` by `" +
                        audioTrack.getInfo().author +
                        "`";
                embedBuilder.setDescription(message);
                embedBuilder.setColor(Color.green);

                channel.sendMessage(embedBuilder.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                if (trackURL.startsWith("ytsearch:")) {
                    trackLoaded(tracks.get(0));
                    return;
                }

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("Added Playlist to Queue");
                embedBuilder.addField("Tracks", String.valueOf(tracks.size()), true);
                embedBuilder.addField("Track Name", "`" + audioPlaylist.getName() + "`", true);
                embedBuilder.setColor(Color.green);
                channel.sendMessage(embedBuilder.build()).queue();

                for (final AudioTrack track : tracks) {
                    musicManager.scheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {

                if (isSpotifyPlaylist && trackURL.contains("ytsearch:")) {
                    loadAndPlay(channel, trackURL, true);
                    BeanBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Getting Song from Playlist", false, false);
                    return;
                }

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setDescription("No matches found.");
                embedBuilder.setColor(Color.red);
                embedBuilder.setFooter("The link provided may not be a video: " + trackURL);
                channel.sendMessage(embedBuilder.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setDescription("Video failed to load.");
                embedBuilder.setColor(Color.red);
                embedBuilder.setFooter("The link provided may not be a video: " + trackURL);
                channel.sendMessage(embedBuilder.build()).queue();
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