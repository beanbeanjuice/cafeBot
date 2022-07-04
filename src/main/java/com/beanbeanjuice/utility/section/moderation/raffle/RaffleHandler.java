package com.beanbeanjuice.utility.section.moderation.raffle;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    private final HashMap<String, ArrayList<Raffle>> raffles;

    /**
     * Create a new {@link RaffleHandler} object.
     */
    public RaffleHandler() {
        raffles = new HashMap<>();
        getAllRaffles();
        startRaffleTimer();
    }

    /**
     * Starts the poll {@link Timer} and {@link TimerTask}.
     */
    private void startRaffleTimer() {
        Timer raffleTimer = new Timer();
        TimerTask raffleTimerTask = new TimerTask() {

            @Override
            public void run() {

                // Goes through ever raffle.
                raffles.forEach((guildID, raffles) -> {
                    for (Raffle raffle : raffles) {

                        // Checking if it SHOULD be checked.
                        if (raffle.isFinished()) {

                            // Checking if the PollChannel is Null
                            TextChannel raffleChannel = Bot.getGuildHandler().getCustomGuild(guildID).getRaffleChannel();

                            // If the channel does not exist, then remove it.
                            if (raffleChannel == null) {
                                if (removeRaffle(guildID, raffle)) {
                                    raffles.remove(raffle);
                                }
                            } else {

                                // If it is not null, check if the message is null.
                                raffleChannel.retrieveMessageById(raffle.getMessageID()).queue((message) -> {
                                    // Edit Message If Not Null
                                    // Get the reactions
                                    ArrayList<User> potentialUsers = new ArrayList<>();

                                    // Go through each user.
                                    message.getReactions().get(0).retrieveUsers().queue(users -> {
                                        for (User user : users) {
                                            if (!user.isBot() && !potentialUsers.contains(user)) {
                                                potentialUsers.add(user);
                                            }
                                        }

                                        ArrayList<User> winners = new ArrayList<>();

                                        if (potentialUsers.size() > raffle.getWinnerAmount()) {
                                            for (int i = 0; i < raffle.getWinnerAmount(); i++) {
                                                User user = potentialUsers.get(Helper.getRandomNumber(0, (potentialUsers.size() - 1)));
                                                if (winners.contains(user)) {
                                                    User newUser = potentialUsers.get(Helper.getRandomNumber(0, (potentialUsers.size() - 1)));
                                                    while (newUser == user) {
                                                        newUser = potentialUsers.get(Helper.getRandomNumber(0, (potentialUsers.size() - 1)));
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
                                        MessageEmbed.AuthorInfo author = message.getEmbeds().get(0).getAuthor();
                                        MessageEmbed.Thumbnail thumbnail = message.getEmbeds().get(0).getThumbnail();
                                        MessageEmbed.ImageInfo image = message.getEmbeds().get(0).getImage();

                                        message.editMessageEmbeds(winnerEmbed(
                                                title,
                                                description,
                                                winners,
                                                author,
                                                thumbnail,
                                                image)).queue();

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

        // Run every 30 seconds.
        raffleTimer.scheduleAtFixedRate(raffleTimerTask, 0, 30000);
    }

    @NotNull
    private MessageEmbed winnerEmbed(@NotNull String title, @NotNull String description, @NotNull ArrayList<User> winners,
                                     @Nullable MessageEmbed.AuthorInfo authorInfo, @Nullable MessageEmbed.Thumbnail thumbnail, @Nullable MessageEmbed.ImageInfo image) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .addField("Description", description, false);

        if (authorInfo != null)
            embedBuilder.setAuthor(authorInfo.getName());

        if (thumbnail != null)
            embedBuilder.setThumbnail(thumbnail.getUrl());

        if (image != null)
            embedBuilder.setImage(image.getUrl());

        if (winners.isEmpty()) {
            embedBuilder.addField("Winner", "No one entered the raffle...", false);
            embedBuilder.setColor(Color.gray);
        } else if (winners.size() == 1) {
            embedBuilder.addField("Winner", winners.get(0).getAsMention(), false);
            embedBuilder.setColor(Color.red);
        } else {
            StringBuilder winnerBuilder = new StringBuilder();
            for (int i = 0; i < winners.size(); i++) {
                winnerBuilder.append(winners.get(i).getAsMention());

                if (i != winners.size() - 1)
                    winnerBuilder.append(", ");

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
            Bot.getCafeAPI().RAFFLE.deleteRaffle(guildID, raffle);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Removing Raffle: " + e.getMessage(), e);
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
            Bot.getCafeAPI().RAFFLE.createRaffle(guildID, raffle);

            if (!raffles.containsKey(guildID))
                raffles.put(guildID, new ArrayList<>());

            raffles.get(guildID).add(raffle);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Creating Raffle: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Retrieves all {@link Raffle} from the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     */
    private void getAllRaffles() {
        try {
            Bot.getCafeAPI().RAFFLE.getAllRaffles().forEach((guildID, raffles) -> {
                for (io.github.beanbeanjuice.cafeapi.cafebot.raffles.Raffle raffle : raffles) {

                    if (!this.raffles.containsKey(guildID))
                        this.raffles.put(guildID, new ArrayList<>());

                    this.raffles.get(guildID).add(new Raffle(raffle.getMessageID(), raffle.getEndingTime(), raffle.getWinnerAmount()));
                }
            });

            Bot.getLogger().log(this.getClass(), LogLevel.OKAY, "Successfully Retrieved All Raffles...");
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Retrieving Raffles from API: " + e.getMessage(), e);
        }
    }

}
