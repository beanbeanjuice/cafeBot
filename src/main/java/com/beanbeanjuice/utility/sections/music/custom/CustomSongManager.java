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

public class CustomSongManager {

    public void addSongToGuild(@NotNull Guild guild, @NotNull Track spotifyTrack, @NotNull User user) {
        CafeBot.getGuildHandler().getCustomGuild(guild).getCustomGuildSongQueue().addCustomSong(new CustomSong(spotifyTrack.getName(), spotifyTrack.getArtists()[0].getName(), Long.parseLong(spotifyTrack.getDurationMs().toString()), user), false);
    }

    public void addSongToGuild(@NotNull Guild guild, @NotNull String searchString, @NotNull User user) {
        AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);

        audioPlayerManager.loadItemOrdered(new GuildMusicManager(audioPlayerManager, guild), searchString, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                CafeBot.getGuildHandler().getCustomGuild(guild).getCustomGuildSongQueue().addCustomSong(new CustomSong(audioTrack.getInfo().title, audioTrack.getInfo().author, audioTrack.getDuration(), user), false);
                CafeBot.getGuildHandler().getCustomGuild(guild).getLastMusicChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                        "Queued Song",
                        "`" + audioTrack.getInfo().title + "` by `" + audioTrack.getInfo().author + "` [`"
                                + CafeBot.getGeneralHelper().formatTime(audioTrack.getDuration()) + "`]\n\n" +
                                "**Requested By**: " + user.getAsMention()
                )).queue();
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

                CafeBot.getGuildHandler().getCustomGuild(guild).getLastMusicChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                        "Queued Playlist",
                        "`" + audioPlaylist.getName() + "` containing `" + audioPlaylist.getTracks().size() + "` songs.\n\n" +
                                "**Requested By**: " + user.getAsMention()
                )).queue();
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