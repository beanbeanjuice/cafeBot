package com.beanbeanjuice.cafebot.commands.games.game;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameStatusType;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class GameStatsSubCommand extends Command implements ISubCommand {

    public GameStatsSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));

        User user = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());

        bot.getCafeAPI().getGamesApi().getGames(user.getId()).thenAccept((games) -> {

            int gamesPlayed = games.size();
            int gamesWon = Math.toIntExact(games.stream().filter((game) -> Arrays.stream(game.getWinners()).anyMatch(user.getId()::equalsIgnoreCase)).count());
            int gamesLost = games.size() - gamesWon;

            HashMap<GameType, Integer> gameWins = new HashMap<>();
            HashMap<GameType, Integer> gameLosses = new HashMap<>();
            HashMap<GameType, Integer> gamesPlayedByType = new HashMap<>();

            Arrays.stream(GameType.values()).forEach(type -> {
                gamesPlayedByType.putIfAbsent(type, 0);
                gameWins.putIfAbsent(type, 0);
                gameLosses.putIfAbsent(type, 0);
            });

            games.forEach((game) -> {
                int currentPlayedTotal = gamesPlayedByType.get(game.getType());
                int currentWins = gameWins.get(game.getType());
                int currentLosses = gameLosses.get(game.getType());

                if (Arrays.stream(game.getWinners()).anyMatch(user.getId()::equalsIgnoreCase)) {
                    currentWins++;
                }

                if (game.getStatus() == GameStatusType.FINISHED && Arrays.stream(game.getWinners()).noneMatch(user.getId()::equalsIgnoreCase)) {
                    currentLosses++;
                }

                gamesPlayedByType.put(game.getType(), ++currentPlayedTotal);
                gameWins.put(game.getType(), currentWins);
                gameLosses.put(game.getType(), currentLosses);
            });

            event.getHook().sendMessageEmbeds(gameStatisticsEmbed(user, gamesPlayed, gamesWon, gamesLost, gamesPlayedByType, gameWins, gameLosses)).queue();

        });

    }

    private MessageEmbed gameStatisticsEmbed(
            User user,
            int gamesPlayed,
            int gamesWon,
            int gamesLost,
            HashMap<GameType, Integer> gameTotals,
            HashMap<GameType, Integer> gameWins,
            HashMap<GameType, Integer> gameLosses
    ) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(String.format("%s's Game Statistics", user.getName()), null, user.getAvatarUrl());

        embedBuilder.addField("**Totals** 🎯", String.format("**Wins**: %s\n**Losses**: %s\n**Played**: %s", gamesWon, gamesLost, gamesPlayed), true);

        Arrays.stream(GameType.values()).forEach((type) -> {
            embedBuilder.addField(
                    String.format("**%s**", type.getFriendlyName()),
                    String.format("**Wins**: %s\n**Losses**: %s\n**Played**: %s", gameWins.get(type), gameLosses.get(type), gameTotals.get(type)),
                    true
            );
        });

        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setFooter("Do you want to play a game?~");

        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescriptionPath() {
        return "Get someone's game stats!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The person who's game data you want to see.", false)
        };
    }

}
