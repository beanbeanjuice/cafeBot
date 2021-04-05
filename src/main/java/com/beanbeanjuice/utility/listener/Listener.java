package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandManager;
import com.beanbeanjuice.utility.guild.GuildHandler;
import me.duncte123.botcommons.BotCommons;
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

    private GuildHandler guildHandler;
    private String botPrefix;
    private CommandManager commandManager;

    public Listener(@NotNull String botPrefix, @NotNull CommandManager commandManager) {
        this.botPrefix = botPrefix;
        this.commandManager = commandManager;
    }

    public void setGuildHandler(GuildHandler guildHandler) {
        this.guildHandler = guildHandler;
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {

    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        guildHandler.updateGuildCache();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        guildHandler.updateGuildCache();

        TextChannel channel = event.getGuild().getDefaultChannel();

        if (channel != null) {
            event.getGuild().getDefaultChannel().sendMessage("The barista has arrived!").queue();
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String prefix = guildHandler.getCustomGuild(event.getGuild().getId()).getPrefix();

        if (prefix == null) {
            prefix = botPrefix;
        }

        String raw = event.getMessage().getContentRaw();

        if (raw.startsWith(prefix)) {
            commandManager.handle(event, prefix);
        }
    }
}