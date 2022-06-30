package com.beanbeanjuice.utility.command;

import org.jetbrains.annotations.NotNull;

public enum CommandCategory {

    GENERIC("Generic commands.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/generic.png"),
    CAFE("Commands used for the cafe.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/cafe.jpg"),
    FUN("Commands used for fun.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/fun.jpg"),
    GAMES("Commands used for small games.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/games.png"),
    SOCIAL("Commands used for social media stuff", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/social.gif"),
    INTERACTION("Commands used for user interactions.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/interaction.png"),
    MUSIC("THIS IS EMPTY BECAUSE YOUTUBE IS TAKING DOWN MUSIC BOTS", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/music.webp"),
    TWITCH("Commands used for twitch.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/twitch.jpg"),
    MODERATION("Commands used for moderation.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/moderation.png"),
    EXPERIMENTAL("Commands that are still experimental, but *may* work.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/experimental.gif");

    private final String message;
    private final String link;

    CommandCategory(@NotNull String message, @NotNull String link) {
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
