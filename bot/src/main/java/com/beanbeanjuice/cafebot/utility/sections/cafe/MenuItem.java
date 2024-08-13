package com.beanbeanjuice.cafebot.utility.sections.cafe;

import lombok.Getter;

public class MenuItem {

    @Getter private final CafeCategory category;
    @Getter private final String name;
    @Getter private final double price;
    @Getter private final String description;
    @Getter private final String imageURL;

    public MenuItem(final CafeCategory category, final String name, final double price, final String description, final String imageURL) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
    }

}
