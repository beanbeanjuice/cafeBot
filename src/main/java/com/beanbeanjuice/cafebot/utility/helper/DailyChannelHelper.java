package com.beanbeanjuice.cafebot.utility.helper;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class DailyChannelHelper {

    private final CafeBot cafeBot;

    public DailyChannelHelper(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
    }

    public void start() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                cafeBot.getLogger().log(DailyChannelHelper.class, LogLevel.INFO, "Resetting daily channels...");
                handleDailyResets();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, TimeUnit.DAYS.toMillis(1), TimeUnit.DAYS.toMillis(1));
    }

    private void handleDailyResets() {
        this.cafeBot.getCafeAPI().getGuildsEndpoint().getAllGuildInformation().thenAcceptAsync((guildsMap) -> {
            guildsMap.forEach((guildId, guildInfo) -> {
                String dailyChannelID = guildInfo.getSetting(GuildInformationType.DAILY_CHANNEL_ID);

                Guild guild = this.cafeBot.getJDA().getGuildById(guildId);
                if (guild == null) return;

                TextChannel channel = guild.getTextChannelById(dailyChannelID);
                if (channel == null) return;

                channel.createCopy().queue((ignored) -> channel.delete().queue());
            });
        });
    }

}
