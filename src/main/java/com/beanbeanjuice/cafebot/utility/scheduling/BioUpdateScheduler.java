package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BioUpdateScheduler extends CustomScheduler {

    public BioUpdateScheduler(CafeBot bot) {
        super(bot, "Bio-Scheduler");
    }

    @Override
    protected void onStart() {
        String initialString = "/help | v" + bot.getBotVersion() + " - ";

        this.scheduler.scheduleAtFixedRate(() -> {
            try {
                int num = Helper.getRandomInteger(1, 7);  // (Inclusive, Exclusive)
                String finalString = "";

                switch (num) {
                    case 1 -> finalString = "Currently in " + bot.getTotalServers() + " cafés!";
                    case 2 -> finalString = "Waiting " + bot.getTotalChannels() + " tables!";
                    case 3 -> finalString = "Serving " + bot.getTotalUsers() + " customers!";
                    case 4 -> finalString = "Hmmm... I really want to go on break but my boss will get angry...";
                    case 5 -> finalString = "Wow... I had to deal with " + bot.getCommandsRun() + " orders today...";
                    case 6 -> finalString = "I feel like there's only " + bot.getShardCount() + " of me...";
                }

                bot.getShardManager().setActivity(Activity.customStatus(initialString + finalString));
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
