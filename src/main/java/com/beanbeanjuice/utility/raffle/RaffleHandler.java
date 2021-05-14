package com.beanbeanjuice.utility.raffle;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RaffleHandler {

    private HashMap<String, ArrayList<Raffle>> raffles;

    public RaffleHandler() {
        raffles = new HashMap<>();

        getRafflesFromDatabase();
        // TODO: Start the timer.

    }

    @NotNull
    public Boolean addRaffle(@NotNull Raffle raffle) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "INSERT INTO beanbot.raffles (guild_id, message_id, ending_time, winner_amount) VALUES (?,?,?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(raffle.getGuildID()));
            statement.setLong(2, Long.parseLong(raffle.getMessageID()));
            statement.setTimestamp(3, raffle.getRaffleEndTime());
            statement.setInt(4, raffle.getWinnerAmount());

            statement.execute();
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Adding Raffle: " + e.getMessage());
            return false;
        }

        if (raffles.get(raffle.getGuildID()) == null) {
            raffles.put(raffle.getGuildID(), new ArrayList<>());
        }

        raffles.get(raffle.getGuildID()).add(raffle);
        return true;

    }

    @NotNull
    public ArrayList<Raffle> getRafflesForGuild(@NotNull String guildID) {
        return raffles.get(guildID);
    }

    @NotNull
    public ArrayList<Raffle> getRafflesForGuild(@NotNull Guild guild) {
        return getRafflesForGuild(guild.getId());
    }

    private void getRafflesFromDatabase() {
        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM beanbot.raffles;";

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
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Retrieving Raffles: " + e.getMessage());
        }
    }

}
