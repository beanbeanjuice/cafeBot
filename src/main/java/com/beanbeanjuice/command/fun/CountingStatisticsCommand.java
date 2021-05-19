package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} to get the current number for the {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class CountingStatisticsCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        Integer highestNumber = CafeBot.getCountingHelper().getHighestNumber(event.getGuild());
        Integer currentNumber = CafeBot.getCountingHelper().getLastNumber(event.getGuild());

        if (highestNumber == null || currentNumber == null) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        event.getChannel().sendMessage(countingStatisticsEmbed(highestNumber, currentNumber)).queue();
    }

    @NotNull
    private MessageEmbed countingStatisticsEmbed(@NotNull Integer highestNumber, @NotNull Integer currentNumber) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Current Number");
        embedBuilder.addField("Highest Number", highestNumber.toString(), true);
        embedBuilder.addField("Current Number", currentNumber.toString(), true);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "counting-statistics";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("current-number");
        arrayList.add("currentnumber");
        arrayList.add("highest-number");
        arrayList.add("highestnumber");
        arrayList.add("countingstatistics");
        arrayList.add("counting-stats");
        arrayList.add("countingstats");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Get the counting stats for the server!";
    }

    @Override
    public String exampleUsage() {
        return "`!!counting-stats`";
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
