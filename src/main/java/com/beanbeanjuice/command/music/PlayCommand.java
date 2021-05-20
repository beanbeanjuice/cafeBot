package com.beanbeanjuice.command.music;

import be.ceau.itunesapi.Lookup;
import be.ceau.itunesapi.request.Entity;
import be.ceau.itunesapi.response.Response;
import be.ceau.itunesapi.response.Result;
import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.music.lavaplayer.PlayerManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The command used for playing a song.
 *
 * @author beanbeanjuice
 */
public class PlayCommand implements ICommand {

    private int playlistCount = 0;
    private String playlistName = "";

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setLastMusicChannel(event.getChannel());

        final TextChannel channel = event.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (args.isEmpty()) {
            event.getChannel().sendMessage(emptyArgsEmbed()).queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel()) {
            if (!event.getMember().getVoiceState().inVoiceChannel()) {
                event.getChannel().sendMessage(userMustBeInVoiceChannelEmbed()).queue();
                return;
            }

            // Join the channel and play music and say you joined
            ctx.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());

            // Start listening for the audio connection.
            CafeBot.getGuildHandler().getCustomGuild(event.getGuild().getId()).startAudioChecking();
        } else if (!event.getMember().getVoiceState().getChannel().equals(selfVoiceState.getChannel())) {
            event.getChannel().sendMessage(userMustBeInSameVoiceChannelEmbed()).queue();
            return;
        }

        String link = String.join(" ", args);

        if (!isURL(link)) {
            link = "ytsearch:" + link;
        } else if (link.contains("spotify.com")) {

            link = link.replace("https://open.spotify.com/", "");

            if (link.startsWith("track/")) {

                link = link.replace("track/", "");
                link = link.split("\\?")[0];

                Track track;

                try {
                    track = CafeBot.getSpotifyApi().getTrack(link).build().execute();
                } catch (SpotifyWebApiException | IOException | ParseException e) {
                    // TODO: Unable to get spotify song.
                    return;
                }

                link = "ytsearch:" + getLinkFromSpotifyTrack(track) + " audio only";
                PlayerManager.getInstance().loadAndPlay(channel, link, false);
                return;

            } else if (link.startsWith("playlist/")) {

                link = link.replace("playlist/", "");
                link = link.split("\\?")[0];

                ArrayList<PlaylistTrack> playlistTracksUnconverted = new ArrayList<>();
                Playlist playlist;

                try {

                    GetPlaylistsItemsRequest playlistsItemsRequest = CafeBot.getSpotifyApi().getPlaylistsItems(link).build();
                    Paging<PlaylistTrack> playlistTrackPaging = playlistsItemsRequest.execute();

                    playlistTracksUnconverted.addAll(Arrays.asList(playlistTrackPaging.getItems()));
                    int currentOffset = 100;

                    while (playlistTrackPaging.getNext() != null) {
                        playlistsItemsRequest = CafeBot.getSpotifyApi().getPlaylistsItems(link).offset(currentOffset).build();
                        playlistTrackPaging = playlistsItemsRequest.execute();
                        playlistTracksUnconverted.addAll(Arrays.asList(playlistTrackPaging.getItems()));

                        currentOffset += 100;
                    }

                    playlist = CafeBot.getSpotifyApi().getPlaylist(link).build().execute();

                } catch (SpotifyWebApiException | IOException | ParseException e) {
                    // TODO: Unable to get spotify playlist.
                    return;
                }

                playlistName = playlist.getName();
                playlistCount = playlist.getTracks().getTotal();

                for (PlaylistTrack playlistTrack : playlistTracksUnconverted) {

                    Track track = (Track) playlistTrack.getTrack();

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(track.getName()).append(" by ")
                            .append(track.getArtists()[0].getName());

                    if (track.getArtists().length > 1) {
                        stringBuilder.append(" and ").append(track.getArtists()[1].getName());
                    }

                    PlayerManager.getInstance().loadAndPlay(channel, "ytsearch:" + stringBuilder.toString() + " audio only", true);
                }
                event.getChannel().sendMessage(loadedPlaylist()).queue();
                return;
            }

        } else if (link.contains("apple.com")) { // Apple Music Links

            if (!link.contains("playlist")) {
                Response response = new Lookup()
                        .addId(link.split("=")[1])
                        .setEntity(Entity.SONG)
                        .execute();

                String songName = response.getResults().get(0).getTrackName();
                String artistName = response.getResults().get(0).getArtistName();

                link = "ytsearch:" + songName + " by " + artistName + " audio only";
            } else {

//                link = link.split("pl.u-")[1].split("\\?")[0];
//
//                Response response = new Lookup()
//                        .addId(link)
//                        .setEntity(Entity.SONG)
//                        .execute();
//
//                for (Result result : response.getResults()) {
//                    System.out.println(result.getTrackName());
//                }

                event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                        "Cannot Get Apple Music Playlist",
                        "Doing this requires a developer license for the Apple Itunes API. " +
                                "Currently, I do not have this, but will try to get one in the future. Please just use individual songs for now."
                )).queue();

                return;
            }

        }
        PlayerManager.getInstance().loadAndPlay(channel, link, false);
    }

    @NotNull
    private String getLinkFromSpotifyTrack(@NotNull Track track) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(track.getName()).append(" by ")
                .append(track.getArtists()[0].getName());

        if (track.getArtists().length > 1) {
            stringBuilder.append(" and ")
                    .append(track.getArtists()[1].getName());
        }
        return stringBuilder.toString();
    }

    @NotNull
    private MessageEmbed userMustBeInVoiceChannelEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Sorry, you must be in a voice channel to use this command.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed emptySpotifyPlaylist() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Empty Spotify Playlist");
        embedBuilder.setDescription("The spotify playlist you requested is currently empty.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed loadedPlaylist() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Added Playlist to Queue");
        embedBuilder.addField("Tracks", String.valueOf(playlistCount), true);
        embedBuilder.addField("Playlist Name", "`" + playlistName + "`", true);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed userMustBeInSameVoiceChannelEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Sorry, you must be in a voice channel to use this command.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed botMustBeInVoiceChannelEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("I'm not currently in a voice channel.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed emptyArgsEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Please enter a link or a search term.");
        return embedBuilder.build();
    }

    @NotNull
    private Boolean isURL(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("p");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Play a song!";
    }

    @Override
    public String exampleUsage() {
        return "`!!play feeling xqcL` or `!!play https://open.spotify.com/track/4KkJDRf8H1e3UuoLhCWtvf?si=4b90debdb82947f2`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.SENTENCE, "link/playlist", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MUSIC;
    }

}