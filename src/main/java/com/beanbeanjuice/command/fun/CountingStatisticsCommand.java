package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import io.github.beanbeanjuice.cafeapi.cafebot.counting.CountingInformation;
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
        CountingInformation countingInformation = CafeBot.getCountingHelper().getCountingInformation(event.getGuild());

        // Checks if there was an error getting the counting information.
        if (countingInformation == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Getting Counting Information",
                    "There has been an error retrieving this Discord server's counting information."
            )).queue();
            return;
        }

        Integer leaderboardPlace = CafeBot.getCountingHelper().getCountingLeaderboardPlace(countingInformation.getHighestNumber());
        if (leaderboardPlace == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError(
                    "There was an error getting counting statistics. There are eitehr no counting statistics for this server " +
                            "on this bot, or there is an SQL server error."
            )).queue();
            return;
        }

        event.getChannel().sendMessageEmbeds(countingStatisticsEmbed(countingInformation.getHighestNumber(), countingInformation.getLastNumber(), leaderboardPlace)).queue();
    }

    @NotNull
    private MessageEmbed countingStatisticsEmbed(@NotNull Integer highestNumber, @NotNull Integer currentNumber, @NotNull Integer leaderboardPlace) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Current Number");
        embedBuilder.addField("Highest Number", highestNumber.toString(), true);
        embedBuilder.addField("Current Number", currentNumber.toString(), true);
        embedBuilder.setDescription("Your current place in the global server leaderboard is `#" + leaderboardPlace + "/" + CafeBot.getJDA().getGuilds().size() + "`.");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("These statistics are for the current server only.");
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
        arrayList.add("countingleaderboard");
        arrayList.add("counting-leaderboard");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Get the counting stats for the server!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "counting-stats`";
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
