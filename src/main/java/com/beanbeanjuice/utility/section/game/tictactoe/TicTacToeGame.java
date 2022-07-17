package com.beanbeanjuice.utility.section.game.tictactoe;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.section.game.WinStreakHandler;
import com.beanbeanjuice.cafeapi.cafebot.minigames.winstreaks.MinigameType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * A custom {@link TicTacToeGame} object.
 *
 * @author beanbeanjuice
 */
public class TicTacToeGame {

    private final String[][] board;
    private final boolean[][] takenSpots;
    private User currentUser;
    private final User player1;
    private final User player2;

    private String currentMessageID = null;
    private final String currentTextChannelID;
    private final String guildID;

    private Timer gameTimer;
    private TimerTask gameTimerTask;

    private final int TIME_UNTIL_END = 60;
    private int count = 0;
    private boolean hasWinner = false;

    private final HashMap<Long, Consumer<MessageReaction>> emojiListeners = new HashMap<>();
    private final ListenerAdapter reactionListener;

    /**
     * Creates a new {@link TicTacToeGame} object.
     * @param player1 The first {@link User}.
     * @param player2 The second {@link User}.
     * @param textChannel The {@link TextChannel} the game is in.
     */
    public TicTacToeGame(@NotNull User player1, @NotNull User player2, @NotNull TextChannel textChannel) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentUser = player1;
        this.guildID = textChannel.getGuild().getId();
        this.currentTextChannelID = textChannel.getId();

        board = new String[3][3];
        takenSpots = new boolean[3][3];
        fillBoard();

