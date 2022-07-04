package com.beanbeanjuice.utility.section.fun;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
import io.github.beanbeanjuice.cafeapi.cafebot.birthdays.Birthday;
import io.github.beanbeanjuice.cafeapi.cafebot.birthdays.BirthdayMonth;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import io.github.beanbeanjuice.cafeapi.exception.TeaPotException;
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

import java.sql.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class used to handle {@link Birthday}.
 *
 * @author beanbeanjuice
 */
public class BirthdayHandler {

    private HashMap<String, Birthday> birthdays;

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
        Timer birthdayTimer = new Timer();
        TimerTask birthdayTimerTask = new TimerTask() {

            @Override
            public void run() {

                birthdays.forEach((userID, birthday) -> {

                    // Check if it IS someone's birthday.
                    if (isBirthday(birthday)) {

                        // Check if they have already been mentioned today.
                        if (!birthday.alreadyMentioned()) {

                            // Update it for all guilds where there is a birthday channel.
                            for (Guild guild : Bot.getBot().getGuilds()) {

                                Member member = guild.getMemberById(userID);
                                if (member != null) {

                                    // Making sure the BirthdayChannel is not null.
                                    TextChannel birthdayChannel = Bot.getGuildHandler().getCustomGuild(guild).getBirthdayChannel();

                                    if (birthdayChannel != null) {
                                        birthdayChannel.sendMessageEmbeds(birthdayEmbed(member)).queue();
                                    }
                                }
                            }

                            // PM them a happy birthday.
                            Helper.pmUser(Helper.getUser(userID), "Hey... we don't know if anyone wished you " +
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
        birthdayTimer.scheduleAtFixedRate(birthdayTimerTask, 0, 60000); // Should be 60000 for 30 minutes.
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
        embedBuilder.setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    /**
     * Updates the {@link Boolean mentioned} status of a {@link Birthday}.
     * @param userID The {@link String userID} of the {@link Birthday}.
     * @param isMentioned The new {@link Boolean mentioned} status.
     */
    private void updateMentionedBirthday(@NotNull String userID, @NotNull Boolean isMentioned) {
        try {
            Bot.getCafeAPI().BIRTHDAY.updateUserBirthdayMention(userID, isMentioned);
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Updating User Birthday Mention: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all {@link Birthday} from the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     */
    private void getAllBirthdays() {
        try {
            birthdays = Bot.getCafeAPI().BIRTHDAY.getAllBirthdays();
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Retrieving Birthdays: " + e.getMessage(), e);
        }
    }

    /**
     * Removes a {@link Birthday} from the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param userID The {@link String userID} of the {@link Birthday} to remove.
     * @return True, if the {@link Birthday} was removed successfully.
     */
    @NotNull
    public Boolean removeBirthday(@NotNull String userID) {
        try {
            Bot.getCafeAPI().BIRTHDAY.removeUserBirthday(userID);
            birthdays.remove(userID);
            return true;
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Removing Birthday: " + e.getMessage(), e);
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
            Bot.getCafeAPI().BIRTHDAY.updateUserBirthday(userID, month, day);
            birthdays.put(userID, new Birthday(month, day, false));
            updateMentionedBirthday(userID, false);
            return true;
        } catch (NotFoundException e) {
            try {
                Bot.getCafeAPI().BIRTHDAY.createUserBirthday(userID, month, day);
                birthdays.put(userID, new Birthday(month, day, false));
                updateMentionedBirthday(userID, false);
                return true;
            } catch (CafeException e2) {
                Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Creating Birthday: " + e2.getMessage(), e2);
                return false;
            }
        }catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Updating Birthday: " + e.getMessage(), e);
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
            return Bot.getCafeAPI().BIRTHDAY.getUserBirthday(userID);
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Retrieving User Birthday: " + e.getMessage(), e);
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