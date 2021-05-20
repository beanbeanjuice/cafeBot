package com.beanbeanjuice.utility.command.usage.categories;

import org.jetbrains.annotations.NotNull;

/**
 * A static class used for handling command category types.
 *
 * @author beanbeanjuice
 */
public enum CategoryType {

    GENERIC("Generic commands.", "http://cdn.beanbeanjuice.com/images/cafeBot/categories/generic.png"),
    CAFE("Commands used for the cafe.", "http://cdn.beanbeanjuice.com/images/cafeBot/categories/cafe.jpg"),
    FUN("Commands used for fun.", "http://cdn.beanbeanjuice.com/images/cafeBot/categories/fun.jpg"),
    INTERACTION("Commands used for user interactions.", "http://cdn.beanbeanjuice.com/images/cafeBot/categories/interaction.png"),
    MUSIC("Commands used for music.", "http://cdn.beanbeanjuice.com/images/cafeBot/categories/music.webp"),
    TWITCH("Commands used for twitch.", "http://cdn.beanbeanjuice.com/images/cafeBot/categories/twitch.jpg"),
    MODERATION("Commands used for moderation.", "http://cdn.beanbeanjuice.com/images/cafeBot/categories/moderation.png");

    private final String message;
    private final String link;

    CategoryType(@NotNull String message, @NotNull String link) {
        this.message = message;
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public String getLink() {
        return link;
    }

}