        // Creates a new reaction listener for the current game.
        reactionListener = new ListenerAdapter() {
            @Override
            public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
                Consumer<MessageReaction> callback = emojiListeners.get(event.getMessageIdLong());

                if (callback != null) {
                    callback.accept(event.getReaction());
                }
            }
        };

        // Adds the reaction listener to the JDA.
        Bot.getBot().addEventListener(reactionListener);
    }

    /**
     * Starts the {@link TicTacToeGame}.
     */
    protected void startGame() {
        startGameTimer();

        try {
            GuildHandler.getGuild(guildID).getTextChannelById(currentTextChannelID).sendMessageEmbeds(getBoardEmbed()).queue(message -> {
                currentMessageID = message.getId();
                editMessage();
            });
        } catch (NullPointerException e) {
            stopGameTimer();
        }
    }

    /**
     * Edits the {@link TicTacToeGame} message.
     */
    private void editMessage() {
        try {
            GuildHandler.getGuild(guildID).getTextChannelById(currentTextChannelID).retrieveMessageById(currentMessageID).queue(message -> {
                message.editMessageEmbeds(getBoardEmbed()).queue(editedMessage -> {
                    addReactions(message);

                    // Adds this message to the reaction listeners.
                    emojiListeners.put(message.getIdLong(), (r) -> {
                        r.retrieveUsers().queue(users -> {

                            // Checking if someone cancelled the game.
                            if (users.contains(player1) || users.contains(player2)) {
                                if (r.getEmoji().equals(Emoji.fromFormatted("❌"))) {
                                    stopGameTimer();
                                    if (checkGameExists()) {
                                        GuildHandler.getGuild(guildID).getTextChannelById(currentTextChannelID).retrieveMessageById(currentMessageID).queue(retrievedMessage -> {
                                            String title = retrievedMessage.getEmbeds().get(0).getTitle();
                                            String description = retrievedMessage.getEmbeds().get(0).getDescription();
                                            retrievedMessage.editMessageEmbeds(endGameEmbed(title, description, "The game was cancelled.")).queue();
                                        });
                                    }
                                    return;
                                }
                            }

                            // Makes sure the person who reacted is the user who is supposed to react.
                            if (users.contains(currentUser)) {
                                if (getBoardEmojis().contains(r.getEmoji().getFormatted()) && !r.isSelf()) {

                                    // Checking if the emote is part of the list and if they are allowed to use that emote.
                                    if (!parseTurn(r.getEmoji())) {
                                        r.removeReaction(currentUser).queue();
                                        return;
                                    }

                                    emojiListeners.remove(r.getMessageIdLong());

                                    if (!hasWinner) {
                                        editMessage();
                                    }
                                    count = 0;
                                }
                            } else {
                                r.retrieveUsers().queue(notPlayers -> {
                                    for (User user : notPlayers) {
                                        if (!user.isBot()) {
                                            try {
                                                r.removeReaction(user).queue();
                                            } catch (InsufficientPermissionException e) {
                                                r.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                                                        "Insufficient Permissions",
                                                        "The bot has insufficient permissions: " + e.getMessage() + ".\n" +
                                                                "Please make sure the bot has the correct permissions."
                                                )).queue();
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    });
                });
            });
        } catch (NullPointerException e) {
            stopGameTimer();
            return;
        }
    }

    /**
     * @return The board of emojis for the {@link TicTacToeGame}.
     */
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
        arrayList.add("8️⃣");
        arrayList.add("9️⃣");
        return arrayList;
    }

    /**
     * Adds reactions to the {@link Message}.
     * @param message The {@link Message} to have reactions added to.
     */
    private void addReactions(Message message) {
        if (!takenSpots[0][2]) {
            message.addReaction(Emoji.fromFormatted("1️⃣")).queue();
        }
        if (!takenSpots[1][2]) {
            message.addReaction(Emoji.fromFormatted("2️⃣")).queue();
        }
        if (!takenSpots[2][2]) {
            message.addReaction(Emoji.fromFormatted("3️⃣")).queue();
        }
        if (!takenSpots[0][1]) {
            message.addReaction(Emoji.fromFormatted("4️⃣")).queue();
        }
        if (!takenSpots[1][1]) {
            message.addReaction(Emoji.fromFormatted("5️⃣")).queue();
        }
        if (!takenSpots[2][1]) {
            message.addReaction(Emoji.fromFormatted("6️⃣")).queue();
        }
        if (!takenSpots[0][0]) {
            message.addReaction(Emoji.fromFormatted("7️⃣")).queue();
        }
        if (!takenSpots[1][0]) {
            message.addReaction(Emoji.fromFormatted("8️⃣")).queue();
        }
        if (!takenSpots[2][0]) {
            message.addReaction(Emoji.fromFormatted("9️⃣")).queue();
        }

        message.addReaction(Emoji.fromFormatted("❌")).queue();
    }

    /**
     * Makes sure the {@link TextChannel} and game {@link Message} still exist.
     * @return True, if the game exists.
     */
    @NotNull
    private Boolean checkGameExists() {
        TextChannel textChannel;
        try {
            textChannel = GuildHandler.getGuild(guildID).getTextChannelById(currentTextChannelID);
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

    /**
     * Starts the game {@link Timer}. Stops the game after inactivity.
     */
    private void startGameTimer() {
        gameTimer = new Timer();
        gameTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (count++ >= TIME_UNTIL_END) {
                    stopGameTimer();
                    if (checkGameExists()) {
                        GuildHandler.getGuild(guildID).getTextChannelById(currentTextChannelID).retrieveMessageById(currentMessageID).queue(retrievedMessage -> {
                            String title = retrievedMessage.getEmbeds().get(0).getTitle();
                            String description = retrievedMessage.getEmbeds().get(0).getDescription();
                            retrievedMessage.editMessageEmbeds(endGameEmbed(title, description, "The game ended because you didn't respond in time.")).queue(e -> {
                                e.clearReactions().queue();
                                e.addReaction(Emoji.fromFormatted("❌")).queue();
                            });
                        });
                    }
                }
            }
        };
        gameTimer.scheduleAtFixedRate(gameTimerTask, 0, 1000);
    }

    /**
     * A {@link MessageEmbed} to be sent upon game ending.
     * @param title The title of the {@link MessageEmbed}.
     * @param description The description of the {@link MessageEmbed}.
     * @param footer The footer of the {@link MessageEmbed}.
     * @return The completed {@link MessageEmbed}.
     */
    @NotNull
    private MessageEmbed endGameEmbed(@NotNull String title, @NotNull String description, @NotNull String footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(Color.red);
        embedBuilder.setFooter(footer);
        return embedBuilder.build();
    }

    /**
     * Stops the current game {@link Timer}.
     */
    private void stopGameTimer() {
        gameTimer.cancel();
        TicTacToeHandler.stopGame(this);
        Bot.getBot().removeEventListener(reactionListener);
    }

    /**
     * Gets the ID for the {@link Guild} that the {@link TicTacToeGame} is in.
     * @return The {@link String} ID for the {@link Guild}.
     */
    @NotNull
    protected String getGuildID() {
        return guildID;
    }

    /**
     * Fills the board for the {@link TicTacToeGame}.
     */
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

    /**
     * Checks to see if a {@link User} can use that turn.
     * @param x The X value of the button.
     * @param y The Y value of the button.
     * @return True, if turn was used.
     */
    private Boolean useTurn(@NotNull Integer x, @NotNull Integer y) {
        if (!takenSpots[x][y]) {
            takenSpots[x][y] = true;

            // Makes sure to properly check for which player is currently playing.
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

    /**
     * Checks the {@link TicTacToeGame} for any winners.
     * @param unicodeEmoji The unicode {@link String} to check for.
     * @param player The possible {@link User} winner.
     */
    private void checkGame(String unicodeEmoji, User player) {
        boolean allSpotsTaken = true;

        for (int y = 2; y >= 0; y--) {
            for (int x = 0; x < 3; x++) {

                // Making sure to only check for the ones that should be checked.
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

    /**
     * A method that is run when there is a tie.
     */
    private void tieGame() {
        hasWinner = true;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Tic-Tac-Toe");
        embedBuilder.setColor(Helper.getRandomColor());
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
            GuildHandler.getGuild(guildID).getTextChannelById(currentTextChannelID)
                    .editMessageEmbedsById(currentMessageID, embedBuilder.build()).queue(message -> {
                        message.clearReactions().queue();
                    });
        }
    }

    /**
     * A method that is run when there is a winner.
     * @param user The {@link User} who has won.
     */
    private void winGame(@NotNull User user) {
        hasWinner = true;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Tic-Tac-Toe");
        embedBuilder.setColor(Helper.getRandomColor());
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

        Integer currentWinStreak = WinStreakHandler.getUserWinStreak(user.getId(), MinigameType.TIC_TAC_TOE);
        User loser;

        if (user == player1) {
            loser = player2;
        } else {
            loser = player1;
        }
        Integer loserWinStreak = WinStreakHandler.getUserWinStreak(loser.getId(), MinigameType.TIC_TAC_TOE);

        if (currentWinStreak != null) {
            if (!WinStreakHandler.updateUserWinStreak(user.getId(), MinigameType.TIC_TAC_TOE, ++currentWinStreak)) {
                currentWinStreak = null;
            }

            if (!WinStreakHandler.updateUserWinStreak(loser.getId(), MinigameType.TIC_TAC_TOE, 0)) {
                loserWinStreak = null;
            }
        }
        embedBuilder.setFooter(user.getName() + " now has a win streak of " + currentWinStreak + ". " + loser.getName() + " has lost their " +
                "win streak of " + loserWinStreak + "!");

        stopGameTimer();

        if (checkGameExists()) {
            GuildHandler.getGuild(guildID).getTextChannelById(currentTextChannelID)
                    .editMessageEmbedsById(currentMessageID, embedBuilder.build()).queue(message -> {
                        message.clearReactions().queue();
                    });
        }
    }

    /**
     * Parses the {@link Emoji}.
     * @param emote The {@link Emoji} to parse.
     * @return True, if that is a valid turn.
     */
    @NotNull
    private Boolean parseTurn(Emoji emote) {
        String emoji = emote.getFormatted();

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

    /**
     * @return The current board {@link MessageEmbed} for the {@link TicTacToeGame}.
     */
    @NotNull
    private MessageEmbed getBoardEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Tic-Tac-Toe");
        embedBuilder.setColor(Helper.getRandomColor());
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
            boardBuilder.append("\n").append("**").append(player1.getName()).append("** (❌) vs ").append(player2.getName()).append(" (🔵)");
        } else {
            boardBuilder.append("\n").append(player1.getName()).append(" (❌) vs **").append(player2.getName()).append("** (🔵)");
        }

        embedBuilder.setDescription(boardBuilder.toString());
        return embedBuilder.build();
    }

}
