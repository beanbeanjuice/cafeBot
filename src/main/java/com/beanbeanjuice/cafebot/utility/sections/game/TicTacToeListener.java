package com.beanbeanjuice.cafebot.utility.sections.game;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks.MinigameType;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks.WinStreak;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks.WinStreaksEndpoint;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.requests.restaction.MessageEditAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TicTacToeListener extends ListenerAdapter {

    private final WinStreaksEndpoint winStreaksEndpoint;

    public TicTacToeListener(final WinStreaksEndpoint winStreaksEndpoint) {
        this.winStreaksEndpoint = winStreaksEndpoint;
    }

    /*
    This will be in the format cafeBot:tictactoe:index:user1:user2
     */
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getComponentId().startsWith("cafeBot:tictactoe:")) return;

        User currentPerson = event.getMessage().getMentions().getUsers().getFirst();
        if (!event.getUser().getId().equals(currentPerson.getId())) return;

        int index = Integer.parseInt(event.getComponentId().split(":")[2]);
        String player1ID = event.getComponentId().split(":")[3];
        String player2ID = event.getComponentId().split(":")[4];

        Guild guild = event.getGuild();
        guild.retrieveMembersByIds(player1ID, player2ID).onSuccess((members) -> {
            Member player1 = members.get(0);
            Member player2 = members.get(1);
            boolean isPlayer1 = currentPerson.getId().equals(player1ID);

            if (player1 == null || player2 == null) {
                event.editMessage(String.format("**Game Cancelled**: Cannot get players %s and %s...", player1ID, player2ID)).setReplace(true).queue();
                return;
            }

            Button button = event.getButton();
            ButtonStyle style = (isPlayer1) ? ButtonStyle.SUCCESS : ButtonStyle.DANGER;
            Emoji emoji = (isPlayer1) ? Emoji.fromFormatted("U+1F1FD") : Emoji.fromFormatted("U+1F1F4");

            Result result = checkGame(index, isPlayer1, event.getMessage().getComponents());
            event.editButton(button.withStyle(style).withEmoji(emoji).asDisabled()).queue((ignored) -> {
                String formatString = String.format(
                        "%s (:regional_indicator_x:) vs %s (:regional_indicator_o:) : **%s**",
                        (isPlayer1) ? player1.getEffectiveName() : player1.getAsMention(),
                        (isPlayer1) ? player2.getAsMention() : player2.getEffectiveName(),
                        result.getMessage()
                );

                MessageEditAction action = event.getMessage().editMessage(formatString);

                if (result != Result.ONGOING) {
                    List<LayoutComponent> components = new ArrayList<>(event.getMessage().getComponents());
                    components.replaceAll(itemComponents -> itemComponents.withDisabled(true));
                    action.setComponents(components);

                    if (result != Result.TIED) {
                        updateWins(player1, player2, result, event.getMessage());
                    }
                }

                action.queue();
            });
        });

    }

    private void updateWins(final Member player1, final Member player2, final Result result, final Message message) {
        CompletableFuture<WinStreak> player1WinStreakFuture = winStreaksEndpoint.getAndCreateUserWinStreak(player1.getId());
        CompletableFuture<WinStreak> player2WinStreakFuture = winStreaksEndpoint.getAndCreateUserWinStreak(player2.getId());

        player1WinStreakFuture.thenCombineAsync(player2WinStreakFuture, (player1WinStreak, player2WinStreak) -> {
            int player1Wins = (result == Result.PLAYER1) ? player1WinStreak.getWins(MinigameType.TIC_TAC_TOE) + 1: 0;
            int player2Wins = (result == Result.PLAYER2) ? player2WinStreak.getWins(MinigameType.TIC_TAC_TOE) + 1: 0;

            winStreaksEndpoint.updateUserWinStreak(player1.getId(), MinigameType.TIC_TAC_TOE, player1Wins);
            winStreaksEndpoint.updateUserWinStreak(player2.getId(), MinigameType.TIC_TAC_TOE, player2Wins);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Helper.getRandomColor());
            embedBuilder.setDescription(String.format(
                    """
                    # Tic-Tac-Toe Win Streaks
                    **%s** - *%d Wins*
                    **%s** - *%d Wins*
                    """, player1.getEffectiveName(), player1Wins, player2.getEffectiveName(), player2Wins));

            message.replyEmbeds(embedBuilder.build()).queue();

            return true;
        });
    }

    private Result checkGame(final int index, final boolean isPlayer1, final List<LayoutComponent> componentLayouts) {
        List<Square> board = getBoard(index, isPlayer1, componentLayouts);
        Result result = (isPlayer1) ? Result.PLAYER1 : Result.PLAYER2;

        // Check rows
        for (int i = 0; i < 9; i += 3) {
            if (board.get(i) != Square.NONE && board.get(i) == board.get(i + 1) && board.get(i) == board.get(i + 2)) {
                return result;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board.get(i) != Square.NONE && board.get(i) == board.get(i + 3) && board.get(i) == board.get(i + 6)) {
                return result;
            }
        }

        // Check diagonals
        if (board.get(0) != Square.NONE && board.get(0) == board.get(4) && board.get(0) == board.get(8)) {
            return result;
        }

        if (board.get(2) != Square.NONE && board.get(2) == board.get(4) && board.get(2) == board.get(6)) {
            return result;
        }

        if (!board.contains(Square.NONE)) return Result.TIED;
        return Result.ONGOING;
    }

    private List<Square> getBoard(final int index, final boolean isPlayer1, final List<LayoutComponent> componentLayouts) {
        Square[] game = new Square[9];


        componentLayouts.forEach((layout) -> {
            layout.getButtons().forEach((button) -> {
                int buttonIndex = Integer.parseInt(button.getId().split(":")[2]);
                if (!button.isDisabled()) {
                    game[buttonIndex] = Square.NONE;
                    return;
                }

                game[buttonIndex] = button.getStyle().equals(ButtonStyle.SUCCESS) ? Square.PLAYER1 : Square.PLAYER2;
            });
        });
        game[index] = (isPlayer1) ? Square.PLAYER1 : Square.PLAYER2;

        return Arrays.stream(game).toList();
    }

    private enum Result {
        PLAYER1 (":regional_indicator_x: WINS"),
        PLAYER2 (":regional_indicator_o: WINS"),
        TIED ("TIED"),
        ONGOING ("ONGOING");

        @Getter final String message;

        Result(final String message) {
            this.message = message;
        }
    }

    private enum Square {
        PLAYER1,
        PLAYER2,
        NONE
    }

}
