package com.beanbeanjuice.utility.poll;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageReaction;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used for handling {@link Poll}s.
 *
 * @author beanbeanjuice
 */
public class PollHandler {

    private HashMap<String, ArrayList<Poll>> activePolls;

    public PollHandler() {
        activePolls = new HashMap<>();
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
    private Boolean removePollFromDatabase(@NotNull Poll poll) {

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
