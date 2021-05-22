package com.beanbeanjuice.utility.sections.fun.birthday;

import com.beanbeanjuice.main.CafeBot;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class used to handle birthdays.
 */
public class BirthdayHandler {

    private HashMap<String, Date> birthdays;
    private Timer birthdayTimer;
    private TimerTask birthdayTimerTask;

    private ArrayList<String> mentionedBirthdays;

    /**
     * Create a new {@link BirthdayHandler}.
     */
    public BirthdayHandler() {
        birthdays = new HashMap<>();
        mentionedBirthdays = new ArrayList<>();
        cacheBirthdays();
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
                        if (!mentionedBirthdays.contains(userID)) {
                            for (Guild guild : CafeBot.getJDA().getGuilds()) {
                                Member member = guild.getMemberById(userID);
                                if (member != null) {

                                    // Making sure the BirthdayChannel is not null.
                                    TextChannel birthdayChannel = CafeBot.getGuildHandler().getCustomGuild(guild).getBirthdayChannel();

                                    if (birthdayChannel != null) {
                                        birthdayChannel.sendMessage(birthdayEmbed(member)).queue();
                                    }

                                }
                            }

                            // PM them a happy birthday.
                            CafeBot.getGeneralHelper().pmUser(CafeBot.getGeneralHelper().getUser(userID), "Hey... we don't know if anyone wished you " +
                                    "a happy birthday, but happy birthday <3!");

                            mentionedBirthdays.add(userID);
                            updateMentionedBirthday(userID, true);
                        }

                    } else {
                        if (mentionedBirthdays.contains(userID)) {
                            mentionedBirthdays.remove(userID);
                            updateMentionedBirthday(userID, false);
                        }
                    }
                });
            }
        };
        birthdayTimer.scheduleAtFixedRate(birthdayTimerTask, 0, 7200000); // Should be 7200000
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
     * Update the mentioned birthday for a specified {@link net.dv8tion.jda.api.entities.User User}.
     * @param userID The ID of the specified {@link net.dv8tion.jda.api.entities.User User}.
     * @param isMentioned Whether or not they have already been mentioned today.
     */
    private void updateMentionedBirthday(@NotNull String userID, @NotNull Boolean isMentioned) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.birthdays SET already_mentioned = (?) WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setBoolean(1, isMentioned);
            statement.setLong(2, Long.parseLong(userID));

            statement.execute();
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Already Mentioned Birthdays: " + e.getMessage());
        }
    }

    /**
     * Update the birthday cache.
     */
    private void cacheBirthdays() {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.birthdays";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(arguments);

            while (resultSet.next()) {
                String userID = String.valueOf(resultSet.getLong(1));
                Date birthday = resultSet.getDate(2);
                birthdays.put(userID, CafeBot.getGeneralHelper().parseDate(birthday.toString()));

                if (resultSet.getBoolean(3)) {
                    mentionedBirthdays.add(userID);
                }
            }
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Getting Birthdays: " + e.getMessage());
        }
    }

    /**
     * Update the birthday {@link Date} for a specified {@link net.dv8tion.jda.api.entities.User User}.
     * @param userID The ID of the {@link net.dv8tion.jda.api.entities.User User}.
     * @param birthday The new {@link Date}.
     * @return Whether or not updating it was successful.
     */
    @NotNull
    public Boolean updateBirthday(@NotNull String userID, @NotNull Date birthday) {
        Connection connection = CafeBot.getSQLServer().getConnection();

        if (!birthdays.containsKey(userID)) {
            String arguments = "INSERT INTO cafeBot.birthdays (user_id, birth_date) VALUES (?,?);";
            try {
                PreparedStatement statement = connection.prepareStatement(arguments);
                statement.setLong(1, Long.parseLong(userID));
                statement.setDate(2, birthday);

                statement.execute();
            } catch (SQLException e) {
                CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Add Birthday: " + e.getMessage());
                return false;
            }
        } else {
            String arguments = "UPDATE cafeBot.birthdays SET birth_date = (?) WHERE user_id = (?);";
            try {
                PreparedStatement statement = connection.prepareStatement(arguments);
                statement.setDate(1, birthday);
                statement.setLong(2, Long.parseLong(userID));

                statement.execute();
            } catch (SQLException e) {
                CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Update Birthday: " + e.getMessage());
                return false;
            }
        }
        birthdays.put(userID, birthday);
        return true;
    }

    /**
     * Get the birthday {@link Date} for a specified {@link net.dv8tion.jda.api.entities.User User}.
     * @param userID The ID of the {@link net.dv8tion.jda.api.entities.User User}.
     * @return The birthday {@link Date} of the {@link net.dv8tion.jda.api.entities.User User}.
     */
    @Nullable
    public Date getBirthday(@NotNull String userID) {
        if (birthdays.containsKey(userID)) {
            return birthdays.get(userID);
        }
        return null;
    }

    /**
     * Check if todays day and month match the birthday day and month.
     * @param checkDate The {@link Date} to check.
     * @return Whether or not it is someone's birthday.
     */
    @NotNull
    public Boolean isBirthday(@NotNull Date checkDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

        LocalDate today = formatter.parseLocalDate(new Date(System.currentTimeMillis()).toString());
        LocalDate birthday = formatter.parseLocalDate(checkDate.toString());

        int todayDay = today.getDayOfMonth();
        int birthdayDay = birthday.getDayOfMonth();
        int todayMonth = today.getMonthOfYear();
        int birthdayMonth = birthday.getMonthOfYear();

        return birthdayMonth == todayMonth && birthdayDay == todayDay;
    }

}
