package com.beanbeanjuice.utility.sections.moderation.welcome;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuildWelcome {

    private String description;
    private String thumbnailURL;
    private String imageURL;

    public GuildWelcome(@Nullable String description, @Nullable String thumbnailURL, @Nullable String imageURL) {
        this.description = description;
        this.thumbnailURL = thumbnailURL;
        this.imageURL = imageURL;
    }

    @NotNull
    public String getDescription() {
        if (description == null) {
            return "Welcome to the server {user}!";
        }
        return description;
    }

    @Nullable
    public String getThumbnailURL() {
        return thumbnailURL;
    }

    @Nullable
    public String getImageURL() {
        return imageURL;
    }

}
