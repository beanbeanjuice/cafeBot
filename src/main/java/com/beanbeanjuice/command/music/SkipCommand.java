package com.beanbeanjuice.command.music;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.sections.music.custom.CustomSong;
import com.beanbeanjuice.utility.sections.music.lavaplayer.GuildMusicManager;
import com.beanbeanjuice.utility.sections.music.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;

/**
 * A command used for skipping the current song.
 *
 * @author beanbeanjuice
 */
public class SkipCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        ctx.getCustomGuild().setLastMusicChannel(event.getChannel());

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            event.getChannel().sendMessage(botMustBeInVoiceChannelEmbed()).queue();
            return;
        }

        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getChannel().sendMessage(mustBeInVoiceChannelEmbed()).queue();
            return;
        }

        if (!event.getMember().getVoiceState().getChannel().equals(selfVoiceState.getChannel())) {
            event.getChannel().sendMessage(userMustBeInSameVoiceChannelEmbed()).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            event.getChannel().sendMessage(noTrackPlayingEmbed()).queue();
            return;
        }

        musicManager.scheduler.nextTrack();
        event.getChannel().sendMessage(successEmbed(CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getCustomGuildSongQueue().getCurrentSong(), CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getCustomGuildSongQueue().getCustomSongQueue().size())).queue();
    }

    @NotNull
    private MessageEmbed userMustBeInSameVoiceChannelEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Sorry, you must be in the same voice channel as the bot to use this command.");
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
    private MessageEmbed noTrackPlayingEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setDescription("There is no track currently playing.");
        embedBuilder.setColor(Color.orange);

        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed mustBeInVoiceChannelEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Sorry, you must be in a voice channel to use this command.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed successEmbed(@Nullable CustomSong customSong, @NotNull Integer songsLeftInQueue) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Skipped Song");

        StringBuilder descriptionBuilder = new StringBuilder();

        if (customSong != null) {
            descriptionBuilder.append("Now Playing - `").append(customSong.getName())
                    .append("` by `").append(customSong.getAuthor())
                    .append("` [`").append(customSong.getLengthString()).append("]`\n\n")
                    .append("**Requested By**: ").append(customSong.getRequester().getAsMention());
        } else {
            descriptionBuilder.append("No song is currently playing. Please wait for a track to start playing if the queue is not empty...");
        }

        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setColor(Color.green);
        embedBuilder.setFooter("Songs Left: " + songsLeftInQueue);
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Skips the current song";
    }

    @Override
    public String exampleUsage() {
        return "`!!skip`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MUSIC;
    }

}