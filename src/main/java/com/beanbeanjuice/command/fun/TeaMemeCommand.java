package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.helper.api.RedditAPI;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} to send Tea memes.
 *
 * @author beanbeanjuice
 */
public class TeaMemeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(new RedditAPI(getSubreddits().get(CafeBot.getGeneralHelper().getRandomNumber(0, getSubreddits().size()))).getRedditEmbed()).queue();
    }

    @NotNull
    private ArrayList<String> getSubreddits() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("TeaPorn");
        arrayList.add("TeaPictures");
        return arrayList;
    }

    @Override
    public String getName() {
        return "tea-meme";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("teameme");
        arrayList.add("tea");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Get a tea meme!";
    }

    @Override
    public String exampleUsage() {
        return "`!!tea`";
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
