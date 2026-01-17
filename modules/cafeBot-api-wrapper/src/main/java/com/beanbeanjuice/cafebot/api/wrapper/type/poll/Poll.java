package com.beanbeanjuice.cafebot.api.wrapper.type.poll;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class Poll extends PartialPoll {

    private final int id;
    private final String guildId;
    private final String messageId;
    private final boolean isActive;
    private final PollOption[] options;
    @Getter private final PollOption[] results;

    public Poll(int id, String guildId, String messageId, String title, @Nullable String description, boolean allowMultiple, boolean isActive, String endsAt, PollOption[] options, PollOption[] results) {
        super(title, description, allowMultiple, endsAt, options);
        this.id = id;
        this.guildId = guildId;
        this.messageId = messageId;
        this.isActive = isActive;
        this.results = results;
        this.options = options;
    }

}
