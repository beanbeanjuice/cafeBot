package com.beanbeanjuice.command.music;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.sections.music.lavaplayer.GuildMusicManager;
import com.beanbeanjuice.utility.sections.music.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * A command used for stopping the queue and making the bot leave.
 *
 * @author beanbeanjuice
 */
public class StopCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setLastMusicChannel(event.getChannel());

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            event.getChannel().sendMessage(botMustBeInVoiceChannelEmbed()).queue();
            return;
        }

        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getChannel().sendMessage(userMustBeInVoiceChannelEmbed()).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();
        musicManager.scheduler.unshuffledQueue.clear();
        musicManager.scheduler.playlistRepeatQueue.clear();
        musicManager.scheduler.setShuffle(false);
        musicManager.scheduler.setPlaylistRepeating(false);
        musicManager.scheduler.inVoiceChannel = false;
        ctx.getGuild().getAudioManager().closeAudioConnection();

        // Stop listening for the audio connection and leave.
        CafeBot.getGuildHandler().getCustomGuild(event.getGuild().getId()).stopAudioChecking();
        event.getChannel().sendMessage(successEmbed()).queue();
    }

    @NotNull
    private MessageEmbed botMustBeInVoiceChannelEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("I'm not currently in a voice channel.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed userMustBeInVoiceChannelEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Sorry, you must be in a voice channel to use this command.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed successEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Successfully removed tracks and left the call.");
        embedBuilder.setColor(Color.green);

        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("leave");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Stop the current music playing and make the bot leave.";
    }

    @Override
    public String exampleUsage() {
        return "`!!stop`";
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