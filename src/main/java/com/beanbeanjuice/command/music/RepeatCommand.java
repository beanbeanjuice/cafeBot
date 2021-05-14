package com.beanbeanjuice.command.music;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.lavaplayer.GuildMusicManager;
import com.beanbeanjuice.utility.lavaplayer.PlayerManager;
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
 * The command used for repeating the current song.
 *
 * @author beanbeanjuice
 */
public class RepeatCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        String commandName = args.get(0).toLowerCase();

        if (!commandName.equals("song") && !commandName.equals("playlist")) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "Incorrect Command Term",
                    "You must use the command term `song` or `playlist` not `" + commandName + "`."
            )).queue();
            return;
        }

        BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).setLastMusicChannel(event.getChannel());

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            event.getChannel().sendMessage(botMustBeInVoiceChannelEmbed()).queue();
            return;
        }

        if (selfVoiceState.inVoiceChannel()) {
            if (!event.getMember().getVoiceState().inVoiceChannel()) {
                event.getChannel().sendMessage(user.getAsMention()).queue(e -> {
                    e.delete().queue();
                });
                event.getChannel().sendMessage(mustBeInVoiceChannelEmbed()).queue();
                return;
            }

            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

            if (commandName.equals("song")) {
                final boolean newRepeating = !musicManager.scheduler.songRepeating;
                musicManager.scheduler.songRepeating = newRepeating;

                if (newRepeating) {
                    event.getChannel().sendMessage(successEmbed("Song repeating has now been turned on.")).queue();
                } else {
                    event.getChannel().sendMessage(successEmbed("Song repeating has now been turned off.")).queue();
                }
            }

            if (commandName.equals("playlist")) {

                if (!musicManager.scheduler.playlistRepeating() && musicManager.scheduler.queue.isEmpty()) {
                    event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                            "Cannot Repeat Playlist",
                            "Cannot repeat the playlist as the playlist is currently empty."
                    )).queue();
                    return;
                }

                final boolean newRepeating = !musicManager.scheduler.playlistRepeating();
                musicManager.scheduler.setPlaylistRepeating(newRepeating);

                if (newRepeating) {
                    event.getChannel().sendMessage(successEmbed("Playlist repeating has now been turned on.")).queue();
                } else {
                    event.getChannel().sendMessage(successEmbed("Playlist repeating has now been turned off.")).queue();
                }
            }
        }
    }

    @NotNull
    private MessageEmbed botMustBeInVoiceChannelEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("I'm not currently in a voice channel.");
        embedBuilder.setColor(Color.red);
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
    private MessageEmbed successEmbed(@NotNull String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(message);
        embedBuilder.setColor(Color.green);

        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Repeat the current song playing.";
    }

    @Override
    public String exampleUsage() {
        return "`!!repeat playlist` or `!!repeat song`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "song/playlist", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MUSIC;
    }

}
