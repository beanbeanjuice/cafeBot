package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.utility.handler.CountingHandler;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.handler.snipe.SnipeHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.handler.guild.CustomGuild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ListenerAdapter} for listening to messages.
 *
 * @author beanbeanjuice
 */
public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        if (!event.isFromGuild()) { return; }

        if (event.getChannelType() == ChannelType.TEXT)
            SnipeHandler.addPreSnipe(event);

        CustomGuild guildInformation = GuildHandler.getCustomGuild(event.getGuild());

        // Checking if the event is a counting channel
        TextChannel countingChannel;

        try {
            countingChannel = guildInformation.getCountingChannel();
        } catch (NullPointerException e) {
            countingChannel = null;
        }

        if (event.getChannel().equals(countingChannel)) {
            String number = event.getMessage().getContentRaw().split(" ")[0];
            if (Helper.isNumber(number)) {
                CountingHandler.checkNumber(event, Integer.parseInt(number));
                return;
            }
        }
    }

}
