package com.beanbeanjuice.cafebot.utility.listeners.games.tictactoe;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameStatusType;
import com.beanbeanjuice.cafebot.api.wrapper.type.Game;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.handlers.games.TicTacToeHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.Component;
import net.dv8tion.jda.api.components.MessageTopLevelComponent;
import net.dv8tion.jda.api.components.MessageTopLevelComponentUnion;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageEditAction;

import java.util.Arrays;
import java.util.List;

public class TicTacToeListener extends ListenerAdapter {

    private final CafeBot bot;

    public TicTacToeListener(CafeBot bot) {
        this.bot = bot;
        bot.addEventListener(new TicTacToeConsentListener(bot));
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getUser().isBot()) return;

        String id = event.getComponentId();
        if (!id.startsWith(TicTacToeHandler.GAME_BUTTON_ID)) return;
        id = id.replace(TicTacToeHandler.GAME_BUTTON_ID, "");

        event.deferEdit().queue();

        User currentPerson = event.getMessage().getMentions().getUsers().getFirst();
        if (!event.getUser().getId().equals(currentPerson.getId())) return;

        int gameId = Integer.parseInt(id.split(":")[0]);
        int buttonIdx = Integer.parseInt(id.split(":")[1]);
        String player1ID = id.split(":")[2];
        String player2ID = id.split(":")[3];

        Guild guild = event.getGuild();
        guild.retrieveMembersByIds(player1ID, player2ID).onSuccess((members) -> {
            Member player1 = members.get(0).getId().equals(player1ID) ? members.get(0) : members.get(1);
            Member player2 = members.get(1).getId().equals(player2ID) ? members.get(1) : members.get(0);
            boolean isPlayer1 = currentPerson.getId().equals(player1ID);

            if (player1 == null || player2 == null) {
                event.editMessage(String.format("**Game Cancelled**: Cannot get players %s and %s...", player1ID, player2ID)).setReplace(true).queue();
                return;
            }

            Button button = event.getButton();
            ButtonStyle style = (isPlayer1) ? ButtonStyle.SUCCESS : ButtonStyle.DANGER;
            Emoji emoji = (isPlayer1) ? Emoji.fromFormatted("U+1F1FD") : Emoji.fromFormatted("U+1F1F4");

            Result result = checkGame(buttonIdx, isPlayer1, event.getMessage().getComponents());
            event.editButton(button.withStyle(style).withEmoji(emoji).asDisabled()).queue((ignored) -> {
                String formatString = String.format(
                        "%s (:regional_indicator_x:) vs %s (:regional_indicator_o:) : **%s**",
                        (isPlayer1) ? player1.getEffectiveName() : player1.getAsMention(),
                        (isPlayer1) ? player2.getAsMention() : player2.getEffectiveName(),
                        result.getMessage()
                );

                MessageEditAction action = event.getMessage().editMessage(formatString);

                if (result != Result.ONGOING) {
                    List<MessageTopLevelComponent> disabledRows = event.getMessage().getComponents().stream()
                            .filter(top -> top.getType() == Component.Type.ACTION_ROW)
                            .map(top -> (MessageTopLevelComponent) top.asActionRow().asDisabled()) // cast here
                            .toList();

                    action.setComponents(disabledRows);

                    if (result != Result.TIED) {
                        updateWins(gameId, player1, player2, result, event.getMessage());
                        return;
                    }

                    if (result == Result.TIED) {
                        endGameWithTie(gameId, player1, player2, event.getMessage());
                        return;
                    }
                }

                action.queue();
            });
        });

    }

    private void endGameWithTie(int gameId, Member player1, Member player2, Message message) {
        bot.getCafeAPI().getGamesApi().updateGame(gameId, GameStatusType.DRAW, new String[0]).thenAccept((endedGame) -> {
            message.editMessageEmbeds(getTiedGameEmbed(endedGame, player1, player2)).queue();
        });
    }

    private MessageEmbed getTiedGameEmbed(Game game, Member player1, Member player2) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Tic Tac Toe");
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setFooter("Type /tictactoe to play again!");

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s tied against %s! No one won...", player1.getAsMention(), player2.getAsMention()));

        if (game.getPool() != 0) sb.append(String.format("\n\nAdditionally, %d was returned to both players...", game.getWager()));

        embedBuilder.setDescription(sb.toString());

        return embedBuilder.build();
    }

    private void updateWins(int gameId, Member player1, Member player2, Result result, Message message) {
        Member winner = (result == Result.PLAYER1) ? player1 : player2;
        Member loser = (result == Result.PLAYER1) ? player2 : player1;

        bot.getCafeAPI().getGamesApi().updateGame(gameId, GameStatusType.FINISHED, new String[]{ winner.getId() }).thenAccept((endedGame) -> {
            message.editMessageEmbeds(getGameWonEmbed(endedGame, winner, loser)).queue();
        });
    }

    private MessageEmbed getGameWonEmbed(Game game, Member winner, Member loser) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Tic Tac Toe");
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setFooter("Type /tictactoe to play again!");
        embedBuilder.setAuthor(winner.getNickname(), null, winner.getAvatarUrl());

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s **beat** %s!... ~~how embarrassing...~~", winner.getAsMention(), loser.getAsMention()));

        if (game.getPool() != 0) sb.append(String.format("\n\nOn top of that... %s won %d!", winner.getAsMention(), game.getPool()));

        embedBuilder.setDescription(sb.toString());

        return embedBuilder.build();
    }

    private Result checkGame(final int index, final boolean isPlayer1, final List<MessageTopLevelComponentUnion> componentLayouts) {
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

    private List<Square> getBoard(final int index, final boolean isPlayer1, List<MessageTopLevelComponentUnion> layouts) {
        Square[] game = new Square[9];

        // Iterate each top‑level component
        for (MessageTopLevelComponentUnion union : layouts) {
            // Only consider action rows (rows of interactive components)
            if (union.getType() == Component.Type.ACTION_ROW) {
                ActionRow row = union.asActionRow();

                // For each button in the row…
                for (Button button : row.getButtons()) {
                    String buttonId = button.getCustomId(); // new name for getId()
                    if (buttonId == null) continue;

                    buttonId = buttonId.replace(TicTacToeHandler.GAME_BUTTON_ID, "");

                    int buttonIndex = Integer.parseInt(buttonId.split(":")[1]);
                    if (!button.isDisabled()) {
                        game[buttonIndex] = Square.NONE;
                    } else {
                        // STYLE_SUCCESS == p1, otherwise p2
                        game[buttonIndex] = button.getStyle() == ButtonStyle.SUCCESS
                                ? Square.PLAYER1
                                : Square.PLAYER2;
                    }
                }
            }
        }

        // Set current move
        game[index] = isPlayer1 ? Square.PLAYER1 : Square.PLAYER2;
        return Arrays.stream(game).toList();
    }

    private enum Result {
        PLAYER1 (":regional_indicator_x: WINS"),
        PLAYER2 (":regional_indicator_o: WINS"),
        TIED ("TIED"),
        ONGOING ("ONGOING");

        @Getter private final String message;

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
