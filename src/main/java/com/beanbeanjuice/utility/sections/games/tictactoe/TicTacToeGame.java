package com.beanbeanjuice.utility.sections.games.tictactoe;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sql.SQLServer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private boolean hasWinner = false;

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
        }

    }

    private void sendMessage() throws NullPointerException {
        CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(currentTextChannelID).sendMessage(getBoardEmbed()).queue(message -> {
            currentMessageID = message.getId();
            addReactions(message);

            emojiListeners.put(message.getIdLong(), (r) -> {

                r.retrieveUsers().queue(users -> {

                    // Checking if someone cancelled the game.
                    if (users.contains(player1) || users.contains(player2)) {
                        if (r.getReactionEmote().getEmoji().equals("❌")) {
                            stopGameTimer();
                            if (checkGameExists()) {
                                CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(currentTextChannelID).retrieveMessageById(currentMessageID).queue(retrievedMessage -> {
                                    String title = retrievedMessage.getEmbeds().get(0).getTitle();
                                    String description = retrievedMessage.getEmbeds().get(0).getDescription();
                                    retrievedMessage.editMessage(endGameEmbed(title, description, "The game was cancelled.")).queue();
                                });
                            }
                            return;
                        }
                    }

                    if (users.contains(currentUser)) {
                        if (getBoardEmojis().contains(r.getReactionEmote().getEmoji()) && !r.isSelf()) {

                            if (!parseTurn(r.getReactionEmote())) {
                                return;
                            }

                            r.retrieveUsers().queue();
                            message.clearReactions().queue();
                            message.addReaction(r.getReactionEmote().getEmoji()).queue();
                            emojiListeners.remove(r.getMessageIdLong());

                            if (!hasWinner) {
                                sendMessage();
                            }
                            count = 0;
                        }
                    }
                });

            });

        });
    }

    private ArrayList<String> getBoardEmojis() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1️⃣");
        arrayList.add("2️⃣");
        arrayList.add("3️⃣");
        arrayList.add("4️⃣");
        arrayList.add("5️⃣");
        arrayList.add("6️⃣");
        arrayList.add("7️⃣");
        arrayList.add("8️⃣");
        arrayList.add("9️⃣");
        return arrayList;
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

        try {
            textChannel.retrieveMessageById(currentMessageID).queue();
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
                System.out.println(count);
                if (count++ >= TIME_UNTIL_END) {
                    stopGameTimer();

                    if (checkGameExists()) {
                        CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(currentTextChannelID).retrieveMessageById(currentMessageID).queue(retrievedMessage -> {
                            String title = retrievedMessage.getEmbeds().get(0).getTitle();
                            String description = retrievedMessage.getEmbeds().get(0).getDescription();
                            retrievedMessage.editMessage(endGameEmbed(title, description, "The game ended because you didn't respond in time.")).queue(e -> {
                                e.clearReactions().queue();
                                e.addReaction("❌").queue();
                            });
                        });
                    }
                }
            }
        };
        gameTimer.scheduleAtFixedRate(gameTimerTask, 0, 1000);
    }

    private MessageEmbed endGameEmbed(@NotNull String title, @NotNull String description, @NotNull String footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(Color.red);
        embedBuilder.setFooter(footer);
        return embedBuilder.build();
    }

    private void stopGameTimer() {
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
                checkGame("❌", player1);
                currentUser = player2;
            } else {
                board[x][y] = "🔵";
                checkGame("🔵", player2);
                currentUser = player1;
            }

            return true;
        }
        return false;
    }

    private void checkGame(String unicodeEmoji, User player) {

        boolean allSpotsTaken = true;

        for (int y = 2; y >= 0; y--) {
            for (int x = 0; x < 3; x++) {

                if (board[x][y].equals(unicodeEmoji)) {

                    try {
                        // Check above and below
                        if (board[x][y + 1].equals(unicodeEmoji) && board[x][y - 1].equals(unicodeEmoji)) {
                            winGame(player);
                            return;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}

                    try {
                        // Check left and right
                        if (board[x - 1][y].equals(unicodeEmoji) && board[x + 1][y].equals(unicodeEmoji)) {
                            winGame(player);
                            return;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}

                    try {
                        // Check up right and down left
                        if (board[x + 1][y + 1].equals(unicodeEmoji) && board[x - 1][y - 1].equals(unicodeEmoji)) {
                            winGame(player);
                            return;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}

                    try {
                        // Check up left and down right
                        if (board[x - 1][y + 1].equals(unicodeEmoji) && board[x + 1][y - 1].equals(unicodeEmoji)) {
                            winGame(player);
                            return;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}

                }

                if (takenSpots[x][y] == false) {
                    allSpotsTaken = false;
                }

            }

        }

        if (allSpotsTaken) {
            tieGame();
        }

    }

    private void tieGame() {
        hasWinner = true;
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

        boardBuilder.append("\n**There was a tie!**");
        embedBuilder.setDescription(boardBuilder.toString());
        stopGameTimer();

        if (checkGameExists()) {
            CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(currentTextChannelID)
                    .sendMessage(embedBuilder.build()).queue();
        }
    }

    private void winGame(@NotNull User user) {
        hasWinner = true;
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

        boardBuilder.append("\n").append("**").append(user.getName()).append("** wins!");
        embedBuilder.setDescription(boardBuilder.toString());
        stopGameTimer();

        if (checkGameExists()) {
            CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(currentTextChannelID)
                    .sendMessage(embedBuilder.build()).queue();
        }
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

        if (player1.equals(currentUser)) {
            boardBuilder.append("\n").append("**").append(player1.getName()).append("** vs ").append(player2.getName());
        } else {
            boardBuilder.append("\n").append(player1.getName()).append(" vs **").append(player2.getName()).append("**");
        }

        embedBuilder.setDescription(boardBuilder.toString());
        return embedBuilder.build();
    }

}
