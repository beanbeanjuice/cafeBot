package com.beanbeanjuice.cafebot.api.wrapper.type.airport;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.AirportMessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AirportMessage extends PartialAirportMessage {

    @Getter private final String guildId;
    @Getter private final AirportMessageType type;

    public AirportMessage(
            String guildId, AirportMessageType type,
            @Nullable String title, @Nullable String author,
            @Nullable String authorUrl, @Nullable String imageUrl,
            @Nullable String thumbnailUrl, @Nullable String description,
            @Nullable String message
    ) {
        super(type, title, author, authorUrl, imageUrl, thumbnailUrl, description, message);
        this.guildId = guildId;
        this.type = type;
    }

}
