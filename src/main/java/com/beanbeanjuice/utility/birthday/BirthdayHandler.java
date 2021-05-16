package com.beanbeanjuice.utility.birthday;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.HashMap;

public class BirthdayHandler {

    private HashMap<String, Date> birthdays;

    public BirthdayHandler() {
        birthdays = new HashMap<>();
        cacheBirthdays();
    }

    private void cacheBirthdays() {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.birthdays";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(arguments);

            while (resultSet.next()) {
                String userID = String.valueOf(resultSet.getLong(1));
                Date birthday = resultSet.getDate(2);
                birthdays.put(userID, birthday);
            }
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Getting Birthdays: " + e.getMessage());
        }
    }

    @NotNull
    public Boolean updateBirthday(@NotNull String userID, @NotNull Date birthday) {
        Connection connection = CafeBot.getSQLServer().getConnection();

        if (!birthdays.containsKey(userID)) {
            String arguments = "INSERT INTO cafeBot.birthdays (user_id, birthday) VALUES (?,?);";
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
            String arguments = "UPDATE cafeBot.birthdays SET birthday = (?) WHERE user_id = (?);";
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

    @Nullable
    public Date getBirthday(@NotNull String userID) {
        if (birthdays.containsKey(userID)) {
            return birthdays.get(userID);
        }
        return null;
    }

    @NotNull
    public Boolean isBirthday(@NotNull Date birthday) {
        return birthday.equals(new Date(System.currentTimeMillis()));
    }

}
