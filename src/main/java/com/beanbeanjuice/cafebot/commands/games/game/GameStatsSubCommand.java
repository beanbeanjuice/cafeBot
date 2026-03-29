package com.beanbeanjuice.cafebot.commands.games.game;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameStatusType;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
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

            event.getHook().sendMessageEmbeds(gameStatisticsEmbed(user, gamesPlayed, gamesWon, gamesLost, gamesPlayedByType, gameWins, gameLosses, ctx.getGuildI18n())).queue();

        });

    }

    private MessageEmbed gameStatisticsEmbed(
            User user,
            int gamesPlayed,
            int gamesWon,
            int gamesLost,
            HashMap<GameType, Integer> gameTotals,
            HashMap<GameType, Integer> gameWins,
            HashMap<GameType, Integer> gameLosses,
            I18N bundle
    ) {
        String title = bundle.getString("command.game.subcommands.stats.embed.title").replace("{user}", user.getName());
        String description = bundle.getString("command.game.subcommands.stats.embed.stats");

        String totalsTitle = bundle.getString("command.game.subcommands.stats.embed.fields.total.name");
        String totalsDescription = description
                .replace("{wins}", String.valueOf(gamesWon))
                .replace("{losses}", String.valueOf(gamesLost))
                .replace("{played}", String.valueOf(gamesPlayed));

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(title, null, user.getAvatarUrl());

        embedBuilder.addField(totalsTitle, totalsDescription, true);

        Arrays.stream(GameType.values()).forEach((type) -> {
            String gameDescription = description
                    .replace("{wins}", String.valueOf(gameWins.get(type)))
                    .replace("{losses}", String.valueOf(gameLosses.get(type)))
                    .replace("{played}", String.valueOf(gameTotals.get(type)));

            embedBuilder.addField(
                    String.format("**%s**", type.getFriendlyName()),
                    gameDescription,
                    true
            );
        });

        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setFooter(bundle.getString("command.game.subcommands.stats.embed.footer"));

        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescriptionPath() {
        return "command.game.subcommands.stats.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "command.game.subcommands.stats.arguments.user.description", false)
        };
    }

}
