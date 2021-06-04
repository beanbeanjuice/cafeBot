package com.beanbeanjuice.utility.sections.music.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;

/**
 * A class used for managing the {@link net.dv8tion.jda.api.entities.Guild Guild} music.
 */
public class GuildMusicManager {

    public final AudioPlayer audioPlayer;
    public final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    /**
     * Creates a new instance of the {@link GuildMusicManager} object.
     * @param manager The {@link AudioPlayerManager} to be used with the {@link AudioPlayerManager}.
     */
    public GuildMusicManager(AudioPlayerManager manager, Guild guild) {
        this.audioPlayer = manager.createPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer, guild);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new AudioPlayerSendHandler(audioPlayer);
    }

    /**
     * @return The {@link AudioPlayerSendHandler} to be used.
     */
    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }

}