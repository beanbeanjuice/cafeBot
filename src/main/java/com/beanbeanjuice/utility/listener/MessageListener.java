package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.Helper;
import io.github.beanbeanjuice.cafeapi.cafebot.guilds.GuildInformation;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        GuildInformation guildInformation = Bot.getCafeAPI().GUILD.getGuildInformation(event.getGuild().getId());

        // TODO: Add button with this link https://discord.com/api/oauth2/authorize?client_id=787162619504492554&permissions=8&scope=bot%20applications.commands
        if (event.getMessage().getContentRaw().startsWith(guildInformation.getPrefix())) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("ATTENTION!", "https://youtu.be/4XxcpBxSCiU")
                    .setDescription("Everything has been transitioned to slash commands! Everything has remained " +
                    "the same except for slash commands. Use the commands as you normally would, but put a slash in " +
                    "front of it instead!")
                    .setColor(Helper.getRandomColor())
                    .setFooter("Sorry for the inconvenience!");
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
