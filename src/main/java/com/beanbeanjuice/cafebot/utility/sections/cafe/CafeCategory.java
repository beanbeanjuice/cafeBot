package com.beanbeanjuice.cafebot.utility.sections.cafe;

import lombok.Getter;

public enum CafeCategory {

    BREAKFAST("Breakfast", "Some food you can get to start up your day!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/breakfast.png"),
    DRINK("Drinks", "Non-alcoholic drinks of your choice!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/drink.jpg"),
    SANDWICH("Sandwiches and Burgers", "I mean... this is pretty self-explanatory.", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/sandwich.jpg"),
    SOUP("Soups", "Some warm yummy soup for your tummy!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/soup.jpg"),
    SIDE("Sides and Condiments", "Things to go along with your meal!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/side.jpg"),
    FRUIT("Fruits", "Some healthy snacks to go along with your coffee!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/fruit.jpg"),
    SWEET("Bread and Sweets", "Something sweet to satisfy that sweet tooth of yours!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/sweet.jpg"),
    ALCOHOL("\"Magical Water\"", "Really? I'm gonna need to see some ID.", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/alcohol.jpg"),
    SECRET("Secret Menu", "If you found this, then congratulations!", "https://cdn.beanbeanjuice.com/images/cafeBot/cafe_category/secret.jpeg");

    @Getter private final String title;
    @Getter private final String description;
    @Getter private final String imageURL;

    CafeCategory(final String title, final String description, final String imageURL) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
    }

}
