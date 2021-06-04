package com.beanbeanjuice.utility.sections.music.custom;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sections.music.lavaplayer.PlayerManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomGuildSongQueue {

    private String guildID;
    private ArrayList<CustomSong> customSongQueue;
    private ArrayList<CustomSong> unshuffledQueue;
    private ArrayList<CustomSong> repeatQueue;
    private CustomSong currentSong;
    private boolean songPlaying = false;

    public CustomGuildSongQueue(@NotNull String guildID) {
        this.guildID = guildID;
        customSongQueue = new ArrayList<>();
        repeatQueue = new ArrayList<>();
    }

    public void addCustomSong(@NotNull CustomSong customSong) {
        System.out.println("Adding Song:" + customSong.getName());
        customSongQueue.add(customSong);
        System.out.println(customSongQueue.size());
        queueNextSong();
    }

    public void queueNextSong() {
        if (!songPlaying) {
            try {
                songPlaying = true;
                currentSong = customSongQueue.remove(0);
                PlayerManager.getInstance().loadAndPlay(currentSong.getSearchString(), CafeBot.getGuildHandler().getGuild(guildID));
            } catch (IndexOutOfBoundsException e) {
                currentSong = null;
            }
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
