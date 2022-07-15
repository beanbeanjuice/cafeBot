package com.beanbeanjuice.command.games;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.section.game.tictactoe.TicTacToeGame;
import com.beanbeanjuice.utility.section.game.tictactoe.TicTacToeHandler;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to play Tic-Tac-Toe.
 *
 * @author beanbeanjuice
 */
public class TicTacToeCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        User player1 = event.getUser();
        User player2 = event.getOption("opponent").getAsUser();

        if (player1.equals(player2)) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Cannot Play Yourself",
                    "You cannot play yourself!"
            )).queue();
            return;
        }

        if (player2.isBot()) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Cannot Play Bot",
                    "You cannot play against a bot!"
            )).queue();
            return;
        }

        TicTacToeGame game = new TicTacToeGame(player1, player2, event.getTextChannel());
        if (!TicTacToeHandler.createGame(event.getGuild().getId(), game)) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Creating Tic-Tac-Toe Game",
                    "There is already an active tic tac toe game on this server. Please wait for it to end."
            )).queue();
        }

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Created Game!",
                "Your tic-tac-toe game has been created!"
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Play tic-tac-toe with someone!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/tic-tac-toe @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "opponent", "The opponent for this tic-tac-toe game!", true));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GAMES;
    }

}
