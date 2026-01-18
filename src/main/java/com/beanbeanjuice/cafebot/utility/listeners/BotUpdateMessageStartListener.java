package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.handlers.BotUpdateHandler;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jspecify.annotations.NonNull;

public class BotUpdateMessageStartListener extends ListenerAdapter {

    private final CafeBot bot;
    private BotUpdateHandler updateHandler;

    public BotUpdateMessageStartListener(CafeBot bot) {
        this.bot = bot;

        try {
            this.updateHandler = new BotUpdateHandler(bot);
        } catch (Exception e) {
            this.updateHandler = null;
            bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Instantiating Update Handler: " + e.getMessage(), true, true);
        }
    }

    @Override
    public void onReady(@NonNull ReadyEvent event) {
        updateHandler.sendUpdateNotifications(event.getJDA().getShardInfo().getShardId());
    }

}
