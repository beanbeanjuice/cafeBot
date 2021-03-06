package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.handler.CountingHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.cafeapi.cafebot.counting.CountingInformation;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} to get the current number for the {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class CountingStatisticsCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        CountingInformation countingInformation = CountingHandler.getCountingInformation(event.getGuild());

        // Checks if there was an error getting the counting information.
        if (countingInformation == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting Counting Information",
                    "There has been an error retrieving this Discord server's counting information."
            )).queue();
            return;
        }

        Integer leaderboardPlace = CountingHandler.getCountingLeaderboardPlace(countingInformation.getHighestNumber());
        if (leaderboardPlace == null) {
            event.getHook().sendMessageEmbeds(Helper.sqlServerError(
                    "There was an error getting counting statistics. There are eitehr no counting statistics for this server " +
                            "on this bot, or there is an SQL server error."
            )).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(countingStatisticsEmbed(countingInformation.getHighestNumber(), countingInformation.getLastNumber(), leaderboardPlace)).queue();
    }

    @NotNull
    private MessageEmbed countingStatisticsEmbed(@NotNull Integer highestNumber, @NotNull Integer currentNumber, @NotNull Integer leaderboardPlace) {
        return new EmbedBuilder()
                .setTitle("Current Number")
                .addField("Highest Number", highestNumber.toString(), true)
                .addField("Current Number", currentNumber.toString(), true)
                .setDescription("Your current place in the global server leaderboard is `#" + leaderboardPlace + "/" + Bot.getBot().getGuilds().size() + "`.")
                .setColor(Helper.getRandomColor())
                .setFooter("These statistics are for the current server only.")
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get the counting statistics for the server!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/counting-statistics`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.FUN;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}
