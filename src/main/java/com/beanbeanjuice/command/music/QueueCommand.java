package com.beanbeanjuice.command.music;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.sections.music.custom.CustomSong;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * The command used for seeing the song queue.
 *
 * @author beanbeanjuice
 */
public class QueueCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        ctx.getCustomGuild().setLastMusicChannel(event.getChannel());

        ArrayList<CustomSong> queue = CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getCustomGuildSongQueue().getCustomSongQueue();

        if (queue.isEmpty()) {
            event.getChannel().sendMessage(emptyQueueEmbed()).queue();
            return;
        }

        event.getChannel().sendMessage(queueEmbed(queue, CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getCustomGuildSongQueue().getQueueLengthMS())).queue();
    }

    @NotNull
    private MessageEmbed queueEmbed(@NotNull ArrayList<CustomSong> queue, @NotNull Long queueTime) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Current Queue");

        StringBuilder message = new StringBuilder();

        for (int i = 0; i < queue.size() && i < 20; i++) {
            CustomSong customSong = queue.get(i);

            message.append("**#").append(i + 1)
                    .append("** `")
                    .append(customSong.getName())
                    .append(" by ")
                    .append(customSong.getAuthor())
                    .append("` [`")
                    .append(customSong.getLengthString())
                    .append("`]\n");
        }

        message.append("**Total Queue Time**: `").append(CafeBot.getGeneralHelper().formatTimeDays(queueTime)).append("`");

        embedBuilder.setDescription(message);
        embedBuilder.setColor(Color.cyan);

        if (queue.size() > 20) {
            embedBuilder.setFooter("And " + (queue.size() - 20) + " more...");
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
    public String exampleUsage(String prefix) {
        return "`" + prefix + "queue`";
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