package com.beanbeanjuice.cafebot.commands.games;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class TicTacToeCommand extends Command implements ICommand {

    public TicTacToeCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        // cafeBot:tictactoe:index:user1:user2
        User opponent = event.getOption("opponent").getAsUser();

        String playerID = event.getUser().getId();
        String opponentID = opponent.getId();

        event.getHook()
                .sendMessage(String.format("%s vs %s", event.getUser().getAsMention(), opponent.getEffectiveName()))
                .addComponents(
                        ActionRow.of(getButton(0, playerID, opponentID), getButton(1, playerID, opponentID), getButton(2, playerID, opponentID)),
                        ActionRow.of(getButton(3, playerID, opponentID), getButton(4, playerID, opponentID), getButton(5, playerID, opponentID)),
                        ActionRow.of(getButton(6, playerID, opponentID), getButton(7, playerID, opponentID), getButton(8, playerID, opponentID))
                )
                .queue();
    }

    private Button getButton(final int index, final String user1ID, final String user2ID) {
        return Button.secondary(String.format("cafeBot:tictactoe:%d:%s:%s", index, user1ID, user2ID), Emoji.fromFormatted("❓"));
    }

    @Override
    public String getName() {
        return "tictactoe";
    }

    @Override
    public String getDescription() {
        return "Play tic-tac-toe with someone!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GAME;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "opponent", "The person you want to play against.", true)
        };
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return false;
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
