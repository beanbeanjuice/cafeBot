package com.beanbeanjuice.utility.section.moderation.poll;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.logging.LogLevel;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class used for handling {@link Poll}.
 *
 * @author beanbeanjuice
 */
public class PollHandler {

    private final HashMap<String, ArrayList<Poll>> activePolls;

    /**
     * Creates a new {@link PollHandler} object.
     */
    public PollHandler() {
        activePolls = new HashMap<>();
        getAllPolls();
        startPollTimer();
    }

    /**
     * Starts the poll {@link Timer} and {@link TimerTask}.
     */
    private void startPollTimer() {
        Timer pollTimer = new Timer();
        TimerTask pollTimerTask = new TimerTask() {

            @Override
            public void run() {

                // Runs through each of the active polls.
                activePolls.forEach((guildID, polls) -> {

                    // Go through each poll
                    for (Poll poll : polls) {

                        // Check if the poll SHOULD be calculated.
                        // The poll should be "calculated" if it is finished.
                        if (poll.isFinished()) {

                            // Check if the poll channel still exists.
                            TextChannel pollChannel = Bot.getGuildHandler().getCustomGuild(guildID).getPollChannel();

                            // If the poll channel doesn't exist, then just remove the poll
                            if (pollChannel == null) {

                                // If it was IN the polls in the first place, then remove it.
                                if (removePoll(guildID, poll)) {
                                    polls.remove(poll);
                                }

                            } else {
                                // Poll Channel IS NOT Null
                                // Check if the message is null
                                pollChannel.retrieveMessageById(poll.getMessageID()).queue((message) -> {
                                    // Edit Message If Not Null
                                    // Get the reactions
                                    ArrayList<MessageReaction> messageReactions = new ArrayList<>(message.getReactions());

                                    // Compare the amount of reactions
                                    int highestReaction = 0;

                                    // Find the highest reaction first.
                                    for (MessageReaction messageReaction : messageReactions) {
                                        if (messageReaction.getCount() > highestReaction) {
                                            highestReaction = messageReaction.getCount();
                                        }
                                    }

                                    // TODO: There has to be a better way to do this. Right now it goes through the list twice.
                                    // Now we have the highest reaction number.
                                    ArrayList<Emoji> highestReactions = new ArrayList<>();

                                    // Gets the amount of reactions for that message.
                                    for (MessageReaction messageReaction : messageReactions) {
                                        if (messageReaction.getCount() == highestReaction && messageReaction.getCount() != 1) {
                                            highestReactions.add(messageReaction.getEmoji());
                                        }
                                    }

                                    // Getting the poll information
                                    String title = message.getEmbeds().get(0).getTitle();
                                    String pollDescription = message.getEmbeds().get(0).getDescription();
                                    MessageEmbed.AuthorInfo author = message.getEmbeds().get(0).getAuthor();
                                    MessageEmbed.Thumbnail thumbnail = message.getEmbeds().get(0).getThumbnail();
                                    MessageEmbed.ImageInfo image = message.getEmbeds().get(0).getImage();

                                    // Update the poll, to finish it.
                                    message.editMessageEmbeds(pollEmbed(
                                            title,
                                            pollDescription,
                                            author,
                                            highestReactions,
                                            thumbnail,
                                            image)).queue();

                                    // Remove it
                                    if (removePoll(guildID, poll)) {
                                        polls.remove(poll);
                                    }

                                }, (failure) -> {

                                    // This means the message does not exist.
                                    if (removePoll(guildID, poll)) {
                                        polls.remove(poll);
                                    }
                                });

                            }

                        }

                    }

                });

            }
        };
        pollTimer.scheduleAtFixedRate(pollTimerTask, 0, 30000);
    }

    /**
     * Creates a {@link Poll} {@link MessageEmbed}.
     * @param pollTitle The {@link String title} of the {@link Poll}.
     * @param pollDescription The {@link String description} of the {@link Poll}.
     * @param winners The {@link Emoji winners} of the poll.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    private MessageEmbed pollEmbed(@NotNull String pollTitle, @NotNull String pollDescription,
                                   @Nullable MessageEmbed.AuthorInfo author, @NotNull ArrayList<Emoji> winners,
                                   @Nullable MessageEmbed.Thumbnail thumbnail, @Nullable MessageEmbed.ImageInfo image) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(pollTitle)
                .setDescription(pollDescription)
                .setFooter("This poll has ended.");

        if (author != null)
            embedBuilder.setAuthor(author.getName());

        StringBuilder winnersBuilder = new StringBuilder();

        if (winners.isEmpty()) {
            winnersBuilder.append("No one voted...");
            embedBuilder.setColor(Color.gray);
        } else {
            for (Emoji emote : winners) {
                winnersBuilder.append(emote.getFormatted()).append(" ");
            }
            embedBuilder.setColor(Color.red);
        }
        embedBuilder.addField("Poll Results", winnersBuilder.toString(), true);

        if (thumbnail != null)
            embedBuilder.setThumbnail(thumbnail.getUrl());

        if (image != null)
            embedBuilder.setImage(image.getUrl());

        return embedBuilder.build();
    }

    /**
     * Gets all {@link Poll} from the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     */
    public void getAllPolls() {
        try {
            Bot.getCafeAPI().POLL.getAllPolls().forEach((guildID, polls) -> {
                if (!activePolls.containsKey(guildID)) {
                    activePolls.put(guildID, new ArrayList<>());
                }

                for (io.github.beanbeanjuice.cafeapi.cafebot.polls.Poll poll : polls) {
                    activePolls.get(guildID).add(new Poll(poll.getMessageID(), poll.getEndingTime()));
                }
            });
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Getting Polls: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a {@link Poll} to the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the {@link Poll}.
     * @param poll The {@link Poll} to add.
     * @return True, if the {@link Poll} was added successfully.
     */
    @NotNull
    public Boolean addPoll(@NotNull String guildID, @NotNull Poll poll) {
        try {
            Bot.getCafeAPI().POLL.createPoll(guildID, poll);

            if (!activePolls.containsKey(guildID)) {
                activePolls.put(guildID, new ArrayList<>());
            }

            activePolls.get(guildID).add(poll);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Creating Poll: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Removes a {@link Poll} from the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the {@link Poll}.
     * @param poll The {@link Poll poll} to remove.
     * @return True, if the {@link Poll} was removed successfully.
     */
    @NotNull
    private Boolean removePoll(@NotNull String guildID, @NotNull Poll poll) {
        try {
            Bot.getCafeAPI().POLL.deletePoll(guildID, poll);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Removing Poll: " + e.getMessage());
            return false;
        }
    }


}