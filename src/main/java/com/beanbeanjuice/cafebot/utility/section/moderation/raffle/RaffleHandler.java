package com.beanbeanjuice.cafebot.utility.section.moderation.raffle;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
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

    private static HashMap<String, ArrayList<Raffle>> raffles;

    /**
     * Starts the {@link RaffleHandler}.
     */
    public static void start() {
        raffles = new HashMap<>();
        getAllRaffles();
        startRaffleTimer();
    }

    /**
     * Starts the poll {@link Timer} and {@link TimerTask}.
     */
    private static void startRaffleTimer() {
        Timer raffleTimer = new Timer();
        TimerTask raffleTimerTask = new TimerTask() {

            @Override
            public void run() {
                checkRaffles();
            }
        };

        // Run every 30 seconds.
        raffleTimer.scheduleAtFixedRate(raffleTimerTask, 0, 30000);
    }

    private static void checkRaffles() {
        // Goes through ever raffle.
        raffles.forEach((guildID, raffles) -> {

            // Check if the guild still contains the bot.
            if (!GuildHandler.guildContainsBot(guildID)) {
                raffles.removeIf(raffle -> removeRaffle(guildID, raffle));
                return;
            }

            // Otherwise check all raffles.
            for (Raffle raffle : raffles) {
                // Checking if it SHOULD be checked.
                if (raffle.isFinished())
                    checkIndividualRaffle(raffle, guildID);
            }
        });
    }

    private static void checkIndividualRaffle(@NotNull Raffle raffle, @NotNull String guildID) {
        // Checking if the PollChannel is Null
        TextChannel raffleChannel = GuildHandler.getCustomGuild(guildID).getRaffleChannel();

        // If the channel does not exist, then remove it.
        if (raffleChannel == null) {
            if (removeRaffle(guildID, raffle))
                raffles.remove(raffle);
        } else {

            try {
                // If it is not null, check if the message is null.
                raffleChannel.retrieveMessageById(raffle.getMessageID()).queue((message) -> {
                    // Edit Message If Not Null
                    // Get the reactions
                    ArrayList<User> potentialUsers = new ArrayList<>();

                    // Go through each user.
                    message.getReactions().get(0).retrieveUsers().queue(users -> {
                        for (User user : users) {
                            if (!user.isBot() && !potentialUsers.contains(user))
                                potentialUsers.add(user);
                        }

                        ArrayList<User> winners = new ArrayList<>();

                        if (potentialUsers.size() > raffle.getWinnerAmount()) {
                            for (int i = 0; i < raffle.getWinnerAmount(); i++) {
                                User user = potentialUsers.get(Helper.getRandomNumber(0, (potentialUsers.size() - 1)));
                                if (winners.contains(user)) {
                                    User newUser = potentialUsers.get(Helper.getRandomNumber(0, (potentialUsers.size() - 1)));
                                    while (newUser == user)
                                        newUser = potentialUsers.get(Helper.getRandomNumber(0, (potentialUsers.size() - 1)));
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
                        if (removeRaffle(guildID, raffle))
                            raffles.remove(raffle);
                    });
                }, (failure) -> {

                    // This means the message does not exist.
                    if (removeRaffle(guildID, raffle))
                        raffles.remove(raffle);
                });

            } catch (InsufficientPermissionException ignored) { }

        }
    }

    @NotNull
    private static MessageEmbed winnerEmbed(@NotNull String title, @NotNull String description, @NotNull ArrayList<User> winners,
                                     @Nullable MessageEmbed.AuthorInfo authorInfo, @Nullable MessageEmbed.Thumbnail thumbnail, @Nullable MessageEmbed.ImageInfo image) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .addField("Description", description, false)
                .setFooter("The raffle has ended.");

        if (authorInfo != null)
            embedBuilder.setAuthor(authorInfo.getName());

        if (thumbnail != null)
            embedBuilder.setThumbnail(thumbnail.getUrl());

        if (image != null)
            embedBuilder.setImage(image.getUrl());

        if (winners.isEmpty()) {
            embedBuilder.addField("Winner", "No one entered the raffle...", false);
            embedBuilder.setColor(Color.orange);
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
     * Removes a {@link Raffle} for a {@link Guild} from the {@link CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the {@link Raffle}.
     * @param raffle The {@link Raffle} to add.
     * @return True, if the {@link Raffle} was successfully removed from the {@link CafeAPI CafeAPI}.
     */
    @NotNull
    private static Boolean removeRaffle(@NotNull String guildID, @NotNull Raffle raffle) {
        try {
            Bot.getCafeAPI().RAFFLE.deleteRaffle(guildID, raffle);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(RaffleHandler.class, LogLevel.ERROR, "Error Removing Raffle: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Adds a {@link Raffle} to the {@link CafeAPI CafeAPI} for a specified {@link Guild}.
     * @param guildID The specified {@link String guildID}.
     * @param raffle The {@link Raffle} to add.
     * @return True, if the {@link Raffle} was successfully added.
     */
    @NotNull
    public static Boolean addRaffle(@NotNull String guildID, @NotNull Raffle raffle) {
        try {
            Bot.getCafeAPI().RAFFLE.createRaffle(guildID, raffle);

            if (!raffles.containsKey(guildID))
                raffles.put(guildID, new ArrayList<>());

            raffles.get(guildID).add(raffle);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(RaffleHandler.class, LogLevel.ERROR, "Error Creating Raffle: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Retrieves all {@link Raffle} from the {@link CafeAPI CafeAPI}.
     */
    private static void getAllRaffles() {
        try {
            Bot.getCafeAPI().RAFFLE.getAllRaffles().forEach((guildID, apiRaffles) -> {

                // If the guild does not contain the bot, remove it.
                if (!GuildHandler.guildContainsBot(guildID)) {
                    for (com.beanbeanjuice.cafeapi.wrapper.cafebot.raffles.Raffle raffle : apiRaffles)
                        removeRaffle(guildID, new Raffle(raffle.getMessageID(), raffle.getEndingTime(), raffle.getWinnerAmount()));
                    return;
                }

                // Add the raffles if the guild is still in the bot.
                for (com.beanbeanjuice.cafeapi.wrapper.cafebot.raffles.Raffle raffle : apiRaffles) {
                    Raffle newRaffle = new Raffle(raffle.getMessageID(), raffle.getEndingTime(), raffle.getWinnerAmount());

                    if (!raffles.containsKey(guildID))
                        raffles.put(guildID, new ArrayList<>());

                    raffles.get(guildID).add(newRaffle);
                }
            });

            Bot.getLogger().log(RaffleHandler.class, LogLevel.OKAY, "Successfully Retrieved All Raffles...");
        } catch (CafeException e) {
            Bot.getLogger().log(RaffleHandler.class, LogLevel.ERROR, "Error Retrieving Raffles from API: " + e.getMessage(), e);
        }
    }

}
