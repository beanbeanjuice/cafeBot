package com.beanbeanjuice.cafebot.utility.section.fun;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays.Birthday;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays.BirthdayMonth;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.TeaPotException;
import com.beanbeanjuice.cafeapi.wrapper.utility.Time;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.util.*;

/**
 * A class used to handle {@link Birthday}.
 *
 * @author beanbeanjuice
 */
public class BirthdayHandler {

    private static HashMap<String, Birthday> birthdays;

    /**
     * Start the {@link BirthdayHandler}.
     */
    public static void start() {
        birthdays = new HashMap<>();
        getAllBirthdays();
        startBirthdayTimer();
    }

    /**
     * A timer used to check for birthdays.
     */
    private static void startBirthdayTimer() {
        Timer birthdayTimer = new Timer();
        TimerTask birthdayTimerTask = new TimerTask() {

            @Override
            public void run() {

                birthdays.forEach((userID, birthday) -> {

                    // Check if it IS someone's birthday.
                    if (isBirthday(birthday)) {

                        // Check if they have already been mentioned today.
                        if (!birthday.isAlreadyMentioned()) {

                            // Update it for all guilds where there is a birthday channel.
                            for (Guild guild : Bot.getBot().getGuilds()) {

                                Member member = guild.getMemberById(userID);
                                if (member != null) {

                                    // Making sure the BirthdayChannel is not null.
                                    TextChannel birthdayChannel = GuildHandler.getCustomGuild(guild).getBirthdayChannel();

                                    if (birthdayChannel != null) {
                                        birthdayChannel.sendMessageEmbeds(birthdayEmbed(member)).queue();
                                    }
                                }
                            }

                            // PM them a happy birthday.
                            Helper.getUser(userID).ifPresent((birthdayUser) -> {
                                Helper.pmUser(birthdayUser, "Hey... we don't know if anyone wished you " +
                                        "a happy birthday, but happy birthday <3!");

                                updateMentionedBirthday(userID, true);
                            });
                        }

                    } else {
                        if (birthday.isAlreadyMentioned() && !isBirthday(birthday))
                            updateMentionedBirthday(userID, false);
                    }
                });

                getAllBirthdays();
            }
        };
        birthdayTimer.scheduleAtFixedRate(birthdayTimerTask, 0, 60000); // Should be 60000 for 30 minutes.
    }

    /**
     * A birthday {@link MessageEmbed} to send in the Birthday {@link TextChannel} for each {@link Guild}.
     * @param member The {@link Member} who's birthday it is.
     * @return The completed {@link MessageEmbed}.
     */
    @NotNull
    private static MessageEmbed birthdayEmbed(@NotNull Member member) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("BIRTHDAY ALERT");
        embedBuilder.setDescription("ATTENTION! It is currently " + member.getAsMention() + "'s birthday! Wish them a happy birthday everyone!");
        embedBuilder.setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    /**
     * Updates the {@link Boolean mentioned} status of a {@link Birthday}.
     * @param userID The {@link String userID} of the {@link Birthday}.
     * @param isMentioned The new {@link Boolean mentioned} status.
     */
    private static void updateMentionedBirthday(@NotNull String userID, @NotNull Boolean isMentioned) {
        try {
            Bot.getCafeAPI().BIRTHDAY.updateUserBirthdayMention(userID, isMentioned);
        } catch (CafeException e) {
            Bot.getLogger().log(BirthdayHandler.class, LogLevel.ERROR, "Error Updating User Birthday Mention: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all {@link Birthday} from the {@link CafeAPI CafeAPI}.
     */
    private static void getAllBirthdays() {
        try {
            birthdays = Bot.getCafeAPI().BIRTHDAY.getAllBirthdays();
        } catch (CafeException | ParseException e) {
            Bot.getLogger().log(BirthdayHandler.class, LogLevel.ERROR, "Error Retrieving Birthdays: " + e.getMessage(), e);
        }
    }

    /**
     * Removes a {@link Birthday} from the {@link CafeAPI CafeAPI}.
     * @param userID The {@link String userID} of the {@link Birthday} to remove.
     * @return True, if the {@link Birthday} was removed successfully.
     */
    @NotNull
    public static Boolean removeBirthday(@NotNull String userID) {
        try {
            Bot.getCafeAPI().BIRTHDAY.removeUserBirthday(userID);
            birthdays.remove(userID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(BirthdayHandler.class, LogLevel.ERROR, "Error Removing Birthday: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Updates/Creates a {@link Birthday} for a specified {@link String userID}.
     * @param userID The {@link String userID} to create the {@link Birthday} for.
     * @param birthday The {@link Birthday} itself.
     * @return True, if the {@link Birthday} was successfully created.
     * @throws TeaPotException Thrown when the {@link BirthdayMonth month} or {@link Integer day} is invalid.
     */
    @NotNull
    public static Boolean updateBirthday(@NotNull String userID, @NotNull Birthday birthday) throws TeaPotException {
        try {
            Bot.getCafeAPI().BIRTHDAY.updateUserBirthday(userID, birthday);
            birthdays.put(userID, birthday);
            updateMentionedBirthday(userID, false);
            return true;
        } catch (NotFoundException e) {
            try {
                Bot.getCafeAPI().BIRTHDAY.createUserBirthday(userID, birthday);
                birthdays.put(userID, birthday);
                updateMentionedBirthday(userID, false);
                return true;
            } catch (CafeException e2) {
                Bot.getLogger().log(BirthdayHandler.class, LogLevel.ERROR, "Error Creating Birthday: " + e2.getMessage(), e2);
                return false;
            }
        }catch (CafeException e) {
            Bot.getLogger().log(BirthdayHandler.class, LogLevel.ERROR, "Error Updating Birthday: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Retrieves a {@link Birthday} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return The {@link Birthday} for the {@link String userID}. Null, if the {@link Birthday} does not exist.
     */
    @Nullable
    public static Birthday getBirthday(@NotNull String userID) {
        try {
            return Bot.getCafeAPI().BIRTHDAY.getUserBirthday(userID);
        }
        catch (NotFoundException | ParseException ignored) { return null; }
        catch (CafeException e) {
            Bot.getLogger().log(BirthdayHandler.class, LogLevel.ERROR, "Error Retrieving User Birthday: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param birthday The {@link Birthday} to check.
     * @return True, if the current {@link Date} is a {@link Birthday}.
     */
    @NotNull
    private static Boolean isBirthday(@NotNull Birthday birthday) {
        // TODO: Seems like it is one day off?
        String month = String.valueOf(birthday.getMonth().getMonthNumber());
        String day = String.valueOf(birthday.getDay());
        String dateString = month + "-" + day + "-2020";

        try {
            return Time.dateHasPassed(dateString, birthday.getTimeZone()) && Time.isSameDay(Time.getFullDate(dateString, birthday.getTimeZone()));
        } catch (ParseException e) {
            return false;
        }
    }

}
