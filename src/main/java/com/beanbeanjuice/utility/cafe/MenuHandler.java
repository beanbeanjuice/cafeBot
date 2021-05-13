package com.beanbeanjuice.utility.cafe;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.cafe.object.CafeCustomer;
import com.beanbeanjuice.utility.cafe.object.MenuItem;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A class used for handling the menu.
 *
 * @author beanbeanjuice
 */
public class MenuHandler {

    @NotNull
    public MenuItem getItem(@NotNull Integer itemNumber) {
        return getMenu().get(itemNumber);
    }

    @NotNull
    public ArrayList<MenuItem> getMenu() {
        ArrayList<MenuItem> arrayList = new ArrayList<>();
        arrayList.add(new MenuItem("Mocha", 3.5, "A grande mocha with whip cream.", ""));
        arrayList.add(new MenuItem("Coffee", 3.0, "A regular coffee. Would you like some sugar with that?", ""));
        arrayList.add(new MenuItem("Macchiato", 3.25, "What even is this...", ""));
        arrayList.add(new MenuItem("Cappuccino", 3.15, "I mean... I would just get a mocha.", ""));
        arrayList.add(new MenuItem("Milk", 2.0, "Here is some regular milk. You should eat this with a donut or something... weirdo...", ""));
        arrayList.add(new MenuItem("Scone", 2.0, "A soft scone. Possible pair with a coffee!", ""));
        arrayList.add(new MenuItem("Cream", 0.5, "Extra cream for your coffee. Very... creamy...", ""));
        arrayList.add(new MenuItem("Sugar", 0.5, "Sugar cubes for the ones who are too weak for black coffee.", ""));
        arrayList.add(new MenuItem("Croissant", 4.25, "A buttery croissant. Pair with a mocha... or maybe a friend?", ""));
        arrayList.add(new MenuItem("Donut", 2.5, "A regular glazed donut. This might be good with some regular milk. Want some?", ""));
        arrayList.add(new MenuItem("Muffin", 1.75, "Blueberry or chocolate. Take your pick!", ""));
        return arrayList;
    }

    @NotNull
    public Boolean updateOrderer(@NotNull CafeCustomer cafeCustomer, @NotNull Double cost) {
        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.cafe_information SET bean_coins = (?), orders_bought = (?) WHERE user_id = (?);";

        double newTip = cafeCustomer.getBeanCoinAmount() - cost;

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setDouble(1, newTip);
            statement.setInt(2, cafeCustomer.getOrdersBought() + 1);
            statement.setLong(3, Long.parseLong(cafeCustomer.getUserID()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Orderer: " + e.getMessage());
            return false;
        }
    }

    @NotNull
    public Boolean updateReceiver(@NotNull CafeCustomer cafeCustomer) {
        Connection connection = BeanBot.getSQLServer().getConnection();
        String arguments = "UPDATE beanbot.cafe_information SET orders_received = (?) WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setInt(1, cafeCustomer.getOrdersReceived() + 1);
            statement.setLong(2, Long.parseLong(cafeCustomer.getUserID()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            BeanBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Receiver: " + e.getMessage());
            return false;
        }
    }

}
