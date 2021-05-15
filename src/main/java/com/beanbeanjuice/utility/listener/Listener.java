package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.main.CafeBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

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
        CafeBot.getGuildHandler().removeGuild(event.getGuild());
        CafeBot.getGuildHandler().checkGuilds();
        CafeBot.updateGuildPresence(); // Updates the amount of servers in the status.
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {

        TextChannel channel = event.getGuild().getDefaultChannel();

        if (channel != null) {
            event.getGuild().getDefaultChannel().sendMessage(guildJoinEmbed()).queue();
        }

        CafeBot.getGuildHandler().addGuild(event.getGuild());
        CafeBot.getGuildHandler().checkGuilds();
        CafeBot.updateGuildPresence(); // Updates the amount of servers in the status.
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        // Checking if the event is a counting channel
        TextChannel countingChannel;

        try {
            countingChannel = CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getCountingChannel();
        } catch (NullPointerException e) {
            countingChannel = null;
        }

        if (event.getChannel().equals(countingChannel)) {
            String number = event.getMessage().getContentRaw().split(" ")[0];
            if (CafeBot.getGeneralHelper().isNumber(number)) {
                CafeBot.getCountingHelper().checkNumber(event, Integer.parseInt(number));
                return;
            }
        }

        User user = event.getAuthor();

        // Checking if the person is a bot or if the event is a webhook message.
        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        // Sets the prefix
        String prefix;
        try {
            prefix = CafeBot.getGuildHandler().getCustomGuild(event.getGuild().getId()).getPrefix();
        } catch (NullPointerException e) {
            event.getChannel().sendMessage(startingUpEmbed()).queue();
            return;
        }

        String raw = event.getMessage().getContentRaw();

        if (raw.startsWith(prefix)) {
            CafeBot.getCommandManager().handle(event, prefix);
        }
    }

    @NotNull
    private MessageEmbed guildJoinEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("The Barista Has Arrived");
        embedBuilder.setColor(Color.green);
        embedBuilder.setThumbnail(CafeBot.getJDA().getSelfUser().getAvatarUrl());
        StringBuilder description = new StringBuilder();
        description.append("Thank you for inviting me! I hope I'm not too much trouble.\n\n\n")
                .append("For a list of command sections, type `!!help`.\n\n")
                .append("For a list of commands in a section, type `!!help (command section name)`!\n\n")
                .append("For help with a specific command, type `!!help (command name/alias)`!\n\n\n")
                .append("Anyway... be sure to report any bug reports or features you want to see!");
        embedBuilder.setDescription(description.toString());
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed startingUpEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Bot Command Error...");
        embedBuilder.setColor(Color.red);
        embedBuilder.setDescription("Sorry... you can't run any commands right now because the connection is either lost," +
                " or I am starting up.");
        return embedBuilder.build();
    }
}