package com.beanbeanjuice.utility.sections.fun.birthday;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.cafebot.birthdays.Birthday;
import com.beanbeanjuice.cafeapi.cafebot.birthdays.BirthdayMonth;
import com.beanbeanjuice.cafeapi.exception.CafeException;
import com.beanbeanjuice.cafeapi.exception.NotFoundException;
import com.beanbeanjuice.cafeapi.exception.TeaPotException;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class used to handle birthdays.
 */
public class BirthdayHandler {

    private HashMap<String, Birthday> birthdays;
    private Timer birthdayTimer;
    private TimerTask birthdayTimerTask;

    /**
     * Create a new {@link BirthdayHandler}.
     */
    public BirthdayHandler() {
        birthdays = new HashMap<>();
        getAllBirthdays();
        startBirthdayTimer();
    }

    /**
     * A timer used to check for birthdays.
     */
    public void startBirthdayTimer() {
        birthdayTimer = new Timer();
        birthdayTimerTask = new TimerTask() {

            @Override
            public void run() {

                birthdays.forEach((userID, birthday) -> {

                    if (isBirthday(birthday)) {
                        if (!birthday.alreadyMentioned()) {
                            for (Guild guild : CafeBot.getJDA().getGuilds()) {
                                Member member = guild.getMemberById(userID);
                                if (member != null) {

                                    // Making sure the BirthdayChannel is not null.
                                    TextChannel birthdayChannel = CafeBot.getGuildHandler().getCustomGuild(guild).getBirthdayChannel();

                                    if (birthdayChannel != null) {
                                        birthdayChannel.sendMessageEmbeds(birthdayEmbed(member)).queue();
                                    }
                                }
                            }

                            // PM them a happy birthday.
                            CafeBot.getGeneralHelper().pmUser(CafeBot.getGeneralHelper().getUser(userID), "Hey... we don't know if anyone wished you " +
                                    "a happy birthday, but happy birthday <3!");

                            updateMentionedBirthday(userID, true);
                        }

                    } else {
                        if (birthday.alreadyMentioned() && !isBirthday(birthday)) {
                            updateMentionedBirthday(userID, false);
                        }
                    }
                });

                getAllBirthdays();
            }
        };
        birthdayTimer.scheduleAtFixedRate(birthdayTimerTask, 0, 7200000); // Should be 7200000 for 1 day
    }

    /**
     * A birthday {@link MessageEmbed} to send in the Birthday {@link TextChannel} for each {@link Guild}.
     * @param member The {@link Member} who's birthday it is.
     * @return The completed {@link MessageEmbed}.
     */
    @NotNull
    private MessageEmbed birthdayEmbed(@NotNull Member member) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("BIRTHDAY ALERT");
        embedBuilder.setDescription("ATTENTION! It is currently " + member.getAsMention() + "'s birthday! Wish them a happy birthday everyone!");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    /**
     * Updates the {@link Boolean mentioned} status of a {@link Birthday}.
     * @param userID The {@link String userID} of the {@link Birthday}.
     * @param isMentioned The new {@link Boolean mentioned} status.
     */
    private void updateMentionedBirthday(@NotNull String userID, @NotNull Boolean isMentioned) {
        try {
            CafeBot.getCafeAPI().birthdays().updateUserBirthdayMention(userID, isMentioned);
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Updating User Birthday Mention: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all {@link Birthday} from the {@link com.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     */
    private void getAllBirthdays() {
        try {
            birthdays = CafeBot.getCafeAPI().birthdays().getAllBirthdays();
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Retrieving Birthdays: " + e.getMessage(), e);
            return;
        }
    }

    /**
     * Removes a {@link Birthday} from the {@link com.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param userID The {@link String userID} of the {@link Birthday} to remove.
     * @return True, if the {@link Birthday} was removed successfully.
     */
    @NotNull
    public Boolean removeBirthday(@NotNull String userID) {
        try {
            CafeBot.getCafeAPI().birthdays().removeUserBirthday(userID);
            birthdays.remove(userID);
            return true;
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Removing Birthday: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Updates/Creates a {@link Birthday} for a specified {@link String userID}.
     * @param userID The {@link String userID} to create the {@link Birthday} for.
     * @param month The {@link BirthdayMonth month} of the {@link Birthday}.
     * @param day The {@link Integer day} of the {@link Birthday}.
     * @return True, if the {@link Birthday} was successfully created.
     * @throws TeaPotException Thrown when the {@link BirthdayMonth month} or {@link Integer day} is invalid.
     */
    @NotNull
    public Boolean updateBirthday(@NotNull String userID, @NotNull BirthdayMonth month, @NotNull Integer day) throws TeaPotException {
        try {
            CafeBot.getCafeAPI().birthdays().updateUserBirthday(userID, month, day);
            birthdays.put(userID, new Birthday(month, day, false));
            updateMentionedBirthday(userID, false);
            return true;
        } catch (NotFoundException e) {
            try {
                CafeBot.getCafeAPI().birthdays().createUserBirthday(userID, month, day);
                birthdays.put(userID, new Birthday(month, day, false));
                updateMentionedBirthday(userID, false);
                return true;
            } catch (CafeException e2) {
                CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Creating Birthday: " + e2.getMessage(), e2);
                return false;
            }
        }catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Updating Birthday: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Retrieves a {@link Birthday} for a specified {@link String userID}.
     * @param userID The specified {@link String userID}.
     * @return The {@link Birthday} for the {@link String userID}. Null, if the {@link Birthday} does not exist.
     */
    @Nullable
    public Birthday getBirthday(@NotNull String userID) {
        try {
            return CafeBot.getCafeAPI().birthdays().getUserBirthday(userID);
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Retrieving User Birthday: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param birthday The {@link Birthday} to check.
     * @return True, if the current {@link Date} is a {@link Birthday}.
     */
    @NotNull
    private Boolean isBirthday(@NotNull Birthday birthday) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate today = formatter.parseLocalDate(new Date(System.currentTimeMillis()).toString());

        return today.getDayOfMonth() == birthday.getDay() && today.getMonthOfYear() == birthday.getMonth().getMonthNumber();
    }

}
