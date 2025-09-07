package com.beanbeanjuice.cafebot.utility.handlers.polls;

import com.beanbeanjuice.cafebot.api.wrapper.type.poll.PartialPoll;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.PartialPollOption;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PollCreationSessionData {

    private final String guildId;
    private final String userId;

    private String title;
    private @Nullable String description;
    private boolean allowMultiple;
    private final int durationInMinutes;
    private final List<PartialPollOption> options;

    public PollCreationSessionData(String guildId, String userId, int durationInMinutes) {
        this.guildId = guildId;
        this.userId = userId;
        this.durationInMinutes = durationInMinutes;

        options = new ArrayList<>();
    }

    public void addInitialData(String title, @Nullable String description, boolean allowMultiple) {
        this.title = title;
        this.description = description;
        this.allowMultiple = allowMultiple;
    }

    public void addOption(@Nullable String emoji, String title, @Nullable String description) {
        options.add(new PartialPollOption(emoji, title, description));
    }

    public PartialPoll build() {
        Instant endsAt = Instant.now().plus(durationInMinutes, ChronoUnit.MINUTES);
        return new PartialPoll(title, description, allowMultiple, endsAt.toString(), options.toArray(new PartialPollOption[0]));
    }

}
