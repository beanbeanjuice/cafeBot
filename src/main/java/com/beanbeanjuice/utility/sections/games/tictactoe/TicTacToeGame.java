package com.beanbeanjuice.utility.sections.games.tictactoe;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sql.SQLServer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class TicTacToeGame {

    private String[][] board;
    private boolean[][] takenSpots;
    private User currentUser;
    private User player1;
    private User player2;

    private String currentMessageID = null;
    private String currentTextChannelID = null;
    private String guildID;

    private Timer gameTimer;
    private TimerTask gameTimerTask;

    private final int TIME_UNTIL_END = 60;

    public TicTacToeGame(@NotNull User player1, @NotNull User player2, @NotNull TextChannel textChannel) {
        this.player1 = player1;
        this.player2 = player2;
        this.guildID = textChannel.getGuild().getId();
        this.currentTextChannelID = textChannel.getId();

        board = new String[3][3];
        takenSpots = new boolean[3][3];
        fillBoard();
    }

    public void startGame() {
        gameTimer = new Timer();
        gameTimerTask = new TimerTask() {

            int count = 0;

            @Override
            public void run() {

                // Checking if no one has responded in 60 seconds.
                if (count++ >= TIME_UNTIL_END) {
                    CafeBot.getTicTacToeHandler().stopGame(guildID);
                    gameTimer.cancel();
                    return;
                }

                // Checking if the TextChannel is null.
                TextChannel textChannel;

                try {
                    textChannel = CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(currentTextChannelID);
                } catch (NullPointerException e) {
                    CafeBot.getTicTacToeHandler().stopGame(guildID);
                    gameTimer.cancel();
                    return;
                }

                Message message;

                try {
                    message = textChannel.getHistory().getMessageById(currentMessageID);
                } catch (NullPointerException e) {
                    CafeBot.getTicTacToeHandler().stopGame(guildID);
                    gameTimer.cancel();
                    return;
                }

                // TODO: Make sure that text channel is null. I don't know if you need to get it async or not.
                // TODO: Make sure the person who reacted is the person who is supposed to be next.
                // TODO: CHeck if an X is reacted, then stop the timer as the game has been cancelled.
            }
        };
        gameTimer.scheduleAtFixedRate(gameTimerTask, 0, 1000);
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

    public void setCurrentMessageID(@NotNull String messageID) {
        currentMessageID = messageID;
    }

    public String getCurrentMessageID() {
        return currentMessageID;
    }

    public Boolean useTurn(@NotNull Integer x, @NotNull Integer y) {
        if (!takenSpots[x][y]) {
            takenSpots[x][y] = true;

            if (currentUser.equals(player1)) {
                board[x][y] = "❌";
                currentUser = player2;
            }

            if (currentUser.equals(player2)) {
                board[x][y] = "🔵";
                currentUser = player1;
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
