package com.beanbeanjuice.utility.sections.games.tictactoe;

import com.beanbeanjuice.main.CafeBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class TicTacToeGame {

    String[][] board;
    boolean[][] takenSpots;
    public User currentUser;
    public User player1;
    public User player2;

    public TicTacToeGame(@NotNull User player1, @NotNull User player2) {
        this.player1 = player1;
        this.player2 = player2;

        board = new String[3][3];
        takenSpots = new boolean[3][3];
        fillBoard();
    }

    private void fillBoard() {
        board[0][2] = "1️⃣";
        board[1][2] = "2️⃣";
        board[2][2] = "3️⃣";
        board[0][1] = "4️⃣";
        board[1][1] = "5️⃣";
        board[2][1] = "6️⃣";
        board[0][0] = "7️⃣";
        board[1][0] = "8️⃣";
        board[2][0] = "9️⃣";
    }

    public Boolean useTurn(@NotNull Integer x, @NotNull Integer y) {
        if (!takenSpots[x][y]) {
            takenSpots[x][y] = true;

            if (currentUser.equals(player1)) {
                board[x][y] = "❌";
            }

            if (currentUser.equals(player2)) {
                board[x][y] = "🔵";
            }

            return true;
        }
        return false;
    }

    public Boolean parseTurn(MessageReaction.ReactionEmote emote) {
        String emoji = emote.getEmoji();

        int x;
        int y;

        switch (emoji) {

            case "1️⃣" -> {
                x = 0;
                y = 2;
            }

            case "2️⃣" -> {
                x = 1;
                y = 2;
            }

            case "3️⃣" -> {
                x = 2;
                y = 2;
            }

            case "4️⃣" -> {
                x = 0;
                y = 1;
            }

            case "5️⃣" -> {
                x = 1;
                y = 1;
            }

            case "6️⃣" -> {
                x = 2;
                y = 1;
            }

            case "7️⃣" -> {
                x = 0;
                y = 0;
            }

            case "8️⃣" -> {
                x = 1;
                y = 0;
            }

            case "9️⃣" -> {
                x = 2;
                y = 0;
            }

            default -> {
                return false;
            }

        }
        return useTurn(x, y);
    }

    public MessageEmbed getBoardEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Tic-Tac-Toe");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        StringBuilder boardBuilder = new StringBuilder();
        boardBuilder.append("———+———+———\n");
        for (int y = 2; y >= 0; y--) {
            for (int x = 0; x < 3; x++) {
                boardBuilder.append("|    ");
                if (x == 2) {
                    boardBuilder.append(" ");
                }
                boardBuilder.append(board[x][y]).append("     ");
            }
            boardBuilder.append("|\n———+———+———\n");
        }
        embedBuilder.setDescription(boardBuilder.toString());
        embedBuilder.setFooter(currentUser.getName() + "'s turn.");
        return embedBuilder.build();
    }

}
