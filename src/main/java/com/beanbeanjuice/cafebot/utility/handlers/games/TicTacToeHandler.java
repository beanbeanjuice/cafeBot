package com.beanbeanjuice.cafebot.utility.handlers.games;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameType;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.concurrent.CompletionException;

@RequiredArgsConstructor
public class TicTacToeHandler {

    private final CafeBot bot;
    public static final String CONSENT_BUTTON_ID        = "cafeBot:tictactoe:consent:";
    public static final String CONSENT_BUTTON_ID_FULL   = "cafeBot:tictactoe:consent:%s:%s:%d"; // player1Id:player2Id:wager
    public static final String GAME_BUTTON_ID           = "cafeBot:tictactoe:game:";
    public static final String GAME_BUTTON_ID_FULL      = "cafeBot:tictactoe:game:%d:%d:%s:%s"; // game_id:button_idx:player1ID:player2ID

    /**
     * Start a game once someone has pushed the appropriate button.
     * The person who clicked the consent button must be player2.
     * @param event The {@link ButtonInteractionEvent event} that was fired.
     */
    public void startGame(ButtonInteractionEvent event, Member player1, Member player2, int wager) {
        String player1Id = player1.getId();
        String player2Id = player2.getId();

        bot.getCafeAPI().getGamesApi().createGame(GameType.TIC_TAC_TOE, wager, new String[]{player1Id, player2Id}).thenAccept((game) -> {
            event.getMessage()
                    .editMessage(String.format("%s vs %s for **%d cC**", player1.getAsMention(), player2.getEffectiveName(), wager))
                    .setComponents(
                            ActionRow.of(getButton(game.getId(), 0, player1Id, player2Id), getButton(game.getId(), 1, player1Id, player2Id), getButton(game.getId(), 2, player1Id, player2Id)),
                            ActionRow.of(getButton(game.getId(), 3, player1Id, player2Id), getButton(game.getId(), 4, player1Id, player2Id), getButton(game.getId(), 5, player1Id, player2Id)),
                            ActionRow.of(getButton(game.getId(), 6, player1Id, player2Id), getButton(game.getId(), 7, player1Id, player2Id), getButton(game.getId(), 8, player1Id, player2Id))
                    )
                    .setEmbeds() // clear old consent embed.
                    .queue();
        }).exceptionally((ex) -> {
            if (ex.getCause() instanceof ApiRequestException apiRequestException) {
                event.getMessage()
                        .editMessageEmbeds(
                                Helper.errorEmbed(
                                        "Error Starting Game",
                                        apiRequestException.getBody().get("error").get("players").get(0).asString()
                                )
                        )
                        .queue();
                throw new CompletionException(ex);
            }

            bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Creating Tic-Tac-Toe Game: %s", ex.getMessage()));
            throw new CompletionException(ex);
        });
    }

    private static Button getButton(int gameId, int buttonIndex, String user1Id, String user2Id) {
        return Button.secondary(String.format(GAME_BUTTON_ID_FULL, gameId, buttonIndex, user1Id, user2Id), Emoji.fromFormatted("❓"));
    }

}
