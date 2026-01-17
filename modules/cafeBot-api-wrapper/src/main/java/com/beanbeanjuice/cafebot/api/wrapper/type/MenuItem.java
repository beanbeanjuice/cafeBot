package com.beanbeanjuice.cafebot.api.wrapper.type;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.MenuCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuItem {

    private final int id;
    private final MenuCategory category;

    private final String name;
    private final String description;
    private final float price;
    private final String imageUrl;

}
