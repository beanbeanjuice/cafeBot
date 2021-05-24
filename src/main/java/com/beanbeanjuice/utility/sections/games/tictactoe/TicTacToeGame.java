package com.beanbeanjuice.utility.sections.games.tictactoe;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sql.SQLServer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class TicTacToeGame {

    private String[][] board;
    private boolean[][] takenSpots;
    private User currentUser;
    private User player1;
    private User player2;

    private String currentMessageID = null;
    private String currentTextChannelID;
    private String guildID;

    private Timer gameTimer;
    private TimerTask gameTimerTask;

    private final int TIME_UNTIL_END = 60;
    private int count = 0;

    private HashMap<Long, Consumer<MessageReaction>> emojiListeners = new HashMap<>();
    private ListenerAdapter reactionListener;

    public TicTacToeGame(@NotNull User player1, @NotNull User player2, @NotNull TextChannel textChannel) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentUser = player1;
        this.guildID = textChannel.getGuild().getId();
        this.currentTextChannelID = textChannel.getId();

        board = new String[3][3];
        takenSpots = new boolean[3][3];
        fillBoard();

        reactionListener = new ListenerAdapter() {
            @Override
            public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
                Consumer<MessageReaction> callback = emojiListeners.get(event.getMessageIdLong());

                if (callback != null) {
                    callback.accept(event.getReaction());
                }

                System.out.println("CHECKED REACTION");
            }
        };

        CafeBot.getJDA().addEventListener(reactionListener);
    }

    public void startGame() {
        startGameTimer();

        try {
            sendMessage();
        } catch (NullPointerException e) {
            stopGameTimer();
            return;
        }

//        while (checkGameExists()) {
//
//
//        }
    }

    private void sendMessage() throws NullPointerException {
        CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(currentTextChannelID).sendMessage(getBoardEmbed()).queue(message -> {
            currentMessageID = message.getId();
            addReactions(message);

            System.out.println("ADDED REACTIONS");
            emojiListeners.put(message.getIdLong(), (r) -> {

                System.out.println("CHECKING REACTION");
                if (r.getReactionEmote().isEmoji() && !r.isSelf()) {
                    r.retrieveUsers().queue();
                    message.clearReactions().queue();
                    message.addReaction(r.getReactionEmote().getEmoji()).queue();
                    stopGameTimer(); // TODO: Not this
                    System.out.println("FINISHING GAME");
                    emojiListeners.remove(r.getMessageIdLong());
                }

            });

        });
    }

    private void addReactions(Message message) {
        if (!takenSpots[0][2]) {
            message.addReaction("1️⃣").queue();
        }
        if (!takenSpots[1][2]) {
            message.addReaction("2️⃣").queue();
        }
        if (!takenSpots[2][2]) {
            message.addReaction("3️⃣").queue();
        }
        if (!takenSpots[0][1]) {
            message.addReaction("4️⃣").queue();
        }
        if (!takenSpots[1][1]) {
            message.addReaction("5️⃣").queue();
        }
        if (!takenSpots[2][1]) {
            message.addReaction("6️⃣").queue();
        }
        if (!takenSpots[0][0]) {
            message.addReaction("7️⃣").queue();
        }
        if (!takenSpots[1][0]) {
            message.addReaction("8️⃣").queue();
        }
        if (!takenSpots[2][0]) {
            message.addReaction("9️⃣").queue();
        }

        message.addReaction("❌").queue();
    }

    private Boolean checkGameExists() {
        TextChannel textChannel;
        try {
            textChannel = CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(currentTextChannelID);
        } catch (NullPointerException e) {
            return false;
        }

        Message message;
        try {
            message = textChannel.getHistory().getMessageById(currentMessageID);
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    private void startGameTimer() {
        gameTimer = new Timer();
        gameTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (count++ >= TIME_UNTIL_END) {
                    stopGameTimer();
                    return;
                }
            }
        };
        gameTimer.scheduleAtFixedRate(gameTimerTask, 0, 1000);
    }

    private void stopGameTimer() {
        System.out.println("STOPPING TIMER");
        gameTimer.cancel();
        CafeBot.getTicTacToeHandler().stopGame(this);
        CafeBot.getJDA().removeEventListener(reactionListener);
    }

    @NotNull
    public String getGuildID() {
        return guildID;
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
