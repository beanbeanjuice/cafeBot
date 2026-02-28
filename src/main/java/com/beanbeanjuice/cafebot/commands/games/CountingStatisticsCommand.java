package com.beanbeanjuice.cafebot.commands.games;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.api.wrapper.type.CountingStatistics;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class CountingStatisticsCommand extends Command implements ICommand {

    public CountingStatisticsCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        Guild guild = event.getGuild();
        String guildId = guild.getId();

        bot.getCafeAPI().getCountingApi().getCountingStatistics().thenAccept((globalCountingMap) -> {
            int highestRanking = getHighestGlobalRanking(globalCountingMap, guildId);
            int currentRanking = getCurrentGlobalRanking(globalCountingMap, guildId);
            CountingStatistics guildStatistics = globalCountingMap.get(guild.getId());

            event.getHook().sendMessageEmbeds(getStatisticsEmbed(guildStatistics, highestRanking, currentRanking, globalCountingMap.size())).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Counting Statistics Error",
                    "I'm really.. sorry... I'm having trouble getting the global counting statistics..."
            )).queue();

            bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Getting Counting Statistics: %s", ex.getMessage()));
            throw new CompletionException(ex);
        });
    }

    private int getCurrentGlobalRanking(Map<String, CountingStatistics> globalCountingMap, String guildId) {
        Set<CountingStatistics> globalStatistics = new HashSet<>(globalCountingMap.values());
        globalStatistics = globalStatistics.stream().sorted((a, b) -> b.getCurrentCount() - a.getCurrentCount()).collect(Collectors.toCollection(LinkedHashSet::new));

        int ranking = 1;

        for (CountingStatistics statistic : globalStatistics) {
            if (statistic.getGuildId().equals(guildId)) return ranking;
            ranking++;
        }

        return ranking;
    }

    private int getHighestGlobalRanking(Map<String, CountingStatistics> globalCountingMap, String guildId) {
        Set<CountingStatistics> globalStatistics = new HashSet<>(globalCountingMap.values());
        globalStatistics = globalStatistics.stream().sorted((a, b) -> b.getHighestCount() - a.getHighestCount()).collect(Collectors.toCollection(LinkedHashSet::new));

        int ranking = 1;

        for (CountingStatistics statistic : globalStatistics) {
            if (statistic.getGuildId().equals(guildId)) return ranking;
            ranking++;
        }

        return ranking;
    }

    private MessageEmbed getStatisticsEmbed(@Nullable CountingStatistics statistics, int highestRanking, int currentRanking, int numGuildsInRanking) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Global Counting Statistics ➕");
        embedBuilder.setColor(Helper.getRandomColor());

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("""
                ***Highest Ranking***: %d/%d
                *This is the ranking for your* ***highest*** *number.*
                
                ***Current Ranking***: %d/%d
                *This is the ranking for your* ***current*** *number.*
                """,
                highestRanking, numGuildsInRanking,
                currentRanking, numGuildsInRanking)
        );

        if (statistics != null) {
            sb.append(String.format("""
                    
                    Your highest number is **%d**!
                    Your current number is **%d**! This means your next number should be **%d**...
                    """,
                    statistics.getHighestCount(),
                    statistics.getCurrentCount(), statistics.getCurrentCount() + 1)
            );
        }

        embedBuilder.setDescription(sb.toString());

        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "counting-statistics";
    }

    @Override
    public String getDescriptionPath() {
        return "Get the server's global counting statistics!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GAME;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return true;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return false;
    }

}
