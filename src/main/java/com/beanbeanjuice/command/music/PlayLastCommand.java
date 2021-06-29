package com.beanbeanjuice.command.music;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.sections.music.custom.CustomGuildSongQueueHandler;
import com.beanbeanjuice.utility.sections.music.custom.CustomSong;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to put the last song in the queue in the front of the queue.
 *
 * @author beanbeanjuice
 */
public class PlayLastCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        CustomGuildSongQueueHandler customQueueHandler = CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getCustomGuildSongQueue();
        ArrayList<CustomSong> customSongQueue = customQueueHandler.getCustomSongQueue();

        // Checking if the song queue is empty.
        if (customSongQueue.isEmpty()) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Empty Queue",
                    "The song queue is currently empty."
            )).queue();
            return;
        }

        // Puts the last song in the queue in the front.
        customQueueHandler.reorderLast();
        CustomSong firstSong = customQueueHandler.getCustomSongQueue().get(0);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Next Song");
        embedBuilder.setDescription("`" + firstSong.getName() + "` by `" + firstSong.getAuthor() + "` `[" + firstSong.getLengthString() + "]`\n\n" +
                "**Requested By**: " + firstSong.getRequester().getAsMention());
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "play-last";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("playlast");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Puts the last song in the queue at the top of the queue.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "playlast`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.EXPERIMENTAL;
    }
}
