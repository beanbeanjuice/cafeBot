package com.beanbeanjuice.utility.sections.cafe;

import org.jetbrains.annotations.NotNull;

public enum CafeCategory {

    DRINK("Drinks", "Non-alcoholic drinks of your choice!", ""),
    SANDWICH("Sandwiches and Burgers", "I mean... this is pretty self-explanatory.", ""),
    SOUP("Soups", "Some warm yummy soup for your tummy!", ""),
    SIDES("Sides and Condiments", "Things to go along with your meal!", ""),
    FRUITS("Fruits", "Some healthy snacks to go along with your coffee!", ""),
    SWEETS("Bread and Sweets", "Something sweet to satisfy that sweet tooth of yours!", ""),
    ALCOHOL("Alcohol", "Really? I'm gonna need to see some ID.", ""),
    SECRET("Secret Menu", "If you found this, then congratulations!", "");

    private final String title;
    private final String description;
    private final String imageURL;

    CafeCategory(@NotNull String title, @NotNull String description, @NotNull String imageURL) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public String getImageURL() {
        return imageURL;
    }

}
