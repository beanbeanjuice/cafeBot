package com.beanbeanjuice.command.music;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.lavaplayer.GuildMusicManager;
import com.beanbeanjuice.utility.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * A command used to shuffle the queue.
 *
 * @author beanbeanjuice
 */
public class ShuffleCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).setLastMusicChannel(event.getChannel());

        Member self = ctx.getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "Not In A Voice Channel",
                    "I am not currently in a voice channel."
            )).queue();
            return;
        }

        if (!event.getGuild().getMember(user).getVoiceState().getChannel().equals(selfVoiceState.getChannel())) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "Not In The Same Voice Channel",
                    "You are not currently in the same voice channel as me."
            )).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        if (musicManager.scheduler.queue.isEmpty()) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "Empty Queue",
                    "Cannot shuffle the queue as the queue is empty."
            )).queue();
            return;
        }

        if (!musicManager.scheduler.shuffle) {
            musicManager.scheduler.setShuffle(true);
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().successEmbed(
                    "Shuffled Queue",
                    "The current queue has been shuffled!"
            )).queue();
        } else {
            musicManager.scheduler.setShuffle(false);
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().successEmbed(
                    "Unshuffled Queue",
                    "The current queue has been unshuffled!"
            )).queue();
        }
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Shuffle the current queue!";
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
