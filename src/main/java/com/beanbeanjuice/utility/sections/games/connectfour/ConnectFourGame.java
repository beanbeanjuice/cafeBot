package com.beanbeanjuice.utility.sections.games.connectfour;

import com.beanbeanjuice.main.CafeBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ConnectFourGame {

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

    public ConnectFourGame(@NotNull User player1, @NotNull User player2, @NotNull TextChannel textChannel) {
        this.player1 = player1;
        this.currentUser = player1;
        this.player2 = player2;
        this.currentTextChannelID = textChannel.getId();
        this.guildID = textChannel.getGuild().getId();

        board = new String[7][6];
        takenSpots = new boolean[7][6];
        fillBoard();

        // Creates a new reaction listener for the current game.
        reactionListener = new ListenerAdapter() {
            @Override
            public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
                Consumer<MessageReaction> callback = emojiListeners.get(event.getMessageIdLong());

                if (callback != null) {
                    callback.accept(event.getReaction());
                }
            }
        };

        // Adds the reaction listener to the JDA.
        CafeBot.getJDA().addEventListener(reactionListener);
    }

    public String getGuildID() {
        return guildID;
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

            // Adds this message to the reaction listeners.
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

                    // Makes sure the person who reacted is the user who is supposed to react.
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

    @NotNull
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

    private void addReactions(Message message) {
        message.addReaction("1️⃣").queue();
        message.addReaction("2️⃣").queue();
        message.addReaction("3️⃣").queue();
        message.addReaction("4️⃣").queue();
        message.addReaction("5️⃣").queue();
        message.addReaction("6️⃣").queue();
        message.addReaction("7️⃣").queue();
        message.addReaction("❌").queue();
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

    private void stopGameTimer() {
        gameTimer.cancel();
        CafeBot.getConnectFourHandler().stopGame(this);
        CafeBot.getJDA().removeEventListener(reactionListener);
    }

    private void fillBoard() {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                board[x][y] = "⬜";
                takenSpots[x][y] = false;
            }
        }
    }

    @NotNull
    private MessageEmbed endGameEmbed(@NotNull String title, @NotNull String description, @NotNull String footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(Color.red);
        embedBuilder.setFooter(footer);
        return embedBuilder.build();
    }

    private boolean parseTurn(MessageReaction.ReactionEmote emote) {
        String emoji = emote.getEmoji();

        int x;
        int y;

        switch (emoji) {
            case "1️⃣" -> {
                x = 0;
            }
            case "2️⃣" -> {
                x = 1;
            }
            case "3️⃣" -> {
                x = 2;
            }
            case "4️⃣" -> {
                x = 3;
            }
            case "5️⃣" -> {
                x = 4;
            }
            case "6️⃣" -> {
                x = 5;
            }
            case "7️⃣" -> {
                x = 6;
            }
            default -> {
                return false;
            }
        }
        y = getTopValue(x);

        if (y == -1) {
            return false;
        }
        return useTurn(x, y);
    }

    private void checkGame(String unicodeEmoji, User player) {
        boolean allSpotsTaken = true;

        for (int y = 5; y >= 0; y--) {
            for (int x = 0; x < 7; x++) {

                // Making sure to only check for the ones that should be checked.
                if (board[x][y].equals(unicodeEmoji)) {

                    try {
                        // Straight up and down
                        if (board[x][y].equals(unicodeEmoji) && board[x][y + 1].equals(unicodeEmoji) && board[x][y + 2].equals(unicodeEmoji) && board[x][y + 3].equals(unicodeEmoji)) {
                            winGame(player);
                            return;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}

                    try {
                        // Horizontal
                        if (board[x][y].equals(unicodeEmoji) && board[x + 1][y].equals(unicodeEmoji) && board[x + 2][y].equals(unicodeEmoji) && board[x + 3][y].equals(unicodeEmoji)) {
                            winGame(player);
                            return;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}

                    try {
                        // Diagonal Up Right
                        if (board[x][y].equals(unicodeEmoji) && board[x + 1][y + 1].equals(unicodeEmoji) && board[x + 2][y + 2].equals(unicodeEmoji) && board[x + 3][y + 3].equals(unicodeEmoji)) {
                            winGame(player);
                            return;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}

                    try {
                        // Diagonal Up Left
                        if (board[x][y].equals(unicodeEmoji) && board[x - 1][y + 1].equals(unicodeEmoji) && board[x - 2][y + 2].equals(unicodeEmoji) && board[x - 3][y + 3].equals(unicodeEmoji)) {
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

    private void winGame(@NotNull User user) {
        hasWinner = true;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Connect Four");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        StringBuilder boardBuilder = new StringBuilder();
        boardBuilder.append("———+———+———+——+———+———+———\n");
        for (int y = 5; y >= 0; y--) {
            for (int x = 0; x < 7; x++) {
                boardBuilder.append("|    ");
                if (x == 6) {
                    boardBuilder.append(" ");
                }
                boardBuilder.append(board[x][y]).append("     ");
            }
            boardBuilder.append("|\n———+———+———+——+———+———+———\n");
        }

        for (int i = 0; i < 7; i++) {
            boardBuilder.append("|    ");
            if (i == 6) {
                boardBuilder.append(" ");
            }
            boardBuilder.append(getBoardEmojis().get(i)).append("     ");
        }

        boardBuilder.append("|\n");

        boardBuilder.append("\n**").append(user.getName()).append("** wins!");

        embedBuilder.setDescription(boardBuilder.toString());
        stopGameTimer();

        if (checkGameExists()) {
            CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(currentTextChannelID)
                    .sendMessage(embedBuilder.build()).queue();
        }
    }

    private void tieGame() {
        hasWinner = true;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Connect Four");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        StringBuilder boardBuilder = new StringBuilder();
        boardBuilder.append("———+———+———+——+———+———+———\n");
        for (int y = 5; y >= 0; y--) {
            for (int x = 0; x < 7; x++) {
                boardBuilder.append("|    ");
                if (x == 6) {
                    boardBuilder.append(" ");
                }
                boardBuilder.append(board[x][y]).append("     ");
            }
            boardBuilder.append("|\n———+———+———+——+———+———+———\n");
        }

        for (int i = 0; i < 7; i++) {
            boardBuilder.append("|    ");
            if (i == 6) {
                boardBuilder.append(" ");
            }
            boardBuilder.append(getBoardEmojis().get(i)).append("     ");
        }

        boardBuilder.append("|\n");

        boardBuilder.append("**There was a tie!**");

        embedBuilder.setDescription(boardBuilder.toString());
        stopGameTimer();

        if (checkGameExists()) {
            CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(currentTextChannelID)
                    .sendMessage(embedBuilder.build()).queue();
        }
    }

    /**
     * Checks to see if a {@link User} can use that turn.
     * @param x The X value of the button.
     * @param y The Y value of the button.
     * @return Whether or not the turn was used.
     */
    public Boolean useTurn(@NotNull Integer x, @NotNull Integer y) {
        if (!takenSpots[x][y]) {
            takenSpots[x][y] = true;

            // Makes sure to properly check for which player is currently playing.
            if (currentUser.equals(player1)) {
                board[x][y] = "🔴";
                checkGame("🔴", player1);
                currentUser = player2;
            } else {
                board[x][y] = "🟡";
                checkGame("🟡", player2);
                currentUser = player1;
            }
            return true;
        }
        return false;
    }

    @NotNull
    public Integer getTopValue(@NotNull Integer x) {
        for (int y = 0; y < 6; y++) {
            if (board[x][y].equals("⬜")) {
                return y;
            }
        }
        return -1;
    }

    @NotNull
    public MessageEmbed getBoardEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Connect Four");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        StringBuilder boardBuilder = new StringBuilder();
        boardBuilder.append("———+———+———+——+———+———+———\n");
        for (int y = 5; y >= 0; y--) {
            for (int x = 0; x < 7; x++) {
                boardBuilder.append("|    ");
                if (x == 6) {
                    boardBuilder.append(" ");
                }
                boardBuilder.append(board[x][y]).append("     ");
            }
            boardBuilder.append("|\n———+———+———+——+———+———+———\n");
        }

        for (int i = 0; i < 7; i++) {
            boardBuilder.append("|    ");
            if (i == 6) {
                boardBuilder.append(" ");
            }
            boardBuilder.append(getBoardEmojis().get(i)).append("     ");
        }

        boardBuilder.append("|\n");

        if (player1.equals(currentUser)) {
            boardBuilder.append("\n").append("**").append(player1.getName()).append("** vs ").append(player2.getName());
        } else {
            boardBuilder.append("\n").append(player1.getName()).append(" vs **").append(player2.getName()).append("**");
        }

        embedBuilder.setDescription(boardBuilder.toString());
        return embedBuilder.build();
    }

    @NotNull
    private ArrayList<String> getBoardEmojis() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1️⃣");
        arrayList.add("2️⃣");
        arrayList.add("3️⃣");
        arrayList.add("4️⃣");
        arrayList.add("5️⃣");
        arrayList.add("6️⃣");
        arrayList.add("7️⃣");
        return arrayList;
    }

}
