package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class UpdateMessageScheduler extends CustomScheduler {

    public UpdateMessageScheduler(CafeBot bot) {
        super(bot, "Update-Message-Scheduler");
    }

    @Override
    protected void onStart() {
        this.scheduler.scheduleAtFixedRate(() -> {
            try {
                bot.getLogger().log(CafeBot.class, LogLevel.DEBUG, "Sending bot status message...", true, false);

                bot.getUser("690927484199370753").queue((owner) -> {
                    bot.pmUser(owner, getUpdateEmbed(bot.getShardManager().getAverageGatewayPing()));
                });
            } catch (Exception e) {
                bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Sending Bot Status Message: " + e.getMessage(), true, true);
            }
        }, 0, 1, TimeUnit.DAYS);
    }

    public MessageEmbed getUpdateEmbed(double gatewayPing) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        double cpuLoad = (double) Math.round((ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getCpuLoad()*100) * 100) / 100;
        long systemMemoryTotal = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getTotalMemorySize()/1048576;
        long systemMemoryUsage = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getCommittedVirtualMemorySize()/1048576;
        long dedicatedMemoryTotal = Runtime.getRuntime().maxMemory()/1048576;
        long dedicatedMemoryUsage = Runtime.getRuntime().totalMemory()/1048576;

        embedBuilder.setTitle("Daily CafeBot Update");

        String description = String.format(
                """
                **__System Status__**: Online
                
                **__Current Version__** - `%s`
                **__Shards__** - `%d`
                **__Average Gateway Ping__** - `%.2f` ms
                **__CPU Usage__** - `%.2f%%`
                **__OS Memory Usage__** - `%d` mb / `%d` mb
                **__ Bot Memory Usage__** - `%d` mb / `%d` mb
                **__Bot Uptime__** - `%s`
                **__Commands Run (Since Restart)__** - `%d`
                """,
                bot.getBotVersion(),
                bot.getShardManager().getShardsTotal(),
                gatewayPing,
                cpuLoad,
                systemMemoryUsage,
                systemMemoryTotal,
                dedicatedMemoryUsage,
                dedicatedMemoryTotal,
                Helper.formatTimeDays(ManagementFactory.getRuntimeMXBean().getUptime()),
                bot.getCommandsRun().get());

        embedBuilder.setDescription(description);
        embedBuilder.setThumbnail(bot.getDiscordAvatarUrl());
        embedBuilder.setColor(Color.green);
        return embedBuilder.build();
    }

}
