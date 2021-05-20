package com.beanbeanjuice.utility.twitch;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.guild.CustomGuild;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * A class for handling twitch message events.
 *
 * @author beanbeanjuice
 */
public class TwitchMessageEventHandler extends SimpleEventHandler {

    /**
     * @param event The {@link ChannelGoLiveEvent}.
     */
    @EventSubscriber
    public void printChannelLive(@NotNull ChannelGoLiveEvent event) {

        // Converts the Twitch Name to lower case.
        String twitchName = event.getChannel().getName().toLowerCase();

        // Gets the Guilds that are listening for that twitch name.
        ArrayList<String> guilds = CafeBot.getTwitchHandler().getGuildsForChannel(twitchName);

        // If there are no guilds/sql error, do nothing.
        if (guilds == null) {
            return;
        }

        // Go through each guild.
        for (String guildID : guilds) {
            CustomGuild customGuild = CafeBot.getGuildHandler().getCustomGuild(guildID);
            if (customGuild.getTwitchChannels().contains(twitchName)) {
                String liveChannelID = CafeBot.getGuildHandler().getCustomGuild(guildID).getLiveChannelID();
                TextChannel liveChannel = CafeBot.getGuildHandler().getGuild(guildID).getTextChannelById(liveChannelID);

                StringBuilder message = new StringBuilder();

                try {
                    message.append(CafeBot.getGuildHandler().getCustomGuild(guildID).getLiveNotificationsRole().getAsMention())
                            .append(", ");
                } catch (NumberFormatException | NullPointerException ignored) {
                }

                message.append(event.getChannel().getName())
                        .append(", is now live on ")
                        .append("https://www.twitch.tv/")
                        .append(event.getChannel().getName());

                try {
                    liveChannel.sendMessage(message.toString()).embed(liveEmbed(event)).queue();
                } catch (NullPointerException ignored) {
                } // If the live channel no longer exists, then just don't print the message.
            }
        }
    }

    /**
     * Creates the {@link MessageEmbed} to be sent in the LiveChannel.
     * @param event The {@link ChannelGoLiveEvent}.
     * @return The {@link MessageEmbed} to be sent.
     */
    @NotNull
    public MessageEmbed liveEmbed(@NotNull ChannelGoLiveEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.pink);
        embedBuilder.setAuthor(event.getChannel().getName());
        embedBuilder.setTitle(event.getStream().getTitle(), "https://www.twitch.tv/" + event.getChannel().getName());
        embedBuilder.setImage(event.getStream().getThumbnailUrl(320, 180));
        embedBuilder.addField("Game", event.getStream().getGameName(), true);
        embedBuilder.addField("Viewers", String.valueOf(event.getStream().getViewerCount()), true);
        return embedBuilder.build();
    }

}