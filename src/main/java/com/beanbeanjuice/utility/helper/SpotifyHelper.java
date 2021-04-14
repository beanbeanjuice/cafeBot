package com.beanbeanjuice.utility.helper;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.wrapper.spotify.model_objects.specification.Track;

public class SpotifyHelper {

    public AudioTrack convertToAudioTrack(Track track) {
        String title = track.getName();
        String author = track.getArtists()[0].getName();
        Long duration = Long.getLong(String.valueOf(track.getDurationMs()));

        // Literal YouTube Identifier
        String identifier = "ytsearch:";

        // URL to the YouTube video
        String url = "";

        AudioTrackInfo audioTrackInfo = new AudioTrackInfo(title, author, duration, identifier, true, url);
        return null;
    }

}
