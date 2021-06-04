package com.beanbeanjuice.utility.sections.music.custom;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.sections.music.lavaplayer.PlayerManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class CustomGuildSongQueueHandler {

    private String guildID;
    private ArrayList<CustomSong> customSongQueue;
    private ArrayList<CustomSong> unshuffledQueue;
    private ArrayList<CustomSong> repeatQueue;
    private CustomSong currentSong;
    private boolean songPlaying = false;
    private boolean playlistRepeat = false;
    private boolean songRepeat = false;
    private boolean shuffle = false;

    public CustomGuildSongQueueHandler(@NotNull String guildID) {
        this.guildID = guildID;
        customSongQueue = new ArrayList<>();
        unshuffledQueue = new ArrayList<>();
        repeatQueue = new ArrayList<>();
    }

    public void addCustomSong(@NotNull CustomSong customSong, @NotNull boolean fromRepeat) {

        // Adds the song randomly if needed.
        if (shuffle) {
            customSongQueue.add(CafeBot.getGeneralHelper().getRandomNumber(0, customSongQueue.size()), customSong);
        } else {
            customSongQueue.add(customSong);
        }
        unshuffledQueue.add(customSong);

        if (!fromRepeat && playlistRepeat) {
            repeatQueue.add(customSong);
        }
        queueNextSong();
    }

    public void queueNextSong() {
        if (!songPlaying) {
            try {
                songPlaying = true;
                currentSong = customSongQueue.remove(0);
                unshuffledQueue.remove(currentSong);
                PlayerManager.getInstance().loadAndPlay(currentSong.getSearchString(), CafeBot.getGuildHandler().getGuild(guildID));
            } catch (IndexOutOfBoundsException e) {

                if (playlistRepeat) {
                    for (CustomSong customSong : repeatQueue) {
                        addCustomSong(customSong, true);
                    }
                } else {
                    currentSong = null;
                }
            }
        }
    }

    public void clear() {
        customSongQueue.clear();
        repeatQueue.clear();
        unshuffledQueue.clear();
        playlistRepeat = false;
        songRepeat = false;
        shuffle = false;
    }

    public void setShuffle(@NotNull Boolean bool) {
        shuffle = bool;

        if (shuffle) {
            unshuffledQueue = customSongQueue;
            Collections.shuffle(customSongQueue);
        } else {
            customSongQueue = unshuffledQueue;
            unshuffledQueue.clear();
        }
    }

    @NotNull
    public Boolean getShuffle() {
        return shuffle;
    }

    public void setPlaylistRepeating(@NotNull Boolean bool) {
        playlistRepeat = bool;

        if (playlistRepeat && songRepeat) {
            repeatQueue = customSongQueue;
            songRepeat = false;
        } else {
            repeatQueue.clear();
        }
    }

    @NotNull
    public Boolean getPlaylistRepeating() {
        return playlistRepeat;
    }

    @NotNull
    public Boolean getSongRepeating() {
        return songRepeat;
    }

    public void setSongRepeating(@NotNull Boolean bool) {
        songRepeat = bool;

        if (songRepeat && playlistRepeat) {
            playlistRepeat = false;
            repeatQueue.clear();
        }
    }

    public CustomSong getCurrentSong() {
        return currentSong;
    }

    public ArrayList<CustomSong> getCustomSongQueue() {
        return customSongQueue;
    }

    public void setSongPlayingStatus(@NotNull Boolean bool) {
        songPlaying = bool;
    }

}
