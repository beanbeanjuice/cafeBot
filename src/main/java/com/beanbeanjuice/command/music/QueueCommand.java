package com.beanbeanjuice.command.music;

import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.lavaplayer.GuildMusicManager;
import com.beanbeanjuice.utility.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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

        event.getMessage().delete().queue();

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

    private MessageEmbed queueEmbed(ArrayList<AudioTrack> trackList, int trackCount) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Current Queue");

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
                    .append(formatTime(audioTrack.getDuration()))
                    .append("`]\n");
        }

        embedBuilder.setDescription(message);
        embedBuilder.setColor(Color.cyan);

        if (trackList.size() > trackCount) {
            embedBuilder.setFooter("And " + (trackList.size() - trackCount) + " more...");
        }

        return embedBuilder.build();
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

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
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MUSIC;
    }

}