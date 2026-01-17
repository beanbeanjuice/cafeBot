package com.beanbeanjuice.cafebot.api.wrapper.type.airport;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.AirportMessageType;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PartialAirportMessage {

    @Getter private final AirportMessageType type;
    private final String title;
    private final String author;
    private final String authorUrl;
    private final String imageUrl;
    private final String thumbnailUrl;
    private final String description;
    private final String message;

    public PartialAirportMessage(
            AirportMessageType type,
            @Nullable String title, @Nullable String author,
            @Nullable String authorUrl, @Nullable String imageUrl,
            @Nullable String thumbnailUrl, @Nullable String description,
            @Nullable String message
    ) {
        this.type = type;
        this.title = title;
        this.author = author;
        this.authorUrl = authorUrl;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.message = message;
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public Optional<String> getAuthor() {
        return Optional.ofNullable(author);
    }

    public Optional<String> getAuthorUrl() {
        return Optional.ofNullable(authorUrl);
    }

    public Optional<String> getImageUrl() {
        return Optional.ofNullable(imageUrl);
    }

    public Optional<String> getThumbnailUrl() {
        return Optional.ofNullable(thumbnailUrl);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

}
