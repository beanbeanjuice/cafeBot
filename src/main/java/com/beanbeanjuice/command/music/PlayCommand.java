package com.beanbeanjuice.command.music;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.lavaplayer.PlayerManager;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.hc.core5.http.ParseException;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * The command used for playing a song.
 *
 * @author beanbeanjuice
 */
public class PlayCommand implements ICommand {

    private boolean isSpotifyPlaylist = false;
    private int playlistCount = 0;
    private String playlistName = "";

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        event.getMessage().delete().queue();
        BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).setLastMusicChannel(event.getChannel());

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
            BeanBot.getGuildHandler().getCustomGuild(event.getGuild().getId()).startAudioChecking();
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

                GetTrackRequest getTrackRequest = BeanBot.getSpotifyApi().getTrack(link).build();
                link = getTrack_Sync(getTrackRequest);

            } else if (link.startsWith("playlist/")) {

                link = link.replace("playlist/", "");
                link = link.split("\\?")[0];

                GetPlaylistRequest getPlaylistRequest = BeanBot.getSpotifyApi().getPlaylist(link).build();

                isSpotifyPlaylist = true;

                try {

                    PlaylistTrack[] playlistTracks = getPlaylist_Sync(getPlaylistRequest);

                    for (PlaylistTrack playlistTrack : playlistTracks) {

                        Track track = (Track) playlistTrack.getTrack();

                        link = "ytsearch:" + track.getName() + " by " + track.getArtists()[0].getName();

                        PlayerManager.getInstance().loadAndPlay(channel, link, isSpotifyPlaylist);
                    }

                    event.getChannel().sendMessage(loadedPlaylist()).queue();
                    return;
                } catch (NullPointerException e) {
                    event.getChannel().sendMessage(emptySpotifyPlaylist()).queue();
                    BeanBot.getLogManager().log(PlayCommand.class, LogLevel.ERROR, "Spotify Rate Limit");
                    return;
                }
            }

        }
        PlayerManager.getInstance().loadAndPlay(channel, link, isSpotifyPlaylist);
    }

    private MessageEmbed userMustBeInVoiceChannelEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Sorry, you must be in a voice channel to use this command.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    private MessageEmbed emptySpotifyPlaylist() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Empty Spotify Playlist");
        embedBuilder.setDescription("The spotify playlist you requested is currently empty.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    private MessageEmbed loadedPlaylist() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Added Playlist to Queue");
        embedBuilder.addField("Tracks", String.valueOf(playlistCount), true);
        embedBuilder.addField("Playlist Name", "`" + playlistName + "`", true);
        embedBuilder.setColor(Color.green);
        return embedBuilder.build();
    }

    private String getTrack_Sync(GetTrackRequest getTrackRequest) {
        try {
            final Track track = getTrackRequest.execute();
            return "ytsearch:" + track.getName() + " by " + track.getArtists()[0].getName();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            BeanBot.getLogManager().log(PlayCommand.class, LogLevel.ERROR, "There was a sync error: " + e.getMessage());
            return null;
        }
    }

    private PlaylistTrack[] getPlaylist_Sync(GetPlaylistRequest getPlaylistRequest) {
        try {
            final Playlist playlist = getPlaylistRequest.execute();
            playlistName = playlist.getName();
            playlistCount = playlist.getTracks().getTotal();
            return playlist.getTracks().getItems();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            BeanBot.getLogManager().log(PlayCommand.class, LogLevel.ERROR, "There was a sync error: " + e.getMessage());
            return null;
        }
    }

    private MessageEmbed userMustBeInSameVoiceChannelEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Sorry, you must be in a voice channel to use this command.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    private MessageEmbed botMustBeInVoiceChannelEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("I'm not currently in a voice channel.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    private MessageEmbed emptyArgsEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setDescription("Please enter a link or a search term.");

        return embedBuilder.build();
    }

    private boolean isURL(String url) {
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
    public Usage getUsage() {
        Usage usage = new Usage();

        for (int i = 0; i < 100; i++) {
            usage.addUsage(CommandType.TEXT, "word", false);
        }
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MUSIC;
    }

}