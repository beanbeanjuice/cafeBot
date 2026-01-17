package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class HoneypotListener extends ListenerAdapter {

    private final CafeBot bot;

    public HoneypotListener(CafeBot bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getAuthor().isBot()) return;

        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (member == null) return;

        handleBan(guild, member, event.getChannel().getId());
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!event.isFromGuild()) return;

        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (member == null) return;

        handleBan(guild, member, event.getChannel().getId());
    }

    private void handleBan(Guild guild, Member member, String channelId) {
        bot.getCafeAPI().getCustomChannelApi().getCustomChannel(guild.getId(), CustomChannelType.HONEYPOT).thenAccept((customChannel) -> {
            if (!customChannel.getChannelId().equals(channelId)) return;

            try {
                guild.ban(member.getUser(), 1, TimeUnit.DAYS).reason("cafeBot: Message or reaction sent in honeypot channel.").queue();
                bot.getLogger().logToGuild(guild, Helper.successEmbed("Honeypot", String.format("A pesky bot tried to grab some honey! %s - **%s** (%s) was banned.", member.getAsMention(), member.getUser().getName(), member.getId())));
            } catch (Exception e) {
                bot.getLogger().logToGuild(guild, Helper.errorEmbed("Honeypot Error", "Error Banning User in Honeypot Channel: " + e.getMessage()));
            }
        });
    }

}
