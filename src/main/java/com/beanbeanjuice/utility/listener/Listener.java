package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.main.BeanBot;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * A listener class used for things that happens when the bot joins/leaves a server.
 *
 * @author beanbeanjuice
 */
public class Listener extends ListenerAdapter {

    @Override
    public void onReady(@Nonnull ReadyEvent event) {

    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        BeanBot.getGuildHandler().removeGuild(event.getGuild());
        BeanBot.getGuildHandler().checkGuilds();
        updateGuildPresence();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {

        TextChannel channel = event.getGuild().getDefaultChannel();

        if (channel != null) {
            event.getGuild().getDefaultChannel().sendMessage("The barista has arrived!").queue();
        }

        BeanBot.getGuildHandler().addGuild(event.getGuild());
        BeanBot.getGuildHandler().checkGuilds();
        updateGuildPresence();
    }

    private void updateGuildPresence() {
        BeanBot.getJDA().getPresence().setActivity(
                Activity.playing(
                        "!! | beanBot " + BeanBot.getBotVersion() + " - Currently in " +
                                BeanBot.getJDA().getGuilds().size() + " servers!"));
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String prefix = BeanBot.getGuildHandler().getCustomGuild(event.getGuild().getId()).getPrefix();

        if (prefix == null) {
            prefix = BeanBot.getPrefix();
        }

        String raw = event.getMessage().getContentRaw();

        if (raw.startsWith(prefix)) {
            BeanBot.getCommandManager().handle(event, prefix);
        }
    }
}