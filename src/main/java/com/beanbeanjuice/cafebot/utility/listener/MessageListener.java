package com.beanbeanjuice.cafebot.utility.listener;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafebot.utility.handler.CountingHandler;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.handler.snipe.SnipeHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.handler.guild.CustomGuild;
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

        if (!event.isFromGuild() || event.getAuthor().isBot() || event.getAuthor().isSystem()) { return; }

        // For sniping messages
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
                Bot.commandsRun++;
                return;
            }
        }
    }

}
