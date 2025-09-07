package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomChannel;
import com.beanbeanjuice.cafebot.api.wrapper.type.Raffle;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jspecify.annotations.NonNull;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class RaffleListener extends ListenerAdapter {

    private final CafeBot bot;

    public RaffleListener(CafeBot bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReactionAdd(@NonNull MessageReactionAddEvent event) {
        handleSubmission(event, true);
    }

    @Override
    public void onMessageReactionRemove(@NonNull MessageReactionRemoveEvent event) {
        handleSubmission(event, false);
    }

    @Override
    public void onGuildMemberRemove(@NonNull GuildMemberRemoveEvent event) {
        if (event.getUser().isBot()) return;

        // Remove raffle submission if user leaves.
        bot.getCafeAPI().getRaffleApi().getRaffles(event.getGuild().getId(), true, false).thenAccept((raffles) -> {
            for (Raffle raffle : raffles) {
                bot.getCafeAPI().getRaffleApi().setSubmission(raffle.getId(), event.getUser().getId(), false);
            }
        });
    }

    private void handleSubmission(GenericMessageReactionEvent event, boolean newStatus) {
        if (event.getUser() == null) return;
        if (event.getUser().isBot()) return;
        if (!event.getEmoji().asUnicode().equals(Emoji.fromUnicode("✅"))) return;

        String guildId = event.getGuild().getId();
        String channelId = event.getChannel().getId();
        String messageId = event.getMessageId();
        String userId = event.getUserId();

        CompletableFuture<Raffle> raffleFuture = bot.getCafeAPI().getRaffleApi().getRaffle(guildId, messageId);
        CompletableFuture<CustomChannel> raffleChannelFuture = bot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildId, CustomChannelType.RAFFLE);

        raffleFuture.thenAcceptBoth(raffleChannelFuture, (raffle, customChannel) -> {
            if (!customChannel.getChannelId().equals(channelId)) return;

            // At this point, raffleID should be the same, and we *are* in the correct channel. This IS the raffle.
            bot.getCafeAPI().getRaffleApi().setSubmission(raffle.getId(), userId, newStatus).thenRun(() -> {
                Helper.pmUser(event.getUser(), getVoteMessage(raffle, newStatus));
            });
        });
    }

    private MessageEmbed getVoteMessage(Raffle raffle, boolean newStatus) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(String.format("Submission for Raffle: %s", raffle.getTitle()));
        embedBuilder.setFooter(String.format("Raffle ID: %d", raffle.getId()));

        if (newStatus) {
            embedBuilder.setDescription("Hai hai!~ Don't worry. I've got your submission all ready!");
            embedBuilder.setColor(Color.GREEN);
        } else {
            embedBuilder.setDescription("Oh... I got my boss to remove your submission from the raffle...");
            embedBuilder.setColor(Color.RED);
        }

        return embedBuilder.build();
    }

}
