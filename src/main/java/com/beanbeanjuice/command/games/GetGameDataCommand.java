package com.beanbeanjuice.command.games;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.section.game.WinStreakHandler;
import com.beanbeanjuice.cafeapi.cafebot.minigames.winstreaks.MinigameType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to get {@link com.beanbeanjuice.cafeapi.cafebot.minigames.winstreaks.WinStreak WinStreak} for a {@link User}.
 *
 * @author beanbeanjuice
 */
public class GetGameDataCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        User user = event.getUser();
        if (event.getOption("user") != null)
            user = event.getOption("user").getAsUser();

        event.getHook().sendMessageEmbeds(gameDataEmbed(user)).queue();
    }

    @NotNull
    private MessageEmbed gameDataEmbed(@NotNull User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(user.getName() + "'s Game Win Streaks");
        StringBuilder descriptionBuilder = new StringBuilder();

        for (MinigameType miniGame : MinigameType.values()) {
            descriptionBuilder.append("**").append(miniGame.getType()).append("**: ");
            descriptionBuilder.append("*").append(WinStreakHandler.getUserWinStreak(user.getId(), miniGame)).append("*\n");
        }

        embedBuilder.setDescription(descriptionBuilder.toString())
                .setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get yours or someone else's game data!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/get-game-data` or `/get-game-data @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "The user you want to get game data for!", false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GAMES;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}
