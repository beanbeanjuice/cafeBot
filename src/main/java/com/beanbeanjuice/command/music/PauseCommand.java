package com.beanbeanjuice.command.music;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.sections.music.lavaplayer.GuildMusicManager;
import com.beanbeanjuice.utility.sections.music.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * A command used for pausing music.
 *
 * @author beanbeanjuice
 */
public class PauseCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setLastMusicChannel(event.getChannel());

        Member self = ctx.getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Not In A Voice Channel",
                    "I am not currently in a voice channel."
            )).queue();
            return;
        }

        if (!event.getGuild().getMember(user).getVoiceState().getChannel().equals(selfVoiceState.getChannel())) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Not In The Same Voice Channel",
                    "You are not currently in the same voice channel as me."
            )).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (musicManager.scheduler.queue.isEmpty() && audioPlayer.getPlayingTrack() == null) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Empty Queue",
                    "Cannot pause the current track as the queue is empty."
            )).queue();
            return;
        }

        if (audioPlayer.isPaused()) {
            audioPlayer.setPaused(false);
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Unpaused Song",
                    "The current song has been successfully unpaused."
            )).queue();
        } else {
            audioPlayer.setPaused(true);
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Paused Song",
                    "The current song has been successfully paused."
            )).queue();
        }

    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Pause/Unpause the current song!";
    }

    @Override
    public String exampleUsage() {
        return "`!!pause`";
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
