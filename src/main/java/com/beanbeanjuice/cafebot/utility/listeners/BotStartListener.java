package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jspecify.annotations.NonNull;

public class BotStartListener extends ListenerAdapter {

    private final CafeBot bot;

    public BotStartListener(CafeBot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(@NonNull ReadyEvent event) {
        bot.getLogger().log(this.getClass(), LogLevel.OKAY, "The bot is online!", true, true);
    }

}
