package com.beanbeanjuice.cafebot.commands.games.game;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks.MinigameType;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks.WinStreak;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataSubCommand extends Command implements ISubCommand {

    public DataSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));

        User user = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());

        cafeBot.getCafeAPI()
                .getWinStreaksEndpoint()
                .getAndCreateUserWinStreak(user.getId())
                .thenAcceptAsync((winstreak) -> {
                    event.getHook().sendMessageEmbeds(gameDataEmbed(user, winstreak)).queue();
                });
    }

    private MessageEmbed gameDataEmbed(final User user, final WinStreak winStreak) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(String.format("%s's Game Win Streaks", user.getName()), null, user.getAvatarUrl());

        String description = Arrays.stream(MinigameType.values())
                .map((type) -> Pair.of(type.getName(), winStreak.getWins(type)))
                .map((streak) -> String.format("**%s** - %d (In a Row)", streak.getLeft(), streak.getRight()))
                .collect(Collectors.joining("\n"));

        embedBuilder.setDescription(description).setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "data";
    }

    @Override
    public String getDescription() {
        return "Get someone's game data!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The person who's game data you want to see.", false)
        };
    }

}
