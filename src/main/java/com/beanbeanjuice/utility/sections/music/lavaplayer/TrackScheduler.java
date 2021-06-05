package com.beanbeanjuice.utility.sections.music.lavaplayer;

import com.beanbeanjuice.CafeBot;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A class used for managing tracks.
 */
public class TrackScheduler extends AudioEventAdapter {

    public final AudioPlayer player;
    private Guild guild;
    public BlockingQueue<AudioTrack> queue;
    public boolean inVoiceChannel = false;

    public TrackScheduler(AudioPlayer player, Guild guild) {
        this.player = player;
        this.guild = guild;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * This goes to next {@link AudioTrack} once the current {@link AudioTrack} has ended.
     * @param player The {@link AudioPlayer} handling the {@link AudioTrack}.
     * @param track The {@link AudioTrack} being used.
     * @param endReason The {@link AudioTrackEndReason} for the {@link AudioTrack}.
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (CafeBot.getGuildHandler().getCustomGuild(guild).getCustomGuildSongQueue().getSongRepeating()) {
                this.player.startTrack(track.makeClone(), false);
                return;
            }
            nextTrack();
        }
    }

    /**
     * Moves to the next {@link AudioTrack}.
     */
    public void nextTrack() {
        this.player.stopTrack();
        CafeBot.getGuildHandler().getCustomGuild(guild).getCustomGuildSongQueue().setSongPlayingStatus(false);
        CafeBot.getGuildHandler().getCustomGuild(guild).getCustomGuildSongQueue().queueNextSong();
    }

    /**
     * A method used for queueing tracks.
     * @param track The {@link AudioTrack} to be added.
     */
    public void queue(AudioTrack track) {
        if (inVoiceChannel) {
            this.player.startTrack(track, true);
        }
    }

}