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
            String title = ctx.getUserI18n().getString("command.tictactoe.embeds.bot.title");
            String description = ctx.getUserI18n().getString("command.tictactoe.embeds.bot.description");

            event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();
            return;
        }

        Member player = event.getMember();
        String playerId = player.getId();
        String opponentId = opponent.getId();

        String title = ctx.getUserI18n().getString("command.tictactoe.embeds.consent.title");
        String description = ctx.getUserI18n().getString("command.tictactoe.embeds.consent.description")
                .replace("{player1}", opponent.getAsMention())
                .replace("{player2}", player.getAsMention())
                .replace("{wager}", String.valueOf(wager));

        event.getHook()
                .sendMessageEmbeds(Helper.successEmbed(title, description))
                .addComponents(ActionRow.of(Button.primary(String.format(TicTacToeHandler.CONSENT_BUTTON_ID_FULL, playerId, opponentId, wager), Emoji.fromUnicode("🤝"))))
                .queue();
    }

    @Override
    public String getName() {
        return "tictactoe";
    }

    @Override
    public String getDescriptionPath() {
        return "command.tictactoe.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GAME;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "opponent", "command.tictactoe.arguments.opponent.description", true),
                new OptionData(OptionType.INTEGER, "wager", "command.tictactoe.arguments.wager.description", false)
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
