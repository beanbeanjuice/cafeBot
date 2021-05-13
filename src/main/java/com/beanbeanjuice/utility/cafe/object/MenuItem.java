package com.beanbeanjuice.utility.cafe.object;

import org.jetbrains.annotations.NotNull;

public class MenuItem {

    private String name;
    private double price;
    private String description;
    private String imageURL;

    public MenuItem(@NotNull String name, @NotNull Double price, @NotNull String description, @NotNull String imageURL) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Double getPrice() {
        return price;
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
