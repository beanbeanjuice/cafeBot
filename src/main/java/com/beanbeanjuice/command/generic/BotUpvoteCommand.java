package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to upvote the bot.
 *
 * @author beanbeanjuice
 */
public class BotUpvoteCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                "Bot Upvote",
                """
                        If you want to show your support for the bot, please click the links below! Please click the links below

                        **Link 1**: [top.gg](https://top.gg/bot/787162619504492554)."""
        )).queue();
    }

    @Override
    public String getName() {
        return "bot-upvote";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("botupvote");
        arrayList.add("upvote-bot");
        arrayList.add("upvotebot");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Upvote the bot on [top.gg](https://top.gg/bot/787162619504492554)!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "upvote-bot`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }
}
