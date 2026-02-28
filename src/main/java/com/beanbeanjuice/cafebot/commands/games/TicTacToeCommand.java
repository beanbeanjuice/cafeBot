package com.beanbeanjuice.cafebot.commands.games;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.handlers.games.TicTacToeHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class TicTacToeCommand extends Command implements ICommand {

    public TicTacToeCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        int wager = Optional.ofNullable(event.getOption("wager")).map(OptionMapping::getAsInt).orElse(0);

        User opponent = event.getOption("opponent").getAsUser();

        if (opponent.isBot()) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Cannot Play Against Bot",
                    "What do you think you're even doing? <:cafeBot_angry:1171726164092518441>"
            )).queue();
            return;
        }

        Member player = event.getMember();
        String playerId = player.getId();
        String opponentId = opponent.getId();

        event.getHook().sendMessageEmbeds(
                Helper.successEmbed(
                        "Waiting for Consent",
                        String.format("%s, do you accept the challenge from %s for **%d cC** (cafeCoins)? Once accepted, games will be automatically closed after 24 hours.", opponent.getAsMention(), player.getAsMention(), wager)
                        )
                )
                .addComponents(ActionRow.of(Button.primary(String.format(TicTacToeHandler.CONSENT_BUTTON_ID_FULL, playerId, opponentId, wager), Emoji.fromUnicode("🤝"))))
                .queue();
    }

    @Override
    public String getName() {
        return "tictactoe";
    }

    @Override
    public String getDescriptionPath() {
        return "Play tic-tac-toe with someone!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GAME;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "opponent", "The person you want to play against.", true),
                new OptionData(OptionType.INTEGER, "wager", "The amount you want to wager for this game.", false)
                        .setMinValue(1)
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
