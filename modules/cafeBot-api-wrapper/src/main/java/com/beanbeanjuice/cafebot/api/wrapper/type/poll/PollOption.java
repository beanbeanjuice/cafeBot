package com.beanbeanjuice.cafebot.api.wrapper.type.poll;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

@Getter
public class PollOption extends PartialPollOption {

    private final int id;
    private final int pollId;
    private final String[] voters;

    /**
     * Used only for creating new polls.
     * @param emoji The {@link String emoji} to use for the option.
     * @param title The {@link String title} of the poll option.
     * @param description An {@link Optional} {@link String description} for the poll option.
     */
    public PollOption(int id, int pollId, String emoji, String title, @Nullable String description, String[] voters) {
        super(Objects.requireNonNull(emoji, "emoji must not be null"), title, description);
        // Creating a new poll
        this.id = id;
        this.pollId = pollId;
        this.voters = voters;
    }

}
