package com.beanbeanjuice.utility.sections.moderation.welcome;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A custom {@link GuildWelcome} class.
 */
public class GuildWelcome {

    private String description;
    private String thumbnailURL;
    private String imageURL;
    private String message;

    /**
     * Creates a new {@link GuildWelcome}.
     * @param description The description of the {@link GuildWelcome}.
     * @param thumbnailURL The thumbnail URL of the {@link GuildWelcome}.
     * @param imageURL The thumbnail URL of the {@link GuildWelcome}.
     */
    public GuildWelcome(@Nullable String description, @Nullable String thumbnailURL, @Nullable String imageURL, @Nullable String message) {
        this.description = description;
        this.thumbnailURL = thumbnailURL;
        this.imageURL = imageURL;
        this.message = message;
    }

    /**
     * @return The description of the {@link GuildWelcome}.
     */
    @NotNull
    public String getDescription() {
        if (description == null) {
            return "Welcome to the server {user}!";
        }
        return description;
    }

    /**
     * @return The separate message of the {@link GuildWelcome}.
     */
    @Nullable
    public String getMessage() {
        return message;
    }

    /**
     * @return The thumbnail URL of the {@link GuildWelcome}.
     */
    @Nullable
    public String getThumbnailURL() {
        return thumbnailURL;
    }

    /**
     * @return The thumbnail URL of the {@link GuildWelcome}.
     */
    @Nullable
    public String getImageURL() {
        return imageURL;
    }

}
