package com.beanbeanjuice.utility.helper;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.wrapper.spotify.model_objects.specification.Track;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

@Deprecated
public class SpotifyHelper {

    public static void main(String[] args) {
        scrapeSite("let it go");
    }

    /**
     * Converts a {@link Track} to a {@link AudioTrack}.
     * @param track The {@link Track} to be converted.
     * @return The converted {@link AudioTrack}.
     */
    public AudioTrack convertToAudioTrack(Track track) {
        String title = track.getName();
        String author = track.getArtists()[0].getName();
        Long duration = Long.getLong(String.valueOf(track.getDurationMs()));

        // Literal YouTube Identifier
        String identifier = "";

        // URL to the YouTube video
        String url = "";

        AudioTrackInfo audioTrackInfo = new AudioTrackInfo(title, author, duration, identifier, true, url);
        return null;
    }

    private static void scrapeSite(String search) {
//        String query = "https://www.youtube.com/results?search_query=";
        search += " youtube video";
        String query = "https://www.google.com/search?q=";
        String encoding = "UTF-8";

        try {
            System.out.println(query + URLEncoder.encode(search, encoding));
        } catch (IOException ignored) {}

        try {
            Document google = Jsoup.connect(query + URLEncoder.encode(search, encoding)).userAgent("Mozilla/5.0").get();

//            Elements links = google.select("a[href]");
            Elements links = google.getElementsByTag("cite");

//            Element element = google.getElementById("video-title");
//            System.out.println(element.text());

            // Check if any results are found
            if (links.isEmpty()) {
                System.out.println("No Results");
                return;
            }

            links.forEach(link -> {
                System.out.println(link.text());
            });

        } catch (IOException e) {
            // TODO: Implement something here
        }


    }

}
