package com.beanbeanjuice.utility.command;

import com.beanbeanjuice.main.BeanBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * A class used for handling the contexts for the {@link ICommand}.
 *
 * @author beanbeanjuice
 */
public class CommandContext {

    private final GuildMessageReceivedEvent event;
    private final ArrayList<String> arguments;
    private final String prefix;

    /**
     * Creates a new instance of the {@link CommandContext} object.
     * @param event The event that triggered the {@link ICommand}.
     * @param arguments The arguments for the {@link GuildMessageReceivedEvent}.
     * @param prefix The bot's prefix.
     */
    public CommandContext(@NotNull GuildMessageReceivedEvent event, @NotNull ArrayList<String> arguments, @NotNull String prefix) {
        this.event = event;
        this.arguments = arguments;
        this.prefix = prefix;
    }

    /**
     * @return The current {@link Guild} the {@link GuildMessageReceivedEvent} was sent in.
     */
    @NotNull
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    /**
     * @return Gets the self member of the Discord bot.
     */
    @Nullable
    public Member getSelfMember() {
        return getGuild().getSelfMember();
    }

    @Nullable
    public ShardManager getShardManager() {
        return BeanBot.getJDA().getShardManager();
    }

    /**
     * @return The current {@link GuildMessageReceivedEvent} that was sent.
     */
    @NotNull
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    /**
     * @return The arguments that were sent during the {@link GuildMessageReceivedEvent}.
     */
    @NotNull
    public ArrayList<String> getArguments() {
        return this.arguments;
    }

    /**
     * @return The prefix for the current {@link GuildMessageReceivedEvent}.
     */
    @NotNull
    public String getPrefix() {
        return this.prefix;
    }

}
