package com.beanbeanjuice.utility.sections.cafe;

import org.jetbrains.annotations.NotNull;

/**
 * The {@link CafeCategory} enum.
 *
 * @author beanbeanjuice
 */
public enum CafeCategory {

    BREAKFAST("Breakfast", "Some food you can get to start up your day!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/breakfast.png"),
    DRINK("Drinks", "Non-alcoholic drinks of your choice!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/drink.jpg"),
    SANDWICH("Sandwiches and Burgers", "I mean... this is pretty self-explanatory.", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/sandwich.jpg"),
    SOUP("Soups", "Some warm yummy soup for your tummy!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/soup.jpg"),
    SIDE("Sides and Condiments", "Things to go along with your meal!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/side.jpg"),
    FRUIT("Fruits", "Some healthy snacks to go along with your coffee!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/fruit.jpg"),
    SWEET("Bread and Sweets", "Something sweet to satisfy that sweet tooth of yours!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/sweet.jpg"),
    ALCOHOL("\"Magical Water\"", "Really? I'm gonna need to see some ID.", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/alcohol.jpg"),
    SECRET("Secret Menu", "If you found this, then congratulations!", "http://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/secret.jpeg");

    private final String title;
    private final String description;
    private final String imageURL;

    /**
     * Creates a new {@link CafeCategory} static object.
     * @param title The title of the {@link CafeCategory}.
     * @param description The description of the {@link CafeCategory}.
     * @param imageURL The image for the {@link CafeCategory}.
     */
    CafeCategory(@NotNull String title, @NotNull String description, @NotNull String imageURL) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
    }

    /**
     * @return The title of the {@link CafeCategory}.
     */
    @NotNull
    public String getTitle() {
        return title;
    }

    /**
     * @return The description of the {@link CafeCategory}.
     */
    @NotNull
    public String getDescription() {
        return description;
    }

    /**
     * @return The image for the {@link CafeCategory}.
     */
    @NotNull
    public String getImageURL() {
        return imageURL;
    }

}
