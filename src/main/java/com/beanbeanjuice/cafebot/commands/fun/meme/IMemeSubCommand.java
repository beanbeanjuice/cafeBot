package com.beanbeanjuice.cafebot.commands.fun.meme;

import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.meme.api.wrapper.MemeAPI;
import com.beanbeanjuice.meme.api.wrapper.objects.RedditMeme;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.CompletionException;

public interface IMemeSubCommand extends ISubCommand {

    String[] getSubreddits();

    default void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        int subredditIndex = Helper.getRandomInteger(0, this.getSubreddits().length);

        MemeAPI.get(this.getSubreddits()[subredditIndex])
                .thenAccept((redditMeme) -> event.getHook().sendMessageEmbeds(this.getMessageEmbed(redditMeme)).queue())
                .exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            ctx.getUserI18n().getString("command.meme.description.error.embed.title"),
                            ctx.getUserI18n().getString("command.meme.description.error.embed.description")
                    )).queue();
                    throw new CompletionException(ex);
                });
    }

    default MessageEmbed getMessageEmbed(RedditMeme meme) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(meme.getTitle());
        embedBuilder.setAuthor(meme.getAuthor(), meme.getUrl());
        embedBuilder.setImage(meme.getImageUrl());
        embedBuilder.setFooter(String.format("r/%s", meme.getSubreddit()));
        embedBuilder.setColor(Helper.getRandomColor());

        return embedBuilder.build();
    }

}
