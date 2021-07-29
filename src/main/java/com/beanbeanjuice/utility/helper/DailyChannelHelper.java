package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.CafeBot;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class DailyChannelHelper {

    public DailyChannelHelper() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 18);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                CafeBot.getGuildHandler().getGuilds().forEach((guildID, customGuild) -> {
                    TextChannel dailyChannel = customGuild.getDailyChannel();

                    // If the channel does exist.
                    if (dailyChannel != null) {

                        try {

                            // Creates a copy of the channel.
                            dailyChannel.createCopy().queue(channel -> {

                                // If the channel was successfully changed, then delete it. Else, delete the copied channel.
                                if (CafeBot.getGuildHandler().getCustomGuild(guildID).setDailyChannelID(channel.getId())) {
                                    dailyChannel.delete().queue();
                                } else {
                                    channel.delete().queue();
                                }
                            });
                        } catch (PermissionException e) {
                            dailyChannel.sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                                    "Missing Permission",
                                    e.getMessage()
                            )).queue();
                        }
                    }
                });
            }
        }, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }

}
