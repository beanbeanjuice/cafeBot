package com.beanbeanjuice.cafebot.command.games;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} used for flipping coins.
 *
 * @author beanbeanjuice
 */
public class CoinFlipCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        int num = Helper.getRandomNumber(1, 3);

        if (num == 1) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Coin Toss!",
                    "Heads"
            )).queue();
            return;
        }

        if (num == 2) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Coin Toss!",
                    "Tails"
            )).queue();
            return;
        }
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Flip a coin!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/coin-flip`";
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

}
