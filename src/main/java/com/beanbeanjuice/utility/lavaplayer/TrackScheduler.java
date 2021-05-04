package com.beanbeanjuice.utility.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A class used for managing tracks.
 */
public class TrackScheduler extends AudioEventAdapter {

    public final AudioPlayer player;
    public BlockingQueue<AudioTrack> queue;
    public BlockingQueue<AudioTrack> unshuffledQueue;
    public boolean repeating = false;
    public boolean shuffle = false;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.unshuffledQueue = new LinkedBlockingQueue<>();
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
            if (this.repeating) {
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

        AudioTrack nextTrack = this.queue.poll();
        this.player.startTrack(nextTrack, false);

        if (shuffle) {
            this.unshuffledQueue.poll();
        }
    }

    /**
     * A method used for queueing tracks.
     * @param track The {@link AudioTrack} to be added.
     */
    public void queue(AudioTrack track) {
        if (!this.player.startTrack(track, true)) {
            this.queue.offer(track);
        }
    }

    /**
     * Sets whether or not the queue should be shuffled.
     * @param shuffleState The state of shuffling.
     */
    public void setShuffle(@NotNull Boolean shuffleState) {
        shuffle = shuffleState;

        if (shuffle) {
            unshuffledQueue.clear();

            ArrayList<AudioTrack> tempUnshuffledQueue = new ArrayList<>(queue);

            for (AudioTrack audioTrack : tempUnshuffledQueue) {
                unshuffledQueue.offer(audioTrack);
            }

            ArrayList<AudioTrack> tempQueue = new ArrayList<>(queue);
            Collections.shuffle(tempQueue);
            queue.clear();

            for (AudioTrack audioTrack : tempQueue) {
                queue.offer(audioTrack);
            }
        } else {
            queue.clear();

            ArrayList<AudioTrack> tempQueue = new ArrayList<>(unshuffledQueue);

            for (AudioTrack audioTrack : tempQueue) {
                queue.offer(audioTrack);
            }
        }
    }

}