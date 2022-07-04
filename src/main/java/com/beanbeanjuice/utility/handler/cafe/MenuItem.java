package com.beanbeanjuice.utility.handler.cafe;

import org.jetbrains.annotations.NotNull;

/**
 * A class used for {@link MenuItem} for the {@link MenuHandler}.
 *
 * @author beanbeanjuice
 */
public class MenuItem {

    private final CafeCategory category;
    private final String name;
    private final double price;
    private final String description;
    private final String imageURL;

    /**
     * Create a new {@link MenuItem} object.
     * @param category The {@link CafeCategory} for the {@link MenuItem}.
     * @param name The name of the {@link MenuItem}.
     * @param price The {@link Double} price of the {@link MenuItem}.
     * @param description The description of the {@link MenuItem}.
     * @param imageURL The image of the {@link MenuItem}.
     */
    public MenuItem(@NotNull CafeCategory category, @NotNull String name, @NotNull Double price, @NotNull String description, @NotNull String imageURL) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
    }

    /**
     * @return The {@link CafeCategory} of the {@link MenuItem}.
     */
    @NotNull
    public CafeCategory getCategory() {
        return category;
    }

    /**
     * @return The name of the {@link MenuItem}.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @return The price for the {@link MenuItem}.
     */
    @NotNull
    public Double getPrice() {
        return price;
    }

    /**
     * @return The description for the {@link MenuItem}.
     */
    @NotNull
    public String getDescription() {
        return description;
    }

    /**
     * @return The image URL for the {@link MenuItem}.
     */
    @NotNull
    public String getImageURL() {
        return imageURL;
    }

}
