package com.beanbeanjuice.cafebot.utility.commands;

import com.beanbeanjuice.cafebot.i18n.I18N;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommandContext {

    private final I18N guildI18n;
    private final I18N userI18n;
    private final boolean isInServer;

    public I18N getDefaultBundle() {
        return (isInServer) ? guildI18n : userI18n;
    }

}
