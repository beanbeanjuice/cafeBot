package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.CafeBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
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

        Integer lastNumber = getLastNumber(guild);
        Integer highestNumber = getHighestNumber(guild);
        String lastUserID = getLastUserID(guild);

        if (lastNumber == null || highestNumber == null || lastUserID == null) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        if (currentNumber == (lastNumber+1) && !lastUserID.equals(event.getAuthor().getId())) {

            // Set the last number to the current number
            // If it fails, say so and return.
            if (!setLastNumber(guild, currentNumber)) {
                event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            if (currentNumber > highestNumber) {
                // Set the highest number to the current number.
                // If it fails, say so and return.
                if (!setHighestNumber(guild, currentNumber)) {
                    event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
                    return;
                }
            }

            if (!setLastUserID(guild, event.getAuthor().getId())) {
                event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }
            event.getMessage().addReaction("U+2705").queue(); // Green Checkmark Reaction

            if (currentNumber % 100 == 0) {
                event.getMessage().addReaction("U+1F31F").queue(); // Star Reaction for if they get to a number that is divisible by 100.
            }

        } else {

            if (!setLastNumber(guild, 0)) {
                event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            if (!setLastUserID(guild, "0")) {
                event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            event.getMessage().addReaction("U+274C").queue();
            event.getChannel().sendMessage(failedEmbed(event.getMember(), lastNumber, highestNumber)).queue();
        }
    }

    /**
     * Sets the last user ID for the counting {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
     * @param guild The {@link Guild} specified.
     * @param lastUserID The ID of the last user who sent the message.
     * @return Whether or not setting the last user ID was successful.
     */
    @NotNull
    private Boolean setLastUserID(@NotNull Guild guild, @NotNull String lastUserID) {

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.counting_information SET last_user_id = (?) WHERE guild_id = (?);";

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

    /**
     * @param guild The {@link Guild} specified.
     * @return The ID of the last user who sent the counting try.
     */
    @Nullable
    private String getLastUserID(@NotNull Guild guild) {

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.counting_information WHERE guild_id = (?);";

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

    /**
     * A failed {@link MessageEmbed} to send if they fail the counting.
     * @param member The {@link Member} who failed.
     * @param lastNumber The last number entered.
     * @param highestNumber The current highest number.
     * @return The {@link MessageEmbed} to send.
     */
    @NotNull
    private MessageEmbed failedEmbed(@NotNull Member member, @NotNull Integer lastNumber, @NotNull Integer highestNumber) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Counting Failed");
        embedBuilder.setDescription("Counting failed due to " + member.getAsMention() + " at `" + lastNumber + "`. " +
                "The highest number received on this server was `" + highestNumber + "`. Counting has been reset to `0`. " +
                "Remember, the same user can't count twice in a row and the numbers must increment by 1!");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    /**
     * Sets the highest number for the {@link Guild}.
     * @param guild The {@link Guild} specified.
     * @param currentNumber The current number for the {@link Guild}.
     * @return Whether or not setting the highest number was successful.
     */
    @NotNull
    private Boolean setHighestNumber(@NotNull Guild guild, @NotNull Integer currentNumber) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.counting_information SET highest_number = (?) WHERE guild_id = (?);";

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

    /**
     * Sets the last number for the {@link Guild}.
     * @param guild The {@link Guild} specified.
     * @param currentNumber The current number for the {@link Guild}.
     * @return Whether or not setting the last number was successful.
     */
    @NotNull
    private Boolean setLastNumber(@NotNull Guild guild, @NotNull Integer currentNumber) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.counting_information SET last_number = (?) WHERE guild_id = (?);";

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

    /**
     * Creates a new row for the Counting Information from the {@link Guild}.
     * @param guild The {@link Guild} specified.
     * @return Whether or not creating a new row was successful.
     */
    @NotNull
    public Boolean createNewRow(@NotNull Guild guild) {
        if (getHighestNumber(guild) != null) {
            return false;
        }

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "INSERT IGNORE INTO cafeBot.counting_information (guild_id) VALUES (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(guild.getId()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * The highest number from the {@link Guild}.
     * @param guild The {@link Guild} specified.
     * @return The highest number for the {@link Guild}.
     */
    @Nullable
    public Integer getHighestNumber(@NotNull Guild guild) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.counting_information WHERE guild_id = (?);";

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

    /**
     * Gets the last number from the {@link Guild}.
     * @param guild The {@link Guild} specified.
     * @return The highest number for the {@link Guild}.
     */
    @Nullable
    public Integer getLastNumber(@NotNull Guild guild) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.counting_information WHERE guild_id = (?);";

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
