package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.api.wrapper.type.MutualGuild;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MutualGuildsScheduler extends CustomScheduler {

    private final List<MutualGuild> additionQueue;
    private final List<MutualGuild> removalQueue;

    public MutualGuildsScheduler(CafeBot bot) {
        super(bot, "Mutual-Guilds-Scheduler");
        this.additionQueue = new Vector<>();
        this.removalQueue = new Vector<>();
    }

    @Override
    protected void onStart() {
        bot.getLogger().log(this.getClass(), LogLevel.INFO, "Starting the Mutual Guilds scheduler...", false, false);

        this.scheduler.scheduleAtFixedRate(() -> {
            try {
                bot.getLogger().log(MutualGuildsScheduler.class, LogLevel.INFO, "Updating mutual guilds...", false, false);

                List<MutualGuild> add;
                List<MutualGuild> remove;

                // Make snapshot + clear atomic
                synchronized (additionQueue) {
                    add = new ArrayList<>(additionQueue);
                    additionQueue.clear();
                }

                synchronized (removalQueue) {
                    remove = new ArrayList<>(removalQueue);
                    removalQueue.clear();
                }

                // Send to API
                bot.getCafeAPI().getMutualGuildsApi().addMutualGuilds(add)
                        .thenRun(
                                () -> bot.getCafeAPI().getMutualGuildsApi().deleteMutualGuilds(remove)
                                        .thenRun(() -> bot.getLogger().log(MutualGuildsScheduler.class, LogLevel.INFO, "Updated mutual guilds!"))
                        );
            } catch (Exception e) {
                bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Updating Mutual Guilds: " + e.getMessage(), true, true);
            }
        }, 1, 1, TimeUnit.HOURS);
    }

    public void addEntry(String userId, String guildId) {
        synchronized (additionQueue) {
            this.additionQueue.add(new MutualGuild(userId, guildId));
        }
    }

    public void removeEntry(String userId, String guildId) {
        synchronized (removalQueue) {
            this.removalQueue.add(new MutualGuild(userId, guildId));
        }
    }

}
