package com.beanbeanjuice.utility.sections.music.custom;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sections.music.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class CustomSong {

    private String name;
    private String author;
    private Long length;
    private User requester;

    public CustomSong(@NotNull String name, @NotNull String author, @NotNull Long length, @NotNull User requester) {
        this.name = name;
        this.author = author;
        this.length = length;
        this.requester = requester;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getAuthor() {
        return author;
    }

    @NotNull
    public String getLengthString() {
        return CafeBot.getGeneralHelper().formatTime(length);
    }

    @NotNull
    public String getSearchString() {
        return "ytsearch:" + name + " by " + author;
    }

    @NotNull
    public User getRequester() {
        return requester;
    }

}
