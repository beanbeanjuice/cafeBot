package com.beanbeanjuice.cafebot.utility.section.settings;

import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * A static class used for resetting the daily {@link TextChannel} in a {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class DailyChannelHandler {

    public static void start() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 18);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GuildHandler.getGuilds().forEach(
                        (guildID, customGuild) ->
                        customGuild.getDailyChannel().ifPresent((dailyChannel) ->
                        resetChannel(guildID, dailyChannel)));
            }
        }, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }

    private static void resetChannel(final String guildID, final TextChannel dailyChannel) {
        try {
            // Creates a copy of the channel.
            dailyChannel.createCopy().queue(channel -> {

                // If the channel was successfully changed, then delete it. Else, delete the copied channel.
                if (GuildHandler.getCustomGuild(guildID).setDailyChannelID(channel.getId())) dailyChannel.delete().queue();
                else channel.delete().queue();
            });
        } catch (PermissionException e) {
            dailyChannel.sendMessageEmbeds(Helper.errorEmbed(
                    "Missing Permission",
                    e.getMessage()
            )).queue();
        }
    }

}
