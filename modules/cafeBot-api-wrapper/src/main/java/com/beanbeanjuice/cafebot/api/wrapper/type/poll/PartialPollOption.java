package com.beanbeanjuice.cafebot.api.wrapper.type.poll;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Getter
public class PartialPollOption {

    private final String emoji;
    private final String title;
    private final String description;

    public PartialPollOption(@Nullable String emoji, String title, @Nullable String description) {
        this.emoji = emoji;
        this.title = title;
        this.description = description;
    }

    public Optional<String> getEmoji() {
        return Optional.ofNullable(this.emoji);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(this.description);
    }

}
