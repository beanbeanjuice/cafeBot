package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.TimeUnit;

public class DailyChannelScheduler extends CustomScheduler {

    public DailyChannelScheduler(CafeBot bot) {
        super(bot, "Daily-Channel-Scheduler");
    }

    @Override
    protected void onStart() {
        bot.getLogger().log(this.getClass(), LogLevel.INFO, "Starting the daily channel scheduler...", false, false);

        this.scheduler.scheduleAtFixedRate(() -> {
            try {
                bot.getLogger().log(DailyChannelScheduler.class, LogLevel.INFO, "Resetting daily channels...", false, false);
                handleDailyResets();
            } catch (Exception e) {
                bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Resetting Daily Channels: " + e.getMessage(), true, true);
            }
        }, 1, 1, TimeUnit.DAYS);
    }

    private void handleDailyResets() {
        this.bot.getCafeAPI().getCustomChannelApi().getCustomChannels(CustomChannelType.DAILY).thenAccept(channels -> {
            channels.forEach((guildId, customChannel) -> {
                Guild guild = this.bot.getShardManager().getGuildById(guildId);
                if (guild == null) return;

                TextChannel channel = guild.getTextChannelById(customChannel.getChannelId());
                if (channel == null) return;

                channel.createCopy().queue((newChannel) -> {
                    bot.getCafeAPI().getCustomChannelApi().setCustomChannel(guildId, CustomChannelType.DAILY, newChannel.getId());
                    channel.delete().queue();

                    bot.increaseCommandsRun();
                });
            });
        });
    }

}
