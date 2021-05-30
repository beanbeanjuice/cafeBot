package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.helper.RedditAPI;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to get coffee memes from the {@link com.beanbeanjuice.utility.helper.RedditAPI RedditAPI}.
 *
 * @author beanbeanjuice
 */
public class CoffeeMemeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(new RedditAPI().getRedditEmbed(getSubreddits().get(CafeBot.getGeneralHelper().getRandomNumber(0, getSubreddits().size())))).queue();
    }

    @NotNull
    private ArrayList<String> getSubreddits() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("CoffeePorn");
        arrayList.add("coffeememes");
        arrayList.add("coffeewithaview");
        return arrayList;
    }

    @Override
    public String getName() {
        return "coffee-meme";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("coffeememe");
        arrayList.add("coffee");
        arrayList.add("coffee-memes");
        arrayList.add("coffeememes");
        arrayList.add("cafe-meme");
        arrayList.add("cafememe");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Get a coffee meme!";
    }

    @Override
    public String exampleUsage() {
        return "`!!coffee-meme`";
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
