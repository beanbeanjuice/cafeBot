package com.beanbeanjuice.utility.sections.cafe.object;

import com.beanbeanjuice.utility.sections.cafe.CafeCategory;
import org.jetbrains.annotations.NotNull;

public class MenuItem {

    private CafeCategory category;
    private final String name;
    private final double price;
    private final String description;
    private final String imageURL;

    public MenuItem(@NotNull CafeCategory category, @NotNull String name, @NotNull Double price, @NotNull String description, @NotNull String imageURL) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
    }

    @NotNull
    public CafeCategory getCategory() {
        return category;
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
