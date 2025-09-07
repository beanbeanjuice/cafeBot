package com.beanbeanjuice.cafebot.utility.commands;

import lombok.Getter;

public enum CommandCategory {

    CAFE("Looking to order something?", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/cafe.jpg"),
    FUN("Ooo! This looks fun!", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/fun.jpg"),
    GAME("Bored huh?", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/games.png"),
    GENERIC("Very basic...", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/generic.png"),
    INTERACTION("Hugs, waves, slaps, and more!", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/interaction.png"),
    SOCIAL("Hmm... I just need to let it out... you know?", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/social.gif"),
    MODERATION("Commands used for moderation.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/moderation.png"),
    SETTINGS("Commands used for bot settings.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/settings.gif");

    @Getter private final String description;
    @Getter private final String link;

    CommandCategory(final String description, final String link) {
        this.description = description;
        this.link = link;
    }

}
