package com.beanbeanjuice.cafebot.commands.fun.counting;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.counting.CountingInformation;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.*;

public class CountingStatisticsSubCommand extends Command implements ISubCommand {

    public CountingStatisticsSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        cafeBot.getCafeAPI().getCountingEndpoint().getAllCountingInformation().thenAcceptAsync((countingMap) -> {
            String guildID = event.getGuild().getId();
            if (!countingMap.containsKey(guildID)) {
                event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                        "Counting Not Set",
                        "This server does not have counting setup."
                )).queue();
                return;
            }

            int leaderboardPlace = getLeaderboardPlace(countingMap, countingMap.get(guildID));
            event.getHook().sendMessageEmbeds(this.countingStatisticsEmbed(countingMap.get(guildID), leaderboardPlace)).queue();
        });
    }

    private MessageEmbed countingStatisticsEmbed(final CountingInformation countingInformation, final int leaderboardPlace) {
        return new EmbedBuilder()
                .setTitle("Current Number")
                .addField("Highest Number", String.valueOf(countingInformation.getHighestNumber()), true)
                .addField("Current Number", String.valueOf(countingInformation.getLastNumber()), true)
                .setDescription(String.format(
                    """
                    Your current place in the global server leaderboard is \
                    **%d/%d**.
                    """, leaderboardPlace, cafeBot.getJDA().getGuilds().size()))
                .setColor(Helper.getRandomColor())
                .setFooter("These statistics are for the current server only.")
                .build();
    }

    private int getLeaderboardPlace(final HashMap<String, CountingInformation> leaderboardMap, final CountingInformation information) {
        List<Map.Entry<String, Integer>> lastNumberArray = leaderboardMap.entrySet().stream()
                .map((entry) -> Map.entry(entry.getKey(), entry.getValue().getLastNumber()))
                .sorted(Map.Entry.comparingByValue())
                .toList();

        for (int i = 0; i < lastNumberArray.size(); i++) {
            if (lastNumberArray.get(i).getValue() < information.getLastNumber()) return i;
        }

        return lastNumberArray.size();
    }

    @Override
    public String getName() {
        return "statistics";
    }

    @Override
    public String getDescription() {
        return "Get counting statistics for this server!";
    }
}
