package com.beanbeanjuice.cafebot.api.wrapper.type.poll;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public class PartialPoll {

    private final String title;
    private final String description;
    private final boolean allowMultiple;
    private final Instant endsAt;
    private final PartialPollOption[] options;

    public PartialPoll(String title, @Nullable String description, boolean allowMultiple, String endsAt, PartialPollOption[] options) {
        this.title = title;
        this.description = description;
        this.allowMultiple = allowMultiple;
        this.endsAt = Instant.parse(endsAt);
        this.options = options;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(this.description);
    }

    public List<String> getExistingEmojis() {
        List<String> emojis = new ArrayList<>();
        Arrays.stream(options).map(PartialPollOption::getEmoji).forEach((emojiOptional) -> emojiOptional.ifPresent(emojis::add));
        return emojis;
    }

}
