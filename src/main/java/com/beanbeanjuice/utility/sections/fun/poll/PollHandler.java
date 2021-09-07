package com.beanbeanjuice.utility.sections.fun.poll;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.exception.CafeException;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A class used for handling {@link Poll}.
 *
 * @author beanbeanjuice
 */
public class PollHandler {

    private HashMap<String, ArrayList<Poll>> activePolls;
    private Timer pollTimer;
    private TimerTask pollTimerTask;

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
        pollTimer = new Timer();
        pollTimerTask = new TimerTask() {

            @Override
            public void run() {

                activePolls.forEach((guildID, polls) -> {

                    // Go through each poll
                    for (Poll poll : polls) {

                        // Check if the poll SHOULD be calculated
                        if (poll.isFinished()) {
                            // Check if the poll channel still exists.
                            TextChannel pollChannel = CafeBot.getGuildHandler().getCustomGuild(guildID).getPollChannel();

                            if (pollChannel == null) {
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

                                    for (MessageReaction messageReaction : messageReactions) {
                                        if (messageReaction.getCount() > highestReaction) {
                                            highestReaction = messageReaction.getCount();
                                        }
                                    }

                                    // Now we have the highest reaction number.
                                    ArrayList<MessageReaction.ReactionEmote> highestReactions = new ArrayList<>();

                                    // Gets the amount of reactions for that message.
                                    for (MessageReaction messageReaction : messageReactions) {
                                        if (messageReaction.getCount() == highestReaction && messageReaction.getCount() != 1) {
                                            highestReactions.add(messageReaction.getReactionEmote());
                                        }
                                    }

                                    String title = message.getEmbeds().get(0).getTitle();
                                    String pollDescription = message.getEmbeds().get(0).getDescription();

                                    message.editMessageEmbeds(pollEmbed(title, pollDescription, highestReactions)).queue();

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
     * @param winners The {@link net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote winners} of the poll.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    private MessageEmbed pollEmbed(@NotNull String pollTitle, @NotNull String pollDescription,
                                  @NotNull ArrayList<MessageReaction.ReactionEmote> winners) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(pollTitle);
        embedBuilder.setDescription(pollDescription);
        embedBuilder.setFooter("This poll has ended.");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());

        StringBuilder winnersBuilder = new StringBuilder();

        if (winners.isEmpty()) {
            winnersBuilder.append("No one voted...");
        } else {
            for (MessageReaction.ReactionEmote emote : winners) {
                winnersBuilder.append(emote.getEmoji()).append(" ");
            }
        }
        embedBuilder.addField("Poll Results", winnersBuilder.toString(), true);
        return embedBuilder.build();
    }

    /**
     * Gets all {@link Poll} from the {@link com.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     */
    public void getAllPolls() {
        try {
            CafeBot.getCafeAPI().polls().getAllPolls().forEach((guildID, polls) -> {
                if (!activePolls.containsKey(guildID)) {
                    activePolls.put(guildID, new ArrayList<>());
                }

                for (com.beanbeanjuice.cafeapi.cafebot.polls.Poll poll : polls) {
                    activePolls.get(guildID).add(new Poll(poll.getMessageID(), poll.getEndingTime()));
                }
            });
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Getting Polls: " + e.getMessage(), e);
            return;
        }
    }

    /**
     * Adds a {@link Poll} to the {@link com.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the {@link Poll}.
     * @param poll The {@link Poll} to add.
     * @return True, if the {@link Poll} was added successfully.
     */
    @NotNull
    public Boolean addPoll(@NotNull String guildID, @NotNull Poll poll) {
        try {
            CafeBot.getCafeAPI().polls().createPoll(guildID, poll);

            if (!activePolls.containsKey(guildID)) {
                activePolls.put(guildID, new ArrayList<>());
            }

            activePolls.get(guildID).add(poll);
            return true;
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Creating Poll: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Removes a {@link Poll} from the {@link com.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param guildID The {@link String guildID} of the {@link Poll}.
     * @param poll The {@link Poll poll} to remove.
     * @return True, if the {@link Poll} was removed successfully.
     */
    @NotNull
    private Boolean removePoll(@NotNull String guildID, @NotNull Poll poll) {
        try {
            CafeBot.getCafeAPI().polls().deletePoll(guildID, poll);
            return true;
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Removing Poll: " + e.getMessage());
            return false;
        }
    }


}
