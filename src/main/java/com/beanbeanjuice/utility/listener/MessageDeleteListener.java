package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.utility.handler.snipe.SnipeHandler;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ListenerAdapter} that listens for {@link net.dv8tion.jda.api.events.message.MessageDeleteEvent MessageDeleteEvent}.
 *
 * @author beanbeanjuice
 * @since v3.1.0
 */
public class MessageDeleteListener extends ListenerAdapter {

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        if (event.isFromGuild())
            SnipeHandler.moveSnipe(event.getChannel().getId(), event.getMessageId());
    }

}
