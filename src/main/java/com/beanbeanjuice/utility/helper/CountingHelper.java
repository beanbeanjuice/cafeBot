package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.main.BeanBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A class used for helping with counting.
 *
 * @author beanbeanjuice
 */
public class CountingHelper {

    public void checkNumber(@NotNull GuildMessageReceivedEvent event, @NotNull Integer currentNumber) {

        Guild guild = event.getGuild();
        // Get the last number in the text channel

        Integer lastNumber = getLastNumber(guild);
        Integer highestNumber = getHighestNumber(guild);
        String lastUserID = getLastUserID(guild);

        if (lastNumber == null || highestNumber == null || lastUserID == null) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        if (currentNumber == (lastNumber+1) && !lastUserID.equals(event.getAuthor().getId())) {

            // Set the last number to the current number
            // If it fails, say so and return.

            if (!setLastNumber(guild, currentNumber)) {
                event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            if (currentNumber > highestNumber) {
                // Set the highest number to the current number.
                // If it fails, say so and return.
                if (!setHighestNumber(guild, currentNumber)) {
                    event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
                    return;
                }

            }

            if (!setLastUserID(guild, event.getAuthor().getId())) {
                event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            // Add Green Checkmark Reaction
            event.getMessage().addReaction("U+2705").queue();
        } else {

            if (!setLastNumber(guild, 0)) {
                event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            if (!setLastUserID(guild, "0")) {
                event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            event.getMessage().addReaction("U+274C").queue();
            event.getChannel().sendMessage(failedEmbed(event.getMember(), lastNumber, highestNumber)).queue();
        }
    }

    private Boolean setLastUserID(@NotNull Guild guild, @NotNull String lastUserID) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.counting_information SET last_user_id = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(lastUserID));
            statement.setLong(2, Long.parseLong(guild.getId()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    private String getLastUserID(@NotNull Guild guild) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM beanbot.counting_information WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guild.getId()));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString(4);
        } catch (SQLException e) {
            return null;
        }

    }

    private MessageEmbed failedEmbed(@NotNull Member member, @NotNull Integer lastNumber, @NotNull Integer highestNumber) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Counting Failed");
        embedBuilder.setDescription("Counting failed due to " + member.getAsMention() + " at `" + lastNumber + "`. " +
                "The highest number received on this server was `" + highestNumber + "`. Counting has been reset to `0`. " +
                "Remember, the same user can't count twice in a row and the numbers must increment by 1!");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    private Boolean setHighestNumber(@NotNull Guild guild, @NotNull Integer currentNumber) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.counting_information SET highest_number = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setInt(1, currentNumber);
            statement.setLong(2, Long.parseLong(guild.getId()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    private Boolean setLastNumber(@NotNull Guild guild, @NotNull Integer currentNumber) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.counting_information SET last_number = (?) WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setInt(1, currentNumber);
            statement.setLong(2, Long.parseLong(guild.getId()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    @NotNull
    public Boolean createNewRow(@NotNull Guild guild) {

        if (getHighestNumber(guild) != null) {
            return false;
        }

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "INSERT IGNORE INTO beanbot.counting_information (guild_id) VALUES (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guild.getId()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    @Nullable
    private Integer getHighestNumber(@NotNull Guild guild) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM beanbot.counting_information WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guild.getId()));

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            return resultSet.getInt(2);
        } catch (SQLException e) {
            return null;
        }

    }

    @Nullable
    private Integer getLastNumber(@NotNull Guild guild) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM beanbot.counting_information WHERE guild_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guild.getId()));

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            return resultSet.getInt(3);
        } catch (SQLException e) {
            return null;
        }

    }

}
