package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.scheduling.MutualGuildsScheduler;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@RequiredArgsConstructor
public class MutualGuildsListener extends ListenerAdapter {

    private final CafeBot bot;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getAuthor().isBot()) return;

        String userId = event.getAuthor().getId();
        String guildId = event.getGuild().getId();

        bot.getMutualGuildsScheduler().addEntry(guildId, userId);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getUser().isBot()) return;

        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (guild == null || member == null) return;

        String userId = member.getId();
        String guildId = guild.getId();

        bot.getMutualGuildsScheduler().addEntry(userId, guildId);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!event.isFromGuild()) return;

        User user = event.getUser();

        if (user == null) return;
        if (event.getUser().isBot()) return;

        String userId = event.getUser().getId();
        String guildId = event.getGuild().getId();

        bot.getMutualGuildsScheduler().addEntry(userId, guildId);
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if (!event.isFromGuild()) return;
        User user = event.getUser();

        if (user == null) return;
        if (event.getUser().isBot()) return;

        String userId = event.getUser().getId();
        String guildId = event.getGuild().getId();

        bot.getMutualGuildsScheduler().addEntry(userId, guildId);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        if (event.getUser().isBot()) return;

        String userId = event.getUser().getId();
        String guildId = event.getGuild().getId();

        bot.getMutualGuildsScheduler().removeEntry(userId, guildId);
    }

}
