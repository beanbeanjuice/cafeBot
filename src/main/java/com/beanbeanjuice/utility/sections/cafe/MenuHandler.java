package com.beanbeanjuice.utility.sections.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.sections.cafe.object.CafeCustomer;
import com.beanbeanjuice.utility.sections.cafe.object.MenuItem;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used for handling the menu.
 *
 * @author beanbeanjuice
 */
public class MenuHandler {

    private HashMap<CafeCategory, ArrayList<MenuItem>> menu;

    public MenuHandler() {
        menu = new HashMap<>();
        createMenuItems();
    }

    private void createMenuItems() {
        // Drinks
        ArrayList<MenuItem> drinks = new ArrayList<>();
        drinks.add(new MenuItem(CafeCategory.DRINK, "Tea", 3.0, "A warm cup of tea for your cozy day!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/tea.webp"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Coffee", 3.0, "A regular coffee. Would you like some sugar with that?", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/coffee.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Mocha", 3.5, "A grande mocha with whip cream. I hear it's the owner's favourite!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/mocha.webp"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Macchiato", 3.25, "What even is this...", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/macchiato.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Cappuccino", 3.15, "I mean... I would just get a mocha.", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/cappuccino.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Milk", 2.0, "Some regular milk. This would go along great with a donut!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/milk.webp"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Chocolate Milk", 2.0, "Ooo!~ Feeling daring today are we?", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/chocolate_milk.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Bottled Water", 1.0, "A normal bottled water to quench your thirst!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/bottled_water.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Strawberry Banana Smoothie", 2.5, "A sweet and thick drink for the day!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/strawberry_banana_smoothie.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Mango Smoothie", 2.5, "A tangy drink to help wake you up!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/mango_smoothie.webp"));
        menu.put(CafeCategory.DRINK, drinks);

        // Sandwiches and Burgers
        ArrayList<MenuItem> sandwiches = new ArrayList<>();
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Ham Sandwich", 4.25, "A ham sandwich with cheese!", ""));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Turkey Sandwich", 4.25, "A turkey sandwich with cheese!", ""));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Chicken Burger", 5.25, "A chicken burger! For if you don't want a ham burger I guess...", ""));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Ham Burger", 5.5, "A regular ham burger! Very juicy, very yummy.", ""));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Cheese Burger", 5.75, "A cheese burger! This is immaculate...", ""));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Grilled Cheese", 3.5, "Perfect for a quick bite!", ""));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "BLT Sammy", 4.25, "The trifecta of sandwiches!", ""));
        menu.put(CafeCategory.SANDWICH, sandwiches);

        // Soup
        ArrayList<MenuItem> soups = new ArrayList<>();
        soups.add(new MenuItem(CafeCategory.SOUP, "Chicken Noodle Soup", 3.25, "Chicken noodle soup to get you over a cold!", ""));
        soups.add(new MenuItem(CafeCategory.SOUP, "Tomato Soup", 3.25, "This would go GREAT with a grilled cheese!", ""));
        soups.add(new MenuItem(CafeCategory.SOUP, "Loaded Baked Potato Soup", 3.25, "This soup is absolutely amazing!", ""));
        soups.add(new MenuItem(CafeCategory.SOUP, "Mushroom Soup", 3.25, "I've personally never had this...", ""));
        soups.add(new MenuItem(CafeCategory.SOUP, "Lobster Bisque", 3.25, "Yummy yummy in my tummy!", ""));
        soups.add(new MenuItem(CafeCategory.SOUP, "Clam Chowder", 3.25, "This is... honestly really good.", ""));
        menu.put(CafeCategory.SOUP, soups);

        // Sides and Condiments
        ArrayList<MenuItem> sides = new ArrayList<>();
        sides.add(new MenuItem(CafeCategory.SIDES, "Fries", 0.5, "Some fries to go along with your order!", ""));
        sides.add(new MenuItem(CafeCategory.SIDES, "Cheese Sticks", 0.5, "Some cheese sticks! You can pull this into string cheese!", ""));
        sides.add(new MenuItem(CafeCategory.SIDES, "Mozzarella Sticks", 0.5, "Mmmm... this is *my* personal favourite!", ""));
        sides.add(new MenuItem(CafeCategory.SIDES, "Ketchup", 0.1, "For your fries!", ""));
        sides.add(new MenuItem(CafeCategory.SIDES, "Cream", 0.5, "Extra cream for your coffee. Very... creamy...", "https://i.pinimg.com/736x/f6/c3/81/f6c3819b8c55b2e31276b0fec659559e.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDES, "Sugar", 0.5, "Sugar cubes for the ones who are too weak for black coffee.", "https://tasteofenglishtea.files.wordpress.com/2011/10/sugar-cubes.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDES, "Saltines", 0.1, "Some crackers to put in your soup!", ""));
        menu.put(CafeCategory.SIDES, sides);

        // Fruits
        ArrayList<MenuItem> fruits = new ArrayList<>();
        fruits.add(new MenuItem(CafeCategory.FRUITS, "Banana", 0.25, "A small banana for your potassium!", ""));
        fruits.add(new MenuItem(CafeCategory.FRUITS, "Apple", 0.25, "An apple a day keeps the doctors away!", ""));
        fruits.add(new MenuItem(CafeCategory.FRUITS, "Apple Sauce", 0.5, "Smooth apple... mmmmmmmm", ""));
        fruits.add(new MenuItem(CafeCategory.FRUITS, "Strawberry", 0.25, "A small bowl of strawberries!", ""));
        fruits.add(new MenuItem(CafeCategory.FRUITS, "Blueberry", 0.25, "A small bowl of blueberries!", ""));
        fruits.add(new MenuItem(CafeCategory.FRUITS, "Mango", 0.25, "A sweet and tangy mango!", ""));
        menu.put(CafeCategory.FRUITS, fruits);

        // Bread and Sweets
        ArrayList<MenuItem> sweets = new ArrayList<>();
        sweets.add(new MenuItem(CafeCategory.SWEETS, "Scone", 2.0, "A soft scone. Possible pair with a coffee!", "https://i.pinimg.com/564x/fb/90/11/fb9011044679899c05c717009f41ebc9--carry-on-aesthetic-simon-snow-aesthetic.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEETS, "Croissant", 4.25, "A buttery croissant. Pair with a mocha... or maybe a friend?", "https://i.pinimg.com/474x/f9/0e/7a/f90e7abb9383dbf0073e5fc8c9d87c49.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEETS, "Donut", 2.5, "A regular glazed donut. This might be good with some regular milk. Want some?", "https://wallpaperaccess.com/full/1179350.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEETS, "Muffin", 1.75, "Blueberry or chocolate. Take your pick!", "https://www.thegreatcoursesdaily.com/wp-content/uploads/2017/04/ThinkstockPhotos-539085530-678x381.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEETS, "Cookie", 1.5, "A chocolate chip cookie! I wouldn't want it any other way.", "https://i.pinimg.com/originals/be/60/2f/be602f901b6d09f7d1afc5274de36e53.png"));
        sweets.add(new MenuItem(CafeCategory.SWEETS, "Banana Bread", 3.0, "[Want some bread?](https://www.youtube.com/watch?v=AQHnPBz3Kx4)", "https://cdn.dribbble.com/users/1981371/screenshots/6547358/bananabread.gif"));
        sweets.add(new MenuItem(CafeCategory.SWEETS, "Cinnamon Bun", 2.25, "A sweet cinnamon bun! It has some pecans on it! ^-^", ""));
        sweets.add(new MenuItem(CafeCategory.SWEETS, "Vanilla Ice Cream", 0.75, "A scoop of vanilla ice cream. I'm sorry... we ran out of other flavours...", ""));
        menu.put(CafeCategory.SWEETS, sweets);

        // Alcohol
        ArrayList<MenuItem> alcohol = new ArrayList<>();
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Rum and Coke", 7.0, "Something not too light but not too heavy!", ""));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Draft Beer", 7.0, "The yummy version of beer.", ""));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Bottled Beer", 7.0, "The less yummy version of beer.", ""));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Wine", 7.0, "Something to go along with your sandwich I guess...", ""));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Sake", 7.0, "A Japanese alcoholic drink! ||~~Weeb...~~||", ""));
        menu.put(CafeCategory.ALCOHOL, alcohol);

        // Secret
        ArrayList<MenuItem> secrets = new ArrayList<>();
        secrets.add(new MenuItem(CafeCategory.SECRET, "The \"One Tap\"", 10.0, "A 69oz cup of Swedish fish GFUEL.", ""));
        secrets.add(new MenuItem(CafeCategory.SECRET, "Otter Pops", 4.0, "A nice cold otter pop to get you through a hot day. They're Lilly Baby's favourite!", ""));
        secrets.add(new MenuItem(CafeCategory.SECRET, "The Duo", 20.0, "A giant bucket of popcorn and extra cheddar goldfish mixed together. Enough for two! A signature dish by Lilly and Will. ❤", ""));
        secrets.add(new MenuItem(CafeCategory.SECRET, "The Kenzie Special", 7.0, "An entire bottle of vodka and some apple juice...", ""));
        menu.put(CafeCategory.SECRET, secrets);
    }

    @Nullable
    public MenuItem getItem(@NotNull CafeCategory category, @NotNull Integer itemNumber) {
        try {
            return menu.get(category).get(itemNumber);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @NotNull
    public ArrayList<MenuItem> getMenu(@NotNull CafeCategory category) {
        return menu.get(category);
    }

    @NotNull
    public Boolean updateOrderer(@NotNull CafeCustomer cafeCustomer, @NotNull Double cost) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.cafe_information SET bean_coins = (?), orders_bought = (?) WHERE user_id = (?);";

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
        String arguments = "UPDATE cafeBot.cafe_information SET orders_received = (?) WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setInt(1, cafeCustomer.getOrdersReceived() + 1);
            statement.setLong(2, Long.parseLong(cafeCustomer.getUserID()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Receiver: " + e.getMessage(), e);
            return false;
        }
    }

}
