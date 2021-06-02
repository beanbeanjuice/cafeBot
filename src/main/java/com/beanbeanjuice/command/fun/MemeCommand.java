package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.helper.api.SubRedditAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * A command used for memes.
 *
 * @author beanbeanjuice
 */
public class MemeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(new SubRedditAPI(getSubreddits().get(CafeBot.getGeneralHelper().getRandomNumber(0, getSubreddits().size()))).getRedditEmbed()).queue();
    }

    @NotNull
    private ArrayList<String> getSubreddits() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("memes");
        arrayList.add("dankmemes");
        arrayList.add("me_irl");
        arrayList.add("PrequelMemes");
        arrayList.add("thatHappened");
        return arrayList;
    }

    @NotNull
    private MessageEmbed cannotGetJSONEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Unable to get JSON.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed messageEmbed(@NotNull String title, @NotNull String url, @NotNull String image) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title, url);
        embedBuilder.setImage(image);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Get a random meme!";
    }

    @Override
    public String exampleUsage() {
        return "`!!meme`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.FUN;
    }
}