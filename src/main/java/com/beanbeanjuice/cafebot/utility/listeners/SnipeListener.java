package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.types.PotentialSnipeMessage;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jspecify.annotations.NonNull;

import java.time.Instant;

@RequiredArgsConstructor
public class SnipeListener extends ListenerAdapter {

    private final CafeBot bot;

    @Override
    public void onMessageReceived(@NonNull MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getAuthor().isBot()) return;

        User user = event.getAuthor();
        String channelId = event.getChannel().getId();
        String message = event.getMessage().getContentRaw();

        PotentialSnipeMessage potentialSnipeMessage = new PotentialSnipeMessage(channelId, user, message, Instant.now());
        bot.getSnipeHandler().addPotentialMessage(event.getMessageId(), potentialSnipeMessage);
    }

    @Override
    public void onMessageDelete(@NonNull MessageDeleteEvent event) {
        if (!event.isFromGuild()) return;

        bot.getSnipeHandler().convertToSnipe(event.getMessageId());
    }

}
