package com.beanbeanjuice.utility.cafe;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Random;

public class ServeHandler {

    private final Integer TIP_MINIMUM = 5;
    private final Integer TIP_MAXIMUM = 20;
    private final Double TIP_MULTIPLIER = 1.125;
    private final Double TIP_DIVIDER = 0.85;
    private final Integer LENGTH_UNTIL_MULTIPLIER = 3;
    private final Integer USAGE_AMOUNT_DIVIDE = 500;
    private final Integer MINUTES_UNTIL_CAN_SERVE = 60;

    /**
     * Gets the dictionary word from the SQL database.
     * @param word The word to check for.
     * @return The word converted to a {@link ServeWord}.
     */
    @Nullable
    public ServeWord getWord(@NotNull String word) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM beanbot.serve_words WHERE word = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setString(1, word);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            return new ServeWord(resultSet.getString(1), resultSet.getInt(2));
        } catch (SQLException e) {
            return null;
        }

    }

    @NotNull
    public Boolean updateWord(@NotNull ServeWord serveWord) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.serve_words SET uses = (?) WHERE word = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);

            BeanBot.getLogManager().log(this.getClass(), LogLevel.DEBUG, ("Uses: " + (serveWord.getUses()+1)));

            statement.setInt(1, serveWord.getUses()+1);
            statement.setString(2, serveWord.getWord());

            statement.execute();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Update Cafe Word: " + e.getMessage());
            return false;
        }

    }

    @NotNull
    public Double roundDouble(@NotNull Double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    @Nullable
    public CafeCustomer getCafeCustomer(@NotNull String userID) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM beanbot.cafe_information WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(userID));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            // Already Have userID.
            Double beanCountAmount = resultSet.getDouble(2);
            Timestamp lastServingTime = resultSet.getTimestamp(3);

            return new CafeCustomer(userID, beanCountAmount, lastServingTime);
        } catch (SQLException e) {

            arguments = "INSERT INTO beanbot.cafe_information (user_id) VALUES (?);";

            try {

                PreparedStatement catchStatement = connection.prepareStatement(arguments);
                catchStatement.setLong(1, Long.parseLong(userID));
                catchStatement.execute();

                return new CafeCustomer(userID, 0.0, new Timestamp(System.currentTimeMillis()));

            } catch (SQLException catchE) {
                return null;
            }
        }

    }

    @Nullable
    public CafeCustomer getCafeCustomer(@NotNull User user) {
        return getCafeCustomer(user.getId());
    }

    /**
     * Calculate the Tip for the {@link ServeWord}.
     * @param serveWord The {@link ServeWord} to check for.
     * @return The amount of tip to receive.
     */
    public Double calculateTip(@NotNull ServeWord serveWord) {
        int length = serveWord.getWord().length();
        int uses = serveWord.getUses();

        // Randomly Between $5-$20
        Random random = new Random();
        double tip = TIP_MINIMUM + (TIP_MAXIMUM - TIP_MINIMUM) * random.nextDouble();
        double addedTip = 0.0;

        // If the word length is above 3, then x1.125 per letter.
        if (length > LENGTH_UNTIL_MULTIPLIER) {
            for (int i = 0; i < (length - LENGTH_UNTIL_MULTIPLIER); i++) {
                addedTip += ((tip + addedTip) * TIP_MULTIPLIER) - (tip + addedTip);
            }

            // Get the uses divided by 10 and however many times that is, do the added tip x0.85.
            for (int i = 0; i < uses / USAGE_AMOUNT_DIVIDE; i++) {
                addedTip *= TIP_DIVIDER;
            }
        }
        return tip + addedTip;
    }

    @NotNull
    public Integer minutesBetween(@NotNull CafeCustomer cafeCustomer) {

        if (cafeCustomer.getLastServingTime() == null) {
            return MINUTES_UNTIL_CAN_SERVE + 10;
        }

        try {
            return Math.round(compareTwoTimeStamps(cafeCustomer.getLastServingTime(), new Timestamp(System.currentTimeMillis())));
        } catch (UnsupportedTemporalTypeException e) {
            return MINUTES_UNTIL_CAN_SERVE + 10;
        }
    }

    public static long compareTwoTimeStamps(Timestamp oldTime, Timestamp currentTime) {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();

        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffMinutes;
    }

    @NotNull
    public Boolean canServe(@NotNull CafeCustomer cafeCustomer) {
        return minutesBetween(cafeCustomer) >= MINUTES_UNTIL_CAN_SERVE;
    }

    @NotNull
    public Boolean updateTip(@NotNull CafeCustomer cafeCustomer, @NotNull Timestamp currentDate, @NotNull Double tipToAdd) {

        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.cafe_information SET bean_coins = (?) AND last_serving_time = (?) WHERE user_id = (?);";

        double newTip = cafeCustomer.getBeanCoinAmount() + tipToAdd;

        System.out.println("Cafe Customer Timer: " + cafeCustomer.getLastServingTime());
        System.out.println("Cafe Customer Timer: " + currentDate.toString());

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setDouble(1, newTip);
            statement.setTimestamp(2, currentDate);
            statement.setLong(3, Long.parseLong(cafeCustomer.getUserID()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Tip: " + e.getMessage());
            return false;
        }

    }

    @NotNull
    public Integer getMinutesUntilCanServe() {
        return MINUTES_UNTIL_CAN_SERVE;
    }



}
