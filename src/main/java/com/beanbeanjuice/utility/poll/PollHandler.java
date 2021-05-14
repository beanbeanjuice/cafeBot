package com.beanbeanjuice.utility.poll;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;

/**
 * A class used for handling {@link Poll}s.
 *
 * @author beanbeanjuice
 */
public class PollHandler {

    private HashMap<String, ArrayList<Poll>> activePolls;
    private Timer pollTimer;
    private TimerTask pollTimerTask;

    public PollHandler() {
        activePolls = new HashMap<>();

        getPollsFromDatabase();
        startPollTimer();
        // TODO: Timertask
    }

    private void startPollTimer() {
        pollTimer = new Timer();
        pollTimerTask = new TimerTask() {

            @Override
            public void run() {

                activePolls.forEach((key, value) -> {

                    // Go through each poll
                    for (Poll poll : value) {

                        // Check if the poll SHOULD be calculated
                        Long timeBetween = BeanBot.getGeneralHelper().compareTwoTimeStamps(poll.getPollEndTime(), new Timestamp(System.currentTimeMillis()), TimestampDifference.MINUTES);
                        // If it is greater than 0, check it.
                        if (timeBetween > 0) {
                            // Check if the poll channel still exists.
                            TextChannel pollChannel = BeanBot.getGuildHandler().getCustomGuild(key).getPollChannel();

                            if (pollChannel == null) {
                                removePoll(poll);
                                value.remove(poll);
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

                                    for (MessageReaction messageReaction : messageReactions) {
                                        if (messageReaction.getCount() == highestReaction && messageReaction.getCount() != 1) {
                                            highestReactions.add(messageReaction.getReactionEmote());
                                        }
                                    }

                                    String title = message.getEmbeds().get(0).getAuthor().getName();
                                    String pollDescription = message.getEmbeds().get(0).getDescription();

                                    message.editMessage(pollEmbed(title, pollDescription, highestReactions)).queue();

                                    // Remove it
                                    removePoll(poll);
                                    value.remove(poll);

                                }, (failure) -> {
                                    removePoll(poll);
                                    value.remove(poll);
                                });

                            }

                        }

                    }

                });

            }
        };
        pollTimer.scheduleAtFixedRate(pollTimerTask, 0, 30000);
    }

    @NotNull
    public MessageEmbed pollEmbed(@NotNull String pollTitle, @NotNull String pollDescription,
                                  @NotNull ArrayList<MessageReaction.ReactionEmote> winners) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(pollTitle);
        embedBuilder.setDescription(pollDescription);
        embedBuilder.setFooter("This poll has ended.");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());

        StringBuilder winnersBuilder = new StringBuilder();

        if (winners.isEmpty()) {
            winnersBuilder.append("No one voted...");
        } else {
            for (MessageReaction.ReactionEmote emote : winners) {
                winnersBuilder.append(emote.getEmoji());
            }
        }
        embedBuilder.addField("Winners", winnersBuilder.toString(), true);
        return embedBuilder.build();
    }

    public Boolean addPoll(Poll poll) {

        String guildID = poll.getGuildID();
        String messageID = poll.getMessageID();

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "INSERT INTO beanbot.polls (guild_id, message_id, ending_time) VALUES (?,?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guildID));
            statement.setLong(2, Long.parseLong(messageID));
            statement.setTimestamp(3, poll.getPollEndTime());

            statement.execute();
        } catch (SQLException e) {
            return false;
        }

        if (activePolls.get(guildID) == null) {
            activePolls.put(guildID, new ArrayList<>());
        }

        activePolls.get(guildID).add(poll);
        return true;
    }

    private void getPollsFromDatabase() {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM beanbot.polls;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(arguments);

            while (resultSet.next()) {
                String guildID = String.valueOf(resultSet.getLong(1));
                String messageID = String.valueOf(resultSet.getLong(2));
                Timestamp pollEndTime = resultSet.getTimestamp(3);

                if (activePolls.get(guildID) == null) {
                    activePolls.put(guildID, new ArrayList<>());
                }

                activePolls.get(guildID).add(new Poll(guildID, messageID, pollEndTime));
            }
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Retrieving Polls from Database: " + e.getMessage());
        }

    }

    @NotNull
    private Boolean removePoll(@NotNull Poll poll) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "DELETE FROM beanbot.polls WHERE guild_id = (?) AND message_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(poll.getGuildID()));
            statement.setLong(2, Long.parseLong(poll.getMessageID()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Removing Poll from Database: " + e.getMessage());
            return false;
        }

    }

    @NotNull
    public ArrayList<Poll> getPollsForGuild(@NotNull String guildID) {
        if (activePolls.get(guildID) != null) {
            return activePolls.get(guildID);
        }
        return new ArrayList<>();
    }

    @NotNull
    public ArrayList<Poll> getPollsForGuild(@NotNull Guild guild) {
        return getPollsForGuild(guild.getId());
    }


}
