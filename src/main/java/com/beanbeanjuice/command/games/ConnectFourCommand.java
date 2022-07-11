package com.beanbeanjuice.command.games;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.section.game.connectfour.ConnectFourGame;
import com.beanbeanjuice.utility.section.game.connectfour.ConnectFourHandler;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used for {@link ConnectFourGame}.
 *
 * @author beanbeanjuice
 */
public class ConnectFourCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        User player1 = event.getUser();
        User player2 = event.getOption("opponent").getAsUser();

        if (player1.equals(player2)) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Cannot Play Yourself",
                    "You cannot play a connect four game against yourself!"
            )).queue();
            return;
        }

        if (player2.isBot()) {
            event.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                    "Cannot Play Against Bot",
                    "You cannot play this game against a bot!"
            )).queue();
            return;
        }

        ConnectFourGame game = new ConnectFourGame(player1, player2, event.getTextChannel());
        if (!ConnectFourHandler.createGame(event.getGuild().getId(), game)) {
            event.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Creating Connect Four Game",
                    "There is already an active connect four game on this server. Please wait for it to end."
            )).queue();
        }

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Created Game!",
                "Your connect-4 game has been created!"
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Play connect-4 with someone!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/connect-4 @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "opponent", "Your opponent for this connect-four game!", true));
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
        return false;
    }

}
