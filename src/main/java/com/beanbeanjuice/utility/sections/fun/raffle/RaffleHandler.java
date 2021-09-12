package com.beanbeanjuice.utility.sections.fun.raffle;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
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
        getAllRaffles();
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

                raffles.forEach((guildID, raffles) -> {
                    for (Raffle raffle : raffles) {

                        // Checking if it SHOULD be checked.
                        if (raffle.isFinished()) {

                            // Checking if the PollChannel is Null
                            TextChannel raffleChannel = CafeBot.getGuildHandler().getCustomGuild(guildID).getRaffleChannel();

                            if (raffleChannel == null) {
                                if (removeRaffle(guildID, raffle)) {
                                    raffles.remove(raffle);
                                }
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

                                        String title = message.getEmbeds().get(0).getTitle();
                                        String description = message.getEmbeds().get(0).getFields().get(0).getValue();

                                        message.editMessageEmbeds(winnerEmbed(title, description, winners)).queue();

                                        // Remove it
                                        if (removeRaffle(guildID, raffle)) {
                                            raffles.remove(raffle);
                                        }
                                    });
                                }, (failure) -> {

                                    // This means the message does not exist.
                                    if (removeRaffle(guildID, raffle)) {
                                        raffles.remove(raffle);
                                    }
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
     * Removes a {@link Raffle} for a {@link Guild} from the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the {@link Raffle}.
     * @param raffle The {@link Raffle} to add.
     * @return True, if the {@link Raffle} was successfully removed from the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     */
    @NotNull
    private Boolean removeRaffle(@NotNull String guildID, @NotNull Raffle raffle) {
        try {
            CafeBot.getCafeAPI().raffles().deleteRaffle(guildID, raffle);
            return true;
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Removing Raffle: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Adds a {@link Raffle} to the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI} for a specified {@link Guild}.
     * @param guildID The specified {@link String guildID}.
     * @param raffle The {@link Raffle} to add.
     * @return True, if the {@link Raffle} was successfully added.
     */
    @NotNull
    public Boolean addRaffle(@NotNull String guildID, @NotNull Raffle raffle) {
        try {
            CafeBot.getCafeAPI().raffles().createRaffle(guildID, raffle);

            if (!raffles.containsKey(guildID)) {
                raffles.put(guildID, new ArrayList<>());
            }

            raffles.get(guildID).add(raffle);
            return true;
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Creating Raffle: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Retrieves all {@link Raffle} from the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     */
    private void getAllRaffles() {
        try {
            CafeBot.getCafeAPI().raffles().getAllRaffles().forEach((guildID, raffles) -> {
                for (io.github.beanbeanjuice.cafeapi.cafebot.raffles.Raffle raffle : raffles) {

                    if (!this.raffles.containsKey(guildID)) {
                        this.raffles.put(guildID, new ArrayList<>());
                    }

                    this.raffles.get(guildID).add(new Raffle(raffle.getMessageID(), raffle.getEndingTime(), raffle.getWinnerAmount()));
                }
            });

            CafeBot.getLogManager().log(this.getClass(), LogLevel.OKAY, "Successfully Retrieved All Raffles...");
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Retrieving Raffles from API: " + e.getMessage(), e);
        }
    }

}
