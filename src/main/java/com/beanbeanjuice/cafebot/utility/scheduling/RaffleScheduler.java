package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.type.Raffle;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RaffleScheduler extends CustomScheduler {

    public RaffleScheduler(CafeBot bot) {
        super(bot, "Raffle-Scheduler");
    }

    @Override
    protected void onStart() {
        // Every minute, get raffles that should be done.
        bot.getLogger().log(this.getClass(), LogLevel.INFO, "Starting the raffle scheduler...", false, false);

        this.scheduler.scheduleAtFixedRate(() -> {
            try {
                bot.getLogger().log(RaffleScheduler.class, LogLevel.INFO, "Checking raffles...", false, false);
                handleRaffles();
            } catch (Exception e) {
                bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Checking Raffles: " + e.getMessage(), false, false);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    private void handleRaffles() {
        bot.getCafeAPI().getRaffleApi().getRaffles(true, true).thenAccept((rafflesMap) -> {
            for (var entry : rafflesMap.entrySet()) {
                String guildId = entry.getKey();
                List<Raffle> raffles = entry.getValue();

                handleGuildRaffles(guildId, raffles);
            }
        });
    }

    private void handleGuildRaffles(String guildId, List<Raffle> raffles) {
        Guild guild = bot.getShardManager().getGuildById(guildId);
        if (guild == null) return;

        bot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildId, CustomChannelType.RAFFLE).thenAccept((customChannel) -> {
            TextChannel channel = guild.getTextChannelById(customChannel.getChannelId());
            if (channel == null) throw new IllegalStateException("Raffle Channel Not Set");

            for (Raffle raffle : raffles) {
                Message message = channel.retrieveMessageById(raffle.getMessageId()).complete();

                bot.getCafeAPI().getRaffleApi().closeRaffle(raffle.getId()).thenAccept((finalRaffle) -> {
                    handleRaffleCompletion(guild, message, finalRaffle);
                });
            }
        }).exceptionally((ex) -> {
            bot.getLogger().logToGuild(guild, Helper.errorEmbed("Raffle Channel Not Set", "I-... I don't think you have a raffle channel set... or the raffle message no longer exists..."));
            raffles.forEach((raffle) -> bot.getCafeAPI().getRaffleApi().deleteRaffle(raffle.getId()));
            return null;
        });
    }

    private void handleRaffleCompletion(Guild guild, Message message, Raffle raffle) {
        bot.getLogger().log(this.getClass(), LogLevel.INFO, String.format("Closing Raffle: %s", raffle.getId()), false, false);
        message.editMessageEmbeds(raffleCompleteEmbed(guild, raffle)).queue();
    }

    private MessageEmbed raffleCompleteEmbed(Guild guild, Raffle raffle) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(raffle.getTitle());
        embedBuilder.setFooter(String.format("Raffle ID: %s", raffle.getId()));
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setColor(Color.RED);
        raffle.getDescription().ifPresent(embedBuilder::setDescription);

        List<String> membersAsMentionList = new ArrayList<>();

        for (String winnerId : raffle.getWinners()) {
            Member member = guild.retrieveMemberById(winnerId).complete();

            if (member != null) membersAsMentionList.add(member.getAsMention());
            else membersAsMentionList.add(winnerId);
        }

        if (!membersAsMentionList.isEmpty()) embedBuilder.addField("Winners", String.join(", ", membersAsMentionList), true);
        else embedBuilder.addField("Winners", "*No one entered the raffle in time!*", true);

        return embedBuilder.build();
    }

}
