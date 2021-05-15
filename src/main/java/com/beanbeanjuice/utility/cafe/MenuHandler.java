package com.beanbeanjuice.utility.cafe;

import com.beanbeanjuice.main.CafeBot;
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
        arrayList.add(new MenuItem("Mocha", 3.5, "A grande mocha with whip cream.", "https://i1.wp.com/gatherforbread.com/wp-content/uploads/2014/10/Dark-Chocolate-Mocha-Square.jpg?fit=1000%2C1000&ssl=1"));
        arrayList.add(new MenuItem("Coffee", 3.0, "A regular coffee. Would you like some sugar with that?", "https://i.pinimg.com/564x/62/83/29/628329ae1a813467e988fdf5ed5f383c.jpg"));
        arrayList.add(new MenuItem("Macchiato", 3.25, "What even is this...", "https://blog.thenibble.com/wp-content/uploads/caramel-macchiato-www.starbucks.com_.sg-230.jpg"));
        arrayList.add(new MenuItem("Cappuccino", 3.15, "I mean... I would just get a mocha.", "https://i.pinimg.com/originals/42/fd/d2/42fdd2d6bfc40c4b0063f1ca68afc064.jpg"));
        arrayList.add(new MenuItem("Milk", 2.0, "Here is some regular milk. You should eat this with a donut or something... weirdo...", "https://static.wikia.nocookie.net/aesthetics/images/e/ee/Milk-bottle-pink-white.jpg/revision/latest?cb=20201215221810"));
        arrayList.add(new MenuItem("Chocolate Milk", 2.0, "Really?", "https://i.pinimg.com/originals/25/a4/bb/25a4bb71489fcd714db28c3564f1b789.jpg"));
        arrayList.add(new MenuItem("Cream", 0.5, "Extra cream for your coffee. Very... creamy...", "https://i.pinimg.com/736x/f6/c3/81/f6c3819b8c55b2e31276b0fec659559e.jpg"));
        arrayList.add(new MenuItem("Sugar", 0.5, "Sugar cubes for the ones who are too weak for black coffee.", "https://tasteofenglishtea.files.wordpress.com/2011/10/sugar-cubes.jpg"));
        arrayList.add(new MenuItem("Scone", 2.0, "A soft scone. Possible pair with a coffee!", "https://i.pinimg.com/564x/fb/90/11/fb9011044679899c05c717009f41ebc9--carry-on-aesthetic-simon-snow-aesthetic.jpg"));
        arrayList.add(new MenuItem("Croissant", 4.25, "A buttery croissant. Pair with a mocha... or maybe a friend?", "https://i.pinimg.com/474x/f9/0e/7a/f90e7abb9383dbf0073e5fc8c9d87c49.jpg"));
        arrayList.add(new MenuItem("Donut", 2.5, "A regular glazed donut. This might be good with some regular milk. Want some?", "https://wallpaperaccess.com/full/1179350.jpg"));
        arrayList.add(new MenuItem("Muffin", 1.75, "Blueberry or chocolate. Take your pick!", "https://www.thegreatcoursesdaily.com/wp-content/uploads/2017/04/ThinkstockPhotos-539085530-678x381.jpg"));
        arrayList.add(new MenuItem("Cookie", 1.5, "A chocolate chip cookie! I wouldn't want it any other way.", "https://i.pinimg.com/originals/be/60/2f/be602f901b6d09f7d1afc5274de36e53.png"));
        return arrayList;
    }

    @NotNull
    public Boolean updateOrderer(@NotNull CafeCustomer cafeCustomer, @NotNull Double cost) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafebot.cafe_information SET bean_coins = (?), orders_bought = (?) WHERE user_id = (?);";

        double newTip = cafeCustomer.getBeanCoinAmount() - cost;

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setDouble(1, newTip);
            statement.setInt(2, cafeCustomer.getOrdersBought() + 1);
            statement.setLong(3, Long.parseLong(cafeCustomer.getUserID()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Orderer: " + e.getMessage());
            return false;
        }
    }

    @NotNull
    public Boolean updateReceiver(@NotNull CafeCustomer cafeCustomer) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafebot.cafe_information SET orders_received = (?) WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setInt(1, cafeCustomer.getOrdersReceived() + 1);
            statement.setLong(2, Long.parseLong(cafeCustomer.getUserID()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Receiver: " + e.getMessage());
            return false;
        }
    }

}
