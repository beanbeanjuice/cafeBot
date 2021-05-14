package com.beanbeanjuice.utility.poll;

import com.beanbeanjuice.main.BeanBot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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



}
