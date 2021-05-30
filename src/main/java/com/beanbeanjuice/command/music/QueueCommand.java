package com.beanbeanjuice.command.music;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.sections.music.lavaplayer.GuildMusicManager;
import com.beanbeanjuice.utility.sections.music.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The command used for seeing the song queue.
 *
 * @author beanbeanjuice
 */
public class QueueCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        ctx.getCustomGuild().setLastMusicChannel(event.getChannel());

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if (queue.isEmpty()) {
            event.getChannel().sendMessage(emptyQueueEmbed()).queue();
            return;
        }

        int trackCount = Math.min(queue.size(), 20);
        final ArrayList<AudioTrack> trackList = new ArrayList<>(queue);
        event.getChannel().sendMessage(queueEmbed(trackList, trackCount)).queue();
    }

    @NotNull
    private MessageEmbed queueEmbed(@NotNull ArrayList<AudioTrack> trackList, @NotNull Integer trackCount) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Current Queue");

        StringBuilder message = new StringBuilder();

        for (int i = 0; i < trackCount; i++) {
            AudioTrack audioTrack = trackList.get(i);
            AudioTrackInfo audioTrackInfo = audioTrack.getInfo();

            message.append("#").append(i + 1)
                    .append(" `")
                    .append(audioTrackInfo.title)
                    .append(" by ")
                    .append(audioTrackInfo.author)
                    .append("` [`")
                    .append(CafeBot.getGeneralHelper().formatTime(audioTrack.getDuration()))
                    .append("`]\n");
        }

        embedBuilder.setDescription(message);
        embedBuilder.setColor(Color.cyan);

        if (trackList.size() > trackCount) {
            embedBuilder.setFooter("And " + (trackList.size() - trackCount) + " more...");
        }

        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed emptyQueueEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.orange);
        embedBuilder.setDescription("The queue is currently empty.");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Shows the queued up songs.";
    }

    @Override
    public String exampleUsage() {
        return "`!!queue`";
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