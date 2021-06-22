package com.beanbeanjuice.utility.command.usage.categories;

import org.jetbrains.annotations.NotNull;

/**
 * A static class used for handling command category types.
 *
 * @author beanbeanjuice
 */
public enum CategoryType {

    GENERIC("Generic commands.", "http://cdn.beanbeanjuice.com/images/cafeBot/category_type/generic.png"),
    CAFE("Commands used for the cafe.", "http://cdn.beanbeanjuice.com/images/cafeBot/category_type/cafe.jpg"),
    FUN("Commands used for fun.", "http://cdn.beanbeanjuice.com/images/cafeBot/category_type/fun.jpg"),
    GAMES("Commands used for small games.", "http://cdn.beanbeanjuice.com/images/cafeBot/category_type/games.png"),
    SOCIAL("Commands used for social media stuff", "http://cdn.beanbeanjuice.com/images/cafeBot/category_type/social.gif"),
    INTERACTION("Commands used for user interactions.", "http://cdn.beanbeanjuice.com/images/cafeBot/category_type/interaction.png"),
    MUSIC("Commands used for music. Currently, check the experimental section.", "http://cdn.beanbeanjuice.com/images/cafeBot/category_type/music.webp"),
    TWITCH("Commands used for twitch.", "http://cdn.beanbeanjuice.com/images/cafeBot/category_type/twitch.jpg"),
    MODERATION("Commands used for moderation.", "http://cdn.beanbeanjuice.com/images/cafeBot/category_type/moderation.png"),
    EXPERIMENTAL("Commands that are still experimental, but work.", "http://cdn.beanbeanjuice.com/images/cafeBot/category_type/experimental.gif");

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
