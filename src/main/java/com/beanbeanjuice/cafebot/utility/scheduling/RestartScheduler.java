package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import java.util.concurrent.TimeUnit;

public class RestartScheduler extends CustomScheduler {

    private static final int GUILDS_PER_SHARD_THRESHOLD = 2000;
    private boolean restartScheduled = false;

    public RestartScheduler(CafeBot bot) {
        super(bot, "Restart-Scheduler");
    }

    @Override
    protected void onStart() {
        bot.getLogger().log(this.getClass(), LogLevel.INFO, "Starting restart scheduler!", true, false);

        this.scheduler.scheduleAtFixedRate(() -> {
            try {
                if (restartScheduled) {
                    runGracefulStop(); // Should be restarted automatically by Docker.
                    return;
                }

                int totalGuilds = bot.getTotalServers();
                int numCurrentShards = bot.getShardCount();

                if (numCurrentShards == 0) return;

                double guildsPerShard = (double) totalGuilds / numCurrentShards;
                int recommendedShards = Math.max(1, (totalGuilds / 1000));

                boolean needsMoreShards = recommendedShards > numCurrentShards;
                boolean exceedsThreshold = guildsPerShard > GUILDS_PER_SHARD_THRESHOLD;

                if (needsMoreShards || exceedsThreshold) {
                    restartScheduled = true;

                    bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Restart imminent (1 hour)! %d shards for %d guilds (%.2f guilds/shard. Recommended %d shards.", numCurrentShards, totalGuilds, guildsPerShard, recommendedShards), true, true);
                    return;
                }

                bot.getLogger().log(this.getClass(), LogLevel.INFO, String.format("No restart needed. %d shards for %d guilds (%.2f guilds/shard. Recommended %d shards.", numCurrentShards, totalGuilds, guildsPerShard, recommendedShards), true, false);
            } catch (Exception e) {
                bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error in Restart Scheduler: " + e.getMessage(), true, true);
            }
        }, 60, 3600, TimeUnit.SECONDS);
    }

    private void runGracefulStop() {
        try {
            bot.getLogger().log(this.getClass(), LogLevel.INFO, "Attempting to stop bot...", true, true);
            Thread.sleep(3000); // Give time for warning log to send
            bot.getShardManager().shutdown();
            Thread.sleep(5000); // Wait for shard manager to stop.
            System.exit(0); // Docker will restart.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error During Restart: " + e.getMessage(), false, false);
            System.exit(1);
        } catch (Exception e) {
            bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error During Restart: " + e.getMessage(), false, false);
            System.exit(1);
        }
    }

}
