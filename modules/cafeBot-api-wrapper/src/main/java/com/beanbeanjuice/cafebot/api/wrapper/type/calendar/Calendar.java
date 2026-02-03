package com.beanbeanjuice.cafebot.api.wrapper.type.calendar;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.OwnerType;
import lombok.Getter;

@Getter
public class Calendar extends PartialCalendar {

    private final String id;

    public Calendar(String id, OwnerType ownerType, String ownerId, String name, String url) {
        super(ownerType, ownerId, name, url);

        this.id = id;
    }

}
