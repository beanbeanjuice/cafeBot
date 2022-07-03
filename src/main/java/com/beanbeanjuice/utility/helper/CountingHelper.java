package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.logging.LogLevel;
import io.github.beanbeanjuice.cafeapi.cafebot.counting.CountingInformation;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A class used for helping with counting.
 *
 * @author beanbeanjuice
 */
public class CountingHelper {

    private HashMap<String, CountingInformation> countingInformationMap;

    /**
     * Creates a new {@link CountingHelper} class.
     */
    public CountingHelper() {
        countingInformationMap = new HashMap<>();
        cacheCountingInformation();
    }

    /**
     * Caches the current counting information from the database.
     */
    public void cacheCountingInformation() {
        Bot.getLogger().log(this.getClass(), LogLevel.LOADING, "Caching Counting Information...");

        try {
            countingInformationMap = Bot.getCafeAPI().COUNTING_INFORMATION.getAllCountingInformation();
            Bot.getLogger().log(this.getClass(), LogLevel.OKAY, "Successfully Cached Counting Information.");
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Getting Guild Counting Information: " + e.getMessage(), e);
        }
    }

    /**
     * Checks the current number for the {@link Guild}.
     * @param event The {@link MessageReceivedEvent} for the {@link Guild}.
     * @param currentNumber The current number for the {@link Guild}.
     */
    public void checkNumber(@NotNull MessageReceivedEvent event, @NotNull Integer currentNumber) {
        Guild guild = event.getGuild();

        CountingInformation countingInformation = getCountingInformation(guild);

        // Checks if there was an error getting the counting information.
        if (countingInformation == null) {
            event.getChannel().sendMessageEmbeds(Helper.sqlServerError()).queue();
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
                    highestNumber, currentNumber, event.getAuthor().getId(), countingInformation.getFailureRoleID()
            );

            try {
                Bot.getCafeAPI().COUNTING_INFORMATION.updateGuildCountingInformation(guild.getId(), newCountingInformation);
                countingInformationMap.put(guild.getId(), newCountingInformation);
            } catch (CafeException e) {
                event.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                        "Error Updating Counting Information",
                        "There was an error updating counting information."
                )).queue();
                Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Updating Counting Information: " + e.getMessage(), e);
                return;
            }

            // Checking if the current number is now the highest number.
            if (highestChanged) {
                // Set the highest number to the current number.
                // If it fails, say so and return.
                event.getMessage().addReaction(Emoji.fromFormatted("☑")).queue(); // Blue Checkmark Reaction
            } else {
                event.getMessage().addReaction(Emoji.fromFormatted("U+2705")).queue(); // Green Checkmark Reaction
            }

            // Checks if the current number is divisible by 100.
            if (currentNumber % 100 == 0) {
                event.getMessage().addReaction(Emoji.fromFormatted("U+1F31F")).queue(); // Star Reaction for if they get to a number that is divisible by 100.
            }

        } else {

            // Resetting back to 0.
            try {
                CountingInformation newCountingInformation = new CountingInformation(
                        countingInformation.getHighestNumber(),
                        0,
                        "0",
                        countingInformation.getFailureRoleID()
                );
                Bot.getCafeAPI().COUNTING_INFORMATION.updateGuildCountingInformation(guild.getId(), newCountingInformation);
                countingInformationMap.put(guild.getId(), newCountingInformation);

            } catch (CafeException e) {
                event.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                        "Error Resetting Counting Information",
                        "You lucked out! There was an error updating the counting information, so you didn't fail!"
                )).queue();
                return;
            }

            event.getMessage().addReaction(Emoji.fromFormatted("U+274C")).queue();
            event.getChannel().sendMessageEmbeds(failedEmbed(event.getMember(), lastNumber, highestNumber)).queue();

            // Giving the counting failure role.
            Role failureRole = Helper.getRole(event.getGuild(), countingInformation.getFailureRoleID());

            if (failureRole != null) {
                try {
                    event.getGuild().addRoleToMember(event.getMember(), failureRole).queue();
                } catch (IllegalArgumentException ignored) {}
                catch (InsufficientPermissionException e) {
                    event.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Giving Counting Failure Role",
                            "I do not have permissions to change/add/remove roles :("
                    )).queue();
                } catch (HierarchyException e) {
                    event.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Giving Counting Failure Role",
                            "The role I am trying to give is higher than the roles I have :("
                    )).queue();
                }
            }
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
        if (!countingInformationMap.containsKey(guildID)) {
            try {
                Bot.getCafeAPI().COUNTING_INFORMATION.createGuildCountingInformation(guildID);
                CountingInformation countingInformation = new CountingInformation(0, 0, "0", "0");
                countingInformationMap.put(guildID, countingInformation);
                Bot.getLogger().log(this.getClass(), LogLevel.DEBUG, "Guild ID: " + guildID);
                return countingInformation;
            } catch (ConflictException e1) {
                CountingInformation countingInformation = Bot.getCafeAPI().COUNTING_INFORMATION.getGuildCountingInformation(guildID);
                countingInformationMap.put(guildID, countingInformation);
                return countingInformation;
            } catch (CafeException e2) {
                Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Creating Counting Information: " + e2.getMessage(), e2);
                return null;
            }
        }

        return countingInformationMap.get(guildID);
    }

    /**
     * Sets a {@link net.dv8tion.jda.api.entities.Role countinFailureRole} for a specified {@link String guildID}.
     * @param guildID The specified {@link String guildID}.
     * @param countingFailureRoleID The new {@link String countingFailureRoleID}.
     * @return True, if updated successfully.
     */
    @NotNull
    public Boolean setCountingFailureRoleID(@NotNull String guildID, @NotNull String countingFailureRoleID) {
        CountingInformation currentCountingInformation = getCountingInformation(guildID);
        if (currentCountingInformation == null) {
            return false;
        }

        try {
            CountingInformation newCountingInformation = new CountingInformation(
                    currentCountingInformation.getHighestNumber(),
                    currentCountingInformation.getLastNumber(),
                    currentCountingInformation.getLastUserID(),
                    countingFailureRoleID);
            Bot.getCafeAPI().COUNTING_INFORMATION.updateGuildCountingInformation(guildID, newCountingInformation);

            countingInformationMap.put(guildID, newCountingInformation);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Updating Counting Failure Role: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Get the leaderboard place for a specified {@link Integer} limit.
     * @param limit The limit specified.
     * @return The current place of that {@link Integer}.
     */
    @Nullable
    public Integer getCountingLeaderboardPlace(@NotNull Integer limit) {
        ArrayList<Integer> places = new ArrayList<>();

        countingInformationMap.forEach((guildID, countingInformation) -> {
            if (places.contains(countingInformation.getHighestNumber())) {
                places.add(0);
            } else {
                places.add(countingInformation.getHighestNumber());
            }
        });

        // Sorts the places in descending order.
        places.sort(Collections.reverseOrder());

        return places.indexOf(limit) + 1;
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
