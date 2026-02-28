package com.beanbeanjuice.cafebot.utility.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ResourceBundle;

@Getter
@RequiredArgsConstructor
public class CommandContext {

    private final ResourceBundle guildI18n;
    private final ResourceBundle userI18n;

}
