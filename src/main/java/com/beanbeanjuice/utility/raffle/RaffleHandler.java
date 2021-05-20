package com.beanbeanjuice.utility.raffle;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class used for handling {@link Raffle} objects.
 *
 * @author beanbeanjuice
 */
public class RaffleHandler {

    private HashMap<String, ArrayList<Raffle>> raffles;
    private Timer raffleTimer;
    private TimerTask raffleTimerTask;

    /**
     * Create a new {@link RaffleHandler} object.
     */
    public RaffleHandler() {
        raffles = new HashMap<>();
        getRafflesFromDatabase();
        startPollTimer();
    }

    /**
     * Starts the poll {@link Timer} and {@link TimerTask}.
     */
    private void startPollTimer() {
        raffleTimer = new Timer();
        raffleTimerTask = new TimerTask() {

            @Override
            public void run() {

                raffles.forEach((key, value) -> {
                    for (Raffle raffle : value) {

                        // Checking if it SHOULD be checked.
                        if (raffle.isFinished()) {

                            // Checking if the PollChannel is Null
                            TextChannel raffleChannel = CafeBot.getGuildHandler().getCustomGuild(key).getRaffleChannel();

                            if (raffleChannel == null) {
                                removeRaffleFromDatabase(raffle);
                                value.remove(raffle);
                            } else {

                                // If its not null, check if the message is null.
                                raffleChannel.retrieveMessageById(raffle.getMessageID()).queue((message) -> {
                                    // Edit Message If Not Null
                                    // Get the reactions
                                    ArrayList<User> potentialUsers = new ArrayList<>();

                                    message.getReactions().get(0).retrieveUsers().queue(users -> {
                                        for (User user : users) {
                                            if (!user.isBot() && !potentialUsers.contains(user)) {
                                                potentialUsers.add(user);
                                            }
                                        }

                                        ArrayList<User> winners = new ArrayList<>();

                                        if (potentialUsers.size() > raffle.getWinnerAmount()) {
                                            for (int i = 0; i < raffle.getWinnerAmount(); i++) {
                                                User user = potentialUsers.get(CafeBot.getGeneralHelper().getRandomNumber(0, (potentialUsers.size() - 1)));
                                                if (winners.contains(user)) {
                                                    User newUser = potentialUsers.get(CafeBot.getGeneralHelper().getRandomNumber(0, (potentialUsers.size() - 1)));
                                                    while (newUser == user) {
                                                        newUser = potentialUsers.get(CafeBot.getGeneralHelper().getRandomNumber(0, (potentialUsers.size() - 1)));
                                                    }
                                                    user = newUser;
                                                }
                                                winners.add(user);
                                            }
                                        } else {
                                            winners = potentialUsers;
                                        }

                                        String title = message.getEmbeds().get(0).getAuthor().getName();
                                        String description = message.getEmbeds().get(0).getFields().get(0).getValue();

                                        message.editMessage(winnerEmbed(title, description, winners)).queue();

                                        // Remove it
                                        removeRaffleFromDatabase(raffle);
                                        value.remove(raffle);
                                    });
                                }, (failure) -> {

                                    // This means the message does not exist.
                                    removeRaffleFromDatabase(raffle);
                                    value.remove(raffle);
                                });
                            }
                        }
                    }
                });
            }
        };
        raffleTimer.scheduleAtFixedRate(raffleTimerTask, 0, 30000);
    }

    /**
     * The winner {@link MessageEmbed} for the {@link RaffleHandler}.
     * @param title The title of the {@link MessageEmbed}.
     * @param description The description of the {@link MessageEmbed}.
     * @param winners The {@link ArrayList<User> winners} of the {@link Raffle}.
     * @return The completed {@link MessageEmbed}.
     */
    @NotNull
    private MessageEmbed winnerEmbed(@NotNull String title, @NotNull String description, @NotNull ArrayList<User> winners) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.addField("Description", description, false);
        embedBuilder.setColor(Color.green);

        if (winners.isEmpty()) {
            embedBuilder.addField("Winner", "No one entered the raffle...", false);
        } else if (winners.size() == 1) {
            embedBuilder.addField("Winner", winners.get(0).getAsMention(), false);
        } else {
            StringBuilder winnerBuilder = new StringBuilder();
            for (int i = 0; i < winners.size(); i++) {
                winnerBuilder.append(winners.get(i).getAsMention());
                if (i != winners.size() - 1) {
                    winnerBuilder.append(", ");
                }
            }
            embedBuilder.addField("Winners", winnerBuilder.toString(), false);
        }
        return embedBuilder.build();
    }

    /**
     * Removes a {@link Raffle} from the database.
     * @param raffle The {@link Raffle} to be removed.
     * @return Whether or not the removal was successful.
     */
    @NotNull
    private Boolean removeRaffleFromDatabase(@NotNull Raffle raffle) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "DELETE FROM cafeBot.raffles WHERE message_id = (?) and guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(raffle.getMessageID()));
            statement.setLong(2, Long.parseLong(raffle.getGuildID()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Removing Raffle: " + e.getMessage());
            return false;
        }
    }

    /**
     * Add a {@link Raffle} to the database.
     * @param raffle The {@link Raffle} to add.
     * @return Whether or not adding the {@link Raffle} was successful.
     */
    @NotNull
    public Boolean addRaffle(@NotNull Raffle raffle) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "INSERT INTO cafeBot.raffles (guild_id, message_id, ending_time, winner_amount) VALUES (?,?,?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(raffle.getGuildID()));
            statement.setLong(2, Long.parseLong(raffle.getMessageID()));
            statement.setTimestamp(3, raffle.getRaffleEndTime());
            statement.setInt(4, raffle.getWinnerAmount());
            statement.execute();
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Adding Raffle: " + e.getMessage());
            return false;
        }

        if (raffles.get(raffle.getGuildID()) == null) {
            raffles.put(raffle.getGuildID(), new ArrayList<>());
        }

        raffles.get(raffle.getGuildID()).add(raffle);
        return true;
    }

    /**
     * Get the {@link ArrayList<Raffle>} for a specified {@link Guild}.
     * @param guildID The ID of the {@link Guild}.
     * @return The {@link ArrayList<Raffle>} for the {@link Guild}.
     */
    @NotNull
    public ArrayList<Raffle> getRafflesForGuild(@NotNull String guildID) {
        if (raffles.get(guildID) != null) {
            return raffles.get(guildID);
        }
        return new ArrayList<>();
    }

    /**
     * Get the {@link ArrayList<Raffle>} for a specified {@link Guild}.
     * @param guild The {@link Guild}.
     * @return The {@link ArrayList<Raffle>} for the {@link Guild}.
     */
    @NotNull
    public ArrayList<Raffle> getRafflesForGuild(@NotNull Guild guild) {
        return getRafflesForGuild(guild.getId());
    }

    /**
     * Refresh the {@link Raffle} cache.
     */
    private void getRafflesFromDatabase() {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.raffles;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(arguments);
            while (resultSet.next()) {
                String guildID = String.valueOf(resultSet.getLong(1));
                String messageID = String.valueOf(resultSet.getLong(2));
                Timestamp raffleEndTime = resultSet.getTimestamp(3);
                Integer winnerAmount = resultSet.getInt(4);

                if (raffles.get(guildID) == null) {
                    raffles.put(guildID, new ArrayList<>());
                }

                raffles.get(guildID).add(new Raffle(guildID, messageID, raffleEndTime, winnerAmount));
            }
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Retrieving Raffles: " + e.getMessage());
        }
    }

}
