package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.cafebot.counting.CountingInformation;
import com.beanbeanjuice.cafeapi.exception.AuthorizationException;
import com.beanbeanjuice.cafeapi.exception.CafeException;
import com.beanbeanjuice.cafeapi.exception.ConflictException;
import com.beanbeanjuice.cafeapi.exception.ResponseException;
import com.beanbeanjuice.utility.logger.LogLevel;
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

    /**
     * Checks the current number for the {@link Guild}.
     * @param event The {@link GuildMessageReceivedEvent} for the {@link Guild}.
     * @param currentNumber The current number for the {@link Guild}.
     */
    public void checkNumber(@NotNull GuildMessageReceivedEvent event, @NotNull Integer currentNumber) {
        Guild guild = event.getGuild();

        CountingInformation countingInformation = getCountingInformation(guild);

        // Checks if there was an error getting the counting information.
        if (countingInformation == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        Integer lastNumber = countingInformation.getLastNumber();
        Integer highestNumber = countingInformation.getHighestNumber();
        String lastUserID = countingInformation.getLastUserID();

        if (currentNumber == (lastNumber+1) && !lastUserID.equals(event.getAuthor().getId())) {

            boolean highestChanged = false;

            // Checking if the highest number has changed.
            if (currentNumber > highestNumber) {
                highestNumber = currentNumber;
                highestChanged = true;
            }

            CountingInformation newCountingInformation = new CountingInformation(
                    highestNumber, currentNumber, event.getAuthor().getId()
            );

            try {
                CafeBot.getCafeAPI().countingInformations().updateGuildCountingInformation(guild.getId(), newCountingInformation);
            } catch (CafeException e) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                        "Error Updating Counting Information",
                        "There was an error updating counting information."
                )).queue();
                CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Updating Counting Information: " + e.getMessage(), e);
                return;
            }

            // Checking if the current number is now the highest number.
            if (highestChanged) {
                // Set the highest number to the current number.
                // If it fails, say so and return.
                event.getMessage().addReaction("☑").queue(); // Blue Checkmark Reaction
            } else {
                event.getMessage().addReaction("U+2705").queue(); // Green Checkmark Reaction
            }

            // Checks if the current number is divisible by 100.
            if (currentNumber % 100 == 0) {
                event.getMessage().addReaction("U+1F31F").queue(); // Star Reaction for if they get to a number that is divisible by 100.
            }

        } else {

            try {
                CafeBot.getCafeAPI().countingInformations().updateGuildCountingInformation(
                        guild.getId(),
                        countingInformation.getHighestNumber(),
                        0, "0");
            } catch (CafeException e) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                        "Error Resetting Counting Information",
                        "You lucked out! There was an error updating the counting information, so you didn't fail!"
                )).queue();
                return;
            }

            event.getMessage().addReaction("U+274C").queue();
            event.getChannel().sendMessageEmbeds(failedEmbed(event.getMember(), lastNumber, highestNumber)).queue();
        }
    }

    /**
     * Retrieves the {@link CountingInformation} for a specified {@link Guild}.
     * @param guild The specified {@link Guild}.
     * @return The {@link CountingInformation} for the {@link Guild}. Null, if there was an error.
     */
    @Nullable
    public CountingInformation getCountingInformation(@NotNull Guild guild) {
        return getCountingInformation(guild.getId());
    }

    /**
     * Retrieves the {@link CountingInformation} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @return The {@link CountingInformation} for the {@link String guildID}. Null, if there was an error.
     */
    @Nullable
    public CountingInformation getCountingInformation(@NotNull String guildID) {
        try {
            CafeBot.getCafeAPI().countingInformations().createGuildCountingInformation(guildID);
        } catch (ConflictException ignored) {}
        catch (AuthorizationException | ResponseException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Creating Counting Information: " + e.getMessage(), e);
            return null;
        }

        try {
            return CafeBot.getCafeAPI().countingInformations().getGuildCountingInformation(guildID);
        } catch (CafeException e) {
            return null;
        }
    }

    /**
     * Get the leaderboard place for a specified {@link Integer} limit.
     * @param limit The limit specified.
     * @return The current place of that {@link Integer}.
     */
    @Nullable
    public Integer getCountingLeaderboardPlace(@NotNull Integer limit) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.counting_information WHERE counting_information.highest_number>=(?) ORDER BY counting_information.highest_number DESC;";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setInt(1, limit);
            ResultSet resultSet = statement.executeQuery();
            int count = 0;
            while (resultSet.next()) {
                count++;
            }
            return count;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(CountingHelper.class, LogLevel.WARN, "Error Getting Leaderboard: " + e.getMessage());
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

}
