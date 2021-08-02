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

    private final HashMap<CafeCategory, ArrayList<MenuItem>> menu;

    /**
     * Creates a new {@link MenuHandler} object.
     */
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
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Ham Sandwich", 4.25, "A ham sandwich with cheese!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/ham_sandwich.jpg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Turkey Sandwich", 4.25, "A turkey sandwich with cheese!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/turkey_sandwich.jpeg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Chicken Burger", 5.25, "A chicken burger! For if you don't want a ham burger I guess...", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/chicken_burger.jpg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Ham Burger", 5.5, "A regular ham burger! Very juicy, very yummy.", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/ham_burger.jpg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Cheese Burger", 5.75, "A cheese burger! This is immaculate...", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/cheese_burger.jpg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Grilled Cheese", 3.5, "Perfect for a quick bite!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/grilled_cheese.jpg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "BLT Sammy", 4.25, "The trifecta of sandwiches!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/blt_sandwich.jpg"));
        menu.put(CafeCategory.SANDWICH, sandwiches);

        // Soup
        ArrayList<MenuItem> soups = new ArrayList<>();
        soups.add(new MenuItem(CafeCategory.SOUP, "Chicken Noodle Soup", 3.25, "Chicken noodle soup to get you over a cold!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/chicken_noodle_soup.jpg"));
        soups.add(new MenuItem(CafeCategory.SOUP, "Tomato Soup", 3.25, "This would go GREAT with a grilled cheese!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/tomato_soup.jpg"));
        soups.add(new MenuItem(CafeCategory.SOUP, "Loaded Baked Potato Soup", 3.25, "This soup is absolutely amazing!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/loaded_baked_potato_soup.jpg"));
        soups.add(new MenuItem(CafeCategory.SOUP, "Mushroom Soup", 3.25, "I've personally never had this...", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/mushroom_soup.jpg"));
        soups.add(new MenuItem(CafeCategory.SOUP, "Lobster Bisque", 3.25, "Yummy yummy in my tummy!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/lobster_bisque.jpg"));
        soups.add(new MenuItem(CafeCategory.SOUP, "Clam Chowder", 3.25, "This is... honestly really good.", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/clam_chowder_soup.jpg"));
        menu.put(CafeCategory.SOUP, soups);

        // Sides and Condiments
        ArrayList<MenuItem> sides = new ArrayList<>();
        sides.add(new MenuItem(CafeCategory.SIDE, "Fries", 0.5, "Some fries to go along with your order!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/fries.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Cheese Sticks", 0.5, "Some cheese sticks! You can pull this into string cheese!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/cheese_sticks.webp"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Mozzarella Sticks", 0.5, "Mmmm... this is *my* personal favourite!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/mozarella_sticks.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Ketchup", 0.1, "For your fries!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/ketchup.webp"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Cream", 0.5, "Extra cream for your coffee. Very... creamy...", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/cream.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Sugar", 0.5, "Sugar cubes for the ones who are too weak for black coffee.", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/sugar.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Saltines", 0.1, "Some crackers to put in your soup!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/saltines.jpg"));
        menu.put(CafeCategory.SIDE, sides);

        // Fruits
        ArrayList<MenuItem> fruits = new ArrayList<>();
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Banana", 0.25, "A small banana for your potassium!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/banana.jpg"));
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Apple", 0.25, "An apple a day keeps the doctors away!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/apple.jpg"));
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Apple Sauce", 0.5, "Smooth apple... mmmmmmmm", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/apple_sauce.jpg"));
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Strawberry", 0.25, "A small bowl of strawberries!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/strawberry.jpg"));
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Blueberry", 0.25, "A small bowl of blueberries!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/blueberry.jpg"));
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Mango", 0.25, "A sweet and tangy mango!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/mango.jpeg"));
        menu.put(CafeCategory.FRUIT, fruits);

        // Bread and Sweets
        ArrayList<MenuItem> sweets = new ArrayList<>();
        sweets.add(new MenuItem(CafeCategory.SWEET, "Scone", 2.0, "A soft scone. Possible pair with a coffee!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/scone.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Croissant", 4.25, "A buttery croissant. Pair with a mocha... or maybe a friend?", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/croissant.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Donut", 2.5, "A regular glazed donut. This might be good with some regular milk. Want some?", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/donut.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Chocolate Muffin", 1.75, "These are very yummy and the perfect size for your mouth!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/muffin.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Chocolate Chip Cookie", 1.5, "A chocolate chip cookie! I wouldn't want it any other way.", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/chocolate_chip_cookie.png"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Banana Bread", 3.0, "[Want some bread?](https://www.youtube.com/watch?v=AQHnPBz3Kx4)", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/banana_bread.gif"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Cinnamon Bun", 2.25, "A sweet cinnamon bun! It has some pecans on it! ^-^", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/cinnamon_bun.jpeg"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Vanilla Ice Cream", 0.75, "A scoop of vanilla ice cream. I'm sorry... we ran out of other flavours...", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/vanilla_ice_cream.gif"));
        menu.put(CafeCategory.SWEET, sweets);

        // Alcohol
        ArrayList<MenuItem> alcohol = new ArrayList<>();
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Not Rum and Coke", 7.0, "Something not too light but not too heavy!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/alcohol/rum_and_coke.jpg"));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Not Draft Beer", 7.0, "The yummy version of beer.", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/alcohol/draft_beer.jpg"));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Not Bottled Beer", 7.0, "The less yummy version of beer.", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/alcohol/bottled_beer.jpg"));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Not Red Wine", 7.0, "Something to go along with your sandwich I guess...", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/alcohol/red_wine.jpg"));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Not Sake", 7.0, "A Japanese alcoholic drink! ||~~Weeb...~~||", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/alcohol/japanese_sake.jpg"));
        menu.put(CafeCategory.ALCOHOL, alcohol);

        // Secret
        ArrayList<MenuItem> secrets = new ArrayList<>();
        secrets.add(new MenuItem(CafeCategory.SECRET, "The \"One Tap\"", 10.0, "A 69oz cup of Swedish fish GFUEL.", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/the_one_tap.webp"));
        secrets.add(new MenuItem(CafeCategory.SECRET, "Otter Pop", 4.0, "A nice cold otter pop to get you through a hot day. They're Lilly's favourite!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/otter_pop.jpg"));
        secrets.add(new MenuItem(CafeCategory.SECRET, "The Duo", 20.0, "A giant bucket of popcorn and extra cheddar goldfish mixed together. Enough for two! A signature dish by Lilly and Will. ❤", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/the_duo.webp"));
        secrets.add(new MenuItem(CafeCategory.SECRET, "The Kenzie Special", 7.0, "An entire bottle of *not* vodka and some apple juice...", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/the_kenzie_special.jpg"));
        secrets.add(new MenuItem(CafeCategory.SECRET, "Chicken Coop", 12.5, "Some fluffy nuggets, a choco puff drink for your thirst, and some sunflower seeds!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/chicken_coop.gif"));
        secrets.add(new MenuItem(CafeCategory.SECRET, "Sprout Moment", 3.0, "A yummy, cold aloe drink! Just a little refreshing signature drink from Sprout!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/sprout_moment.jpg"));
        menu.put(CafeCategory.SECRET, secrets);
    }

    /**
     * Gets the {@link MenuItem} from the {@link CafeCategory} and item number.
     * @param category The {@link CafeCategory} of the item.
     * @param itemNumber The item number.
     * @return The {@link MenuItem} searched for. Null if not found.
     */
    @Nullable
    public MenuItem getItem(@NotNull CafeCategory category, @NotNull Integer itemNumber) {
        try {
            return menu.get(category).get(itemNumber);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Gets the {@link ArrayList<MenuItem>} for the {@link CafeCategory}.
     * @param category The {@link CafeCategory} to look in.
     * @return The {@link ArrayList<MenuItem>} for the {@link CafeCategory}.
     */
    @NotNull
    public ArrayList<MenuItem> getMenu(@NotNull CafeCategory category) {
        return menu.get(category);
    }

    /**
     * Updates the {@link CafeCustomer} in the database.
     * @param cafeCustomer The {@link CafeCustomer} to update.
     * @param cost The {@link Double} cost of the order.
     * @return Whether or not the {@link CafeCustomer} was successfully updated.
     */
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

    /**
     * Updates the {@link CafeCustomer} in the database.
     * @param cafeCustomer The {@link CafeCustomer} to update.
     * @return True if the {@link CafeCustomer} was successfully updated.
     */
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
