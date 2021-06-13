package com.beanbeanjuice.utility.sections.music.custom;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sections.music.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A {@link CustomSongManager} class used for handling lavaplayer songs.
 *
 * @author beanbeanjuice
 */
public class CustomSongManager {

    /**
     * Add a {@link CustomSong} to the specified {@link Guild}.
     * @param guild The {@link Guild} to add the {@link CustomSong} to.
     * @param spotifyTrack The {@link Track} to be used.
     * @param user The {@link User} who sent the song request.
     */
    public void addSongToGuild(@NotNull Guild guild, @NotNull Track spotifyTrack, @NotNull User user) {
        String trackName = spotifyTrack.getName();

        if (spotifyTrack.getIsExplicit()) {
            trackName += " uncensored";
        }
        CafeBot.getGuildHandler().getCustomGuild(guild).getCustomGuildSongQueue().addCustomSong(new CustomSong(trackName, spotifyTrack.getArtists()[0].getName(), Long.parseLong(spotifyTrack.getDurationMs().toString()), user), false);
    }

    /**
     * Add a {@link CustomSong} to the specified {@link Guild}.
     * @param guild The {@link Guild} to add the {@link CustomSong} to.
     * @param searchString The search {@link String} for YouTube.
     * @param user The {@link User} who sent the request.
     */
    public void addSongToGuild(@NotNull Guild guild, @NotNull String searchString, @NotNull User user) {
        AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);

        audioPlayerManager.loadItemOrdered(new GuildMusicManager(audioPlayerManager, guild), searchString, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                CafeBot.getGuildHandler().getCustomGuild(guild).getCustomGuildSongQueue().addCustomSong(new CustomSong(audioTrack.getInfo().title, audioTrack.getInfo().author, audioTrack.getDuration(), user), false);
                try {
                    CafeBot.getGuildHandler().getCustomGuild(guild).getLastMusicChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                            "Queued Song",
                            "`" + audioTrack.getInfo().title + "` by `" + audioTrack.getInfo().author + "` [`"
                                    + CafeBot.getGeneralHelper().formatTime(audioTrack.getDuration()) + "`]\n\n" +
                                    "**Requested By**: " + user.getAsMention()
                    )).queue();
                } catch (NullPointerException ignored) {}
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                if (searchString.startsWith("ytsearch:")) {
                    trackLoaded(tracks.get(0));
                    return;
                }

                for (final AudioTrack track : tracks) {
                    CafeBot.getGuildHandler().getCustomGuild(guild).getCustomGuildSongQueue().addCustomSong(new CustomSong(track.getInfo().title, track.getInfo().author, track.getDuration(), user), false);
                }

                try {
                    CafeBot.getGuildHandler().getCustomGuild(guild).getLastMusicChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                            "Queued Playlist",
                            "`" + audioPlaylist.getName() + "` containing `" + audioPlaylist.getTracks().size() + "` songs.\n\n" +
                                    "**Requested By**: " + user.getAsMention()
                    )).queue();
                } catch (NullPointerException ignored) {}
            }

            @Override
            public void noMatches() {
                CafeBot.getLogManager().log(this.getClass(), LogLevel.DEBUG, "No Matches: " + searchString);
            }

            @Override
            public void loadFailed(FriendlyException e) {
                CafeBot.getLogManager().log(this.getClass(), LogLevel.DEBUG, "Load Failed");
            }
        });
    }
}
