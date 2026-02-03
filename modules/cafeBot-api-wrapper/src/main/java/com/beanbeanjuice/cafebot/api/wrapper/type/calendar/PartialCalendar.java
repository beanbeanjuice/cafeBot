package com.beanbeanjuice.cafebot.api.wrapper.type.calendar;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.OwnerType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PartialCalendar {

    private final OwnerType ownerType;
    private final String ownerId;

    private final String name;
    private final String url;

}
