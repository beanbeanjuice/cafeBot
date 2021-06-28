package com.beanbeanjuice.command.testing;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class TestCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        CafeBot.getTopGGAPI().getBot("787162619504492554").whenCompleteAsync((bot, e) -> {
            event.getChannel().sendMessage("Votes: " + String.valueOf(bot.getMonthlyPoints())).queue();
        });

        CafeBot.getTopGGAPI().getStats("787162619504492554").whenComplete((stats, e) -> {
            event.getChannel().sendMessage("Server Count: " + String.valueOf(stats.getServerCount())).queue();
        });

        CafeBot.getTopGGAPI().getVoters("").whenCompleteAsync((voters, e) -> {

        });
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Test Command";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "Test";
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
