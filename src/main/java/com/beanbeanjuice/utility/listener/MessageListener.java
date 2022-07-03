package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.handler.guild.CustomGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ListenerAdapter} for listening to messages.
 *
 * @author beanbeanjuice
 */
public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        if (!event.isFromGuild()) {
            return;
        }

        CustomGuild guildInformation = Bot.getGuildHandler().getCustomGuild(event.getGuild());

        // Checking if the event is a counting channel
        TextChannel countingChannel;

        try {
            countingChannel = guildInformation.getCountingChannel();
        } catch (NullPointerException e) {
            countingChannel = null;
        }

        if (event.getChannel().equals(countingChannel)) {
            String number = event.getMessage().getContentRaw().split(" ")[0];
            if (Helper.isNumber(number)) {
                Bot.getCountingHelper().checkNumber(event, Integer.parseInt(number));
                return;
            }
        }
        
        if (event.getMessage().getContentRaw().startsWith(guildInformation.getPrefix())) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("ATTENTION!", "https://youtu.be/4XxcpBxSCiU")
                    .setDescription("Everything has been transitioned to slash commands! Everything has remained " +
                    "the same except for slash commands. Use the commands as you normally would, but put a slash in " +
                    "front of it instead! If you haven't already, click the link below to add slash commands for **CafeBot**.")
                    .setColor(Helper.getRandomColor())
                    .setFooter("Sorry for the inconvenience!");
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(
                    Button.link("https://discord.com/api/oauth2/authorize?client_id=787162619504492554&permissions=8&scope=bot%20applications.commands",
                            "Add Slash Commands").withEmoji(Emoji.fromFormatted("<a:wowowow:886217210010431508>"))
            ).queue();
        }
    }

}
