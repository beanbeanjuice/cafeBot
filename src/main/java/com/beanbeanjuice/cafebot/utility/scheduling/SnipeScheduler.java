package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;

import java.util.concurrent.TimeUnit;

public class SnipeScheduler extends CustomScheduler {

    public SnipeScheduler(CafeBot bot) {
        super(bot, "Snipe-Scheduler");
    }

    @Override
    protected void onStart() {
        bot.getLogger().log(this.getClass(), LogLevel.INFO, "Starting the Snipe Message scheduler...", true, false);

        this.scheduler.scheduleAtFixedRate(() -> {
            try {
                bot.getLogger().log(MutualGuildsScheduler.class, LogLevel.INFO, "Removing old snipes...", false, false);

                bot.getSnipeHandler().removeOldMessages();
            } catch (Exception e) {
                bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Removing Old Snipe Messages: " + e.getMessage(), true, true);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

}
