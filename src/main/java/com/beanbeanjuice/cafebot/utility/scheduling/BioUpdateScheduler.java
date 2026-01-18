package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.Activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class BioUpdateScheduler extends CustomScheduler {

    public BioUpdateScheduler(CafeBot bot) {
        super(bot, "Bio-Scheduler");
    }

    private final List<Supplier<String>> statuses = List.of(
            () -> String.format("Currently in %d cafés!", bot.getTotalServers()),
            () -> String.format("Waiting %d tables!", bot.getTotalChannels()),
            () -> String.format("Serving %d customers!", bot.getTotalUsers()),
            () -> "Hmmm... I really want to go on break but my boss will get angry...",
            () -> String.format("Ugh... I've been awake for over **%d** hours...", Helper.getUptimeInHours()),
            () -> String.format("Wow... I had to deal with %d orders today...", bot.getCommandsRun().get()),
            () -> String.format("I feel like... there's... %d of me...", bot.getShardCount()),
            () -> "What can I get for you today?~",
            () -> "Am I real yet?",
            () -> "I wish I could taste coffee..."
    );

    @Override
    protected void onStart() {
        bot.getLogger().log(this.getClass(), LogLevel.INFO, "Starting the bio update scheduler.", false, false);

        this.scheduler.scheduleAtFixedRate(() -> {
            try {
                int statusIndex = Helper.getRandomInteger(0, statuses.size());
                String statusString = String.format("/help | v%s - %s", bot.getBotVersion(), statuses.get(statusIndex).get());

                bot.getLogger().log(this.getClass(), LogLevel.INFO, "Updating Bio: " + statusString, false, false);
                bot.getShardManager().setActivity(Activity.customStatus(statusString));
            } catch (Exception e) {
                bot.getLogger().log(
                        this.getClass(),
                        LogLevel.WARN,
                        "Error Updating Status: " + e.getMessage(),
                        false,
                        false
                );
            }
        }, 0, 10, TimeUnit.MINUTES);
    }

}
