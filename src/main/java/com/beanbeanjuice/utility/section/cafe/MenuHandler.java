package com.beanbeanjuice.utility.section.cafe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used for handling the menu.
 *
 * @author beanbeanjuice
 */
public class MenuHandler {

    private static HashMap<CafeCategory, ArrayList<MenuItem>> menu = new HashMap<>();

    /**
     * Creates a new {@link MenuHandler} object.
     */
    public MenuHandler() {
        menu = new HashMap<>();
        createMenuItems();
    }

    /**
     * Creates the {@link MenuItem}.
     */
    private static void createMenuItems() {

        // Breakfasts
        ArrayList<MenuItem> breakfasts = new ArrayList<>();
        breakfasts.add(new MenuItem(CafeCategory.BREAKFAST, "Normal Pancakes", 7.25, "Normal pancakes with a side of 2 eggs and 2 pieces of bacon!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/breakfasts/pancakes.jpg"));
        breakfasts.add(new MenuItem(CafeCategory.BREAKFAST, "Chocolate Chip Pancakes", 7.5, "Chocolate chip pancakes with a side of 2 eggs and 2 pieces of bacon!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/breakfasts/chocolate_chip_pancakes.jpg"));
        breakfasts.add(new MenuItem(CafeCategory.BREAKFAST, "Everything Omelette", 8.25, "An everything omelette! This has eggs, bacon, cheese, and so much more!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/breakfasts/omelette.jpg"));
        breakfasts.add(new MenuItem(CafeCategory.BREAKFAST, "French Toast", 4.75, "Some french toast with maple syrup!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/breakfasts/french_toast.webp"));
        breakfasts.add(new MenuItem(CafeCategory.BREAKFAST, "Breakfast Sandwich", 5.5, "Bacon, egg, and cheese sandwich!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/breakfasts/bacon_egg_and_cheese_sandwich.jpg"));
        menu.put(CafeCategory.BREAKFAST, breakfasts);

        // Drinks
        ArrayList<MenuItem> drinks = new ArrayList<>();
        drinks.add(new MenuItem(CafeCategory.DRINK, "Tea", 3.0, "A warm cup of tea for your cozy day!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/tea.webp"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Coffee", 3.0, "A regular coffee. Would you like some sugar with that?", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/coffee.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Mocha", 3.5, "A grande mocha with whip cream. I hear it's the owner's favourite!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/mocha.webp"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Macchiato", 3.25, "What even is this...", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/macchiato.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Cappuccino", 3.15, "I mean... I would just get a mocha.", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/cappuccino.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Milk", 2.0, "Some regular milk. This would go along great with a donut!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/milk.webp"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Chocolate Milk", 2.0, "Ooo!~ Feeling daring today are we?", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/chocolate_milk.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Bottled Water", 1.0, "A normal bottled water to quench your thirst!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/bottled_water.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Strawberry Banana Smoothie", 2.5, "A sweet and thick drink for the day!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/strawberry_banana_smoothie.jpg"));
        drinks.add(new MenuItem(CafeCategory.DRINK, "Mango Smoothie", 2.5, "A tangy drink to help wake you up!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/drinks/mango_smoothie.webp"));
        menu.put(CafeCategory.DRINK, drinks);

        // Sandwiches and Burgers
        ArrayList<MenuItem> sandwiches = new ArrayList<>();
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Ham Sandwich", 4.25, "A ham sandwich with cheese!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/ham_sandwich.jpg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Turkey Sandwich", 4.25, "A turkey sandwich with cheese!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/turkey_sandwich.jpeg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Chicken Burger", 5.25, "A chicken burger! For if you don't want a ham burger I guess...", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/chicken_burger.jpg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Ham Burger", 5.5, "A regular ham burger! Very juicy, very yummy.", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/ham_burger.jpg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Cheese Burger", 5.75, "A cheese burger! This is immaculate...", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/cheese_burger.jpg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "Grilled Cheese", 3.5, "Perfect for a quick bite!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/grilled_cheese.jpg"));
        sandwiches.add(new MenuItem(CafeCategory.SANDWICH, "BLT Sammy", 4.25, "The trifecta of sandwiches!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sandwiches/blt_sandwich.jpg"));
        menu.put(CafeCategory.SANDWICH, sandwiches);

        // Soup
        ArrayList<MenuItem> soups = new ArrayList<>();
        soups.add(new MenuItem(CafeCategory.SOUP, "Chicken Noodle Soup", 3.25, "Chicken noodle soup to get you over a cold!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/chicken_noodle_soup.jpg"));
        soups.add(new MenuItem(CafeCategory.SOUP, "Tomato Soup", 3.25, "This would go GREAT with a grilled cheese!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/tomato_soup.jpg"));
        soups.add(new MenuItem(CafeCategory.SOUP, "Loaded Baked Potato Soup", 3.25, "This soup is absolutely amazing!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/loaded_baked_potato_soup.jpg"));
        soups.add(new MenuItem(CafeCategory.SOUP, "Mushroom Soup", 3.25, "I've personally never had this...", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/mushroom_soup.jpg"));
        soups.add(new MenuItem(CafeCategory.SOUP, "Lobster Bisque", 3.25, "Yummy yummy in my tummy!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/lobster_bisque.jpg"));
        soups.add(new MenuItem(CafeCategory.SOUP, "Clam Chowder", 3.25, "This is... honestly really good.", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/soups/clam_chowder_soup.jpg"));
        menu.put(CafeCategory.SOUP, soups);

        // Sides and Condiments
        ArrayList<MenuItem> sides = new ArrayList<>();
        sides.add(new MenuItem(CafeCategory.SIDE, "Fries", 0.5, "Some fries to go along with your order!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/fries.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Cheese Sticks", 0.5, "Some cheese sticks! You can pull this into string cheese!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/cheese_sticks.webp"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Mozzarella Sticks", 0.5, "Mmmm... this is *my* personal favourite!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/mozarella_sticks.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Ketchup", 0.1, "For your fries!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/ketchup.webp"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Cream", 0.5, "Extra cream for your coffee. Very... creamy...", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/cream.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Sugar", 0.5, "Sugar cubes for the ones who are too weak for black coffee.", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/sugar.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Saltines", 0.1, "Some crackers to put in your soup!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/saltines.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Popcorn Chicken", 1.5, "MMmmmm these are my favourite...", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/popcorn_chicken.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Egg Puffs", 1.5, "Some cheese egg puffs! These might go well with some coffee! *wink*", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/egg_puffs.jpg"));
        sides.add(new MenuItem(CafeCategory.SIDE, "Chicken Nuggets", 1.5, "Chicken nuggets with sweet and sour sauce. As it should be!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sides/chicken_nuggets.jpg"));
        menu.put(CafeCategory.SIDE, sides);

        // Fruits
        ArrayList<MenuItem> fruits = new ArrayList<>();
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Banana", 0.25, "A small banana for your potassium!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/banana.jpg"));
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Apple", 0.25, "An apple a day keeps the doctors away!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/apple.jpg"));
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Apple Sauce", 0.5, "Smooth apple... mmmmmmmm", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/apple_sauce.jpg"));
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Strawberry", 0.25, "A small bowl of strawberries!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/strawberry.jpg"));
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Blueberry", 0.25, "A small bowl of blueberries!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/blueberry.jpg"));
        fruits.add(new MenuItem(CafeCategory.FRUIT, "Mango", 0.25, "A sweet and tangy mango!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/fruits/mango.jpeg"));
        menu.put(CafeCategory.FRUIT, fruits);

        // Bread and Sweets
        ArrayList<MenuItem> sweets = new ArrayList<>();
        sweets.add(new MenuItem(CafeCategory.SWEET, "Scone", 2.0, "A soft scone. Possible pair with a coffee!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/scone.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Croissant", 4.25, "A buttery croissant. Pair with a mocha... or maybe a friend?", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/croissant.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Donut", 2.5, "A regular glazed donut. This might be good with some regular milk. Want some?", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/donut.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Chocolate Muffin", 1.75, "These are very yummy and the perfect size for your mouth!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/muffin.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Chocolate Chip Cookie", 1.5, "A chocolate chip cookie! I wouldn't want it any other way.", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/chocolate_chip_cookie.png"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Banana Bread", 3.0, "[Want some bread?](https://www.youtube.com/watch?v=AQHnPBz3Kx4)", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/banana_bread.gif"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Cinnamon Bun", 2.25, "A sweet cinnamon bun! It has some pecans on it! ^-^", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/cinnamon_bun.jpeg"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Vanilla Ice Cream", 0.75, "A scoop of vanilla ice cream. I'm sorry... we ran out of other flavours...", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/vanilla_ice_cream.gif"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Ice Cream Waffles", 3.5, "A nice warm waffle with some nice cold vanilla ice cream!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/ice_cream_waffles.jpg"));
        sweets.add(new MenuItem(CafeCategory.SWEET, "Cheese Cake", 2.5, "A nice warm slice of cheese cake!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/sweets/cheese_cake.jpg"));
        menu.put(CafeCategory.SWEET, sweets);

        // Alcohol
        ArrayList<MenuItem> alcohol = new ArrayList<>();
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Not Rum and Coke", 7.0, "Something not too light but not too heavy!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/alcohol/rum_and_coke.jpg"));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Not Draft Beer", 7.0, "The yummy version of beer.", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/alcohol/draft_beer.jpg"));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Not Bottled Beer", 7.0, "The less yummy version of beer.", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/alcohol/bottled_beer.jpg"));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Not Red Wine", 7.0, "Something to go along with your sandwich I guess...", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/alcohol/red_wine.jpg"));
        alcohol.add(new MenuItem(CafeCategory.ALCOHOL, "Not Sake", 7.0, "A Japanese alcoholic drink! ||~~Weeb...~~||", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/alcohol/japanese_sake.jpg"));
        menu.put(CafeCategory.ALCOHOL, alcohol);

        // Secret
        ArrayList<MenuItem> secrets = new ArrayList<>();
        secrets.add(new MenuItem(CafeCategory.SECRET, "The \"One Tap\"", 10.0, "A 69oz cup of Swedish fish GFUEL.", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/the_one_tap.webp"));
        secrets.add(new MenuItem(CafeCategory.SECRET, "Otter Pop", 4.0, "A nice cold otter pop to get you through a hot day. They're Lilly's favourite!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/otter_pop.jpg"));
        secrets.add(new MenuItem(CafeCategory.SECRET, "The Duo", 20.0, "A giant bucket of popcorn and extra cheddar goldfish mixed together. Enough for two! A signature dish by Lilly and Will. ❤", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/the_duo.webp"));
        secrets.add(new MenuItem(CafeCategory.SECRET, "The Kenzie Special", 7.0, "An entire bottle of *not* vodka and some apple juice...", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/the_kenzie_special.jpg"));
        secrets.add(new MenuItem(CafeCategory.SECRET, "Chicken Coop", 12.5, "Some fluffy nuggets, a choco puff drink for your thirst, and some sunflower seeds!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/chicken_coop.gif"));
        secrets.add(new MenuItem(CafeCategory.SECRET, "Sprout Moment", 3.0, "A yummy, cold aloe drink! Just a little refreshing signature drink from Sprout!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/sprout_moment.jpg"));
        secrets.add(new MenuItem(CafeCategory.SECRET, "Boba GFUEL", 3.0, "A nice yum yum bubble tea in the GFUEL flavour of your choice!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_menu/secrets/boba_gfuel.jpg"));
        menu.put(CafeCategory.SECRET, secrets);
    }

    /**
     * Gets the {@link MenuItem} from the {@link CafeCategory} and item number.
     * @param category The {@link CafeCategory} of the item.
     * @param itemNumber The item number.
     * @return The {@link MenuItem} searched for. Null if not found.
     */
    @Nullable
    public static MenuItem getItem(@NotNull CafeCategory category, @NotNull Integer itemNumber) {
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
    public static ArrayList<MenuItem> getMenu(@NotNull CafeCategory category) {
        return menu.get(category);
    }

}
