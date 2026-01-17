package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.CafeBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ConfessionBanListener extends ListenerAdapter {

    private final CafeBot bot;

    public ConfessionBanListener(CafeBot bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getMember() == null) return;

        Member admin = event.getMember();
        if (admin.getUser().isBot()) return;
        if (!admin.hasPermission(Permission.BAN_MEMBERS)) return;

        Optional<String> memberIdOptional = bot.getConfessionHandler().getUserId(event.getMessageId());
        if (memberIdOptional.isEmpty()) return;

        Guild guild = event.getGuild();
        String memberId = memberIdOptional.get();

        guild.retrieveMemberById(memberId).queue((member) -> {
            member.ban(0, TimeUnit.MINUTES).reason(
                    String.format("cafeBot: Banned by %s (%s) for confession content violation.", admin.getUser().getName(), admin.getId())
            ).queue((success) -> event.retrieveMessage().queue((message) -> message.delete().queue()));
        });
    }

}
