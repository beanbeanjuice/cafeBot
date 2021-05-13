package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.main.BeanBot;
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
        BeanBot.getGuildHandler().removeGuild(event.getGuild());
        BeanBot.getGuildHandler().checkGuilds();
        BeanBot.updateGuildPresence(); // Updates the amount of servers in the status.
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {

        TextChannel channel = event.getGuild().getDefaultChannel();

        if (channel != null) {
            event.getGuild().getDefaultChannel().sendMessage("The barista has arrived!").queue();
        }

        BeanBot.getGuildHandler().addGuild(event.getGuild());
        BeanBot.getGuildHandler().checkGuilds();
        BeanBot.updateGuildPresence(); // Updates the amount of servers in the status.
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        TextChannel countingChannel;

        try {
            countingChannel = BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).getCountingChannel();
        } catch (NullPointerException e) {
            countingChannel = null;
        }

        if (event.getChannel().equals(countingChannel)) {

            String number = event.getMessage().getContentRaw().split(" ")[0];

            if (BeanBot.getGeneralHelper().isNumber(number)) {
                BeanBot.getCountingHelper().checkNumber(event, Integer.parseInt(number));
                return;
            }

        }


        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String prefix;
        try {
            prefix = BeanBot.getGuildHandler().getCustomGuild(event.getGuild().getId()).getPrefix();
        } catch (NullPointerException e) {
            event.getChannel().sendMessage(startingUpEmbed()).queue();
            return;
        }

        String raw = event.getMessage().getContentRaw();

        if (raw.startsWith(prefix)) {
            BeanBot.getCommandManager().handle(event, prefix);
        }
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