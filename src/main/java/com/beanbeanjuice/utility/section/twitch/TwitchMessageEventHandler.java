package com.beanbeanjuice.utility.section.twitch;

import com.beanbeanjuice.utility.handler.guild.CustomGuild;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
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
        ArrayList<String> guilds = TwitchHandler.getGuildsForChannel(twitchName);

        // If there are no guilds/sql error, do nothing.
        if (guilds.size() == 0) {
            return;
        }

        // Go through each guild.
        for (String guildID : guilds) {
            CustomGuild customGuild = GuildHandler.getCustomGuild(guildID);
            if (customGuild.getTwitchChannels().contains(twitchName)) {
                String liveChannelID = GuildHandler.getCustomGuild(guildID).getLiveChannelID();
                TextChannel liveChannel = GuildHandler.getGuild(guildID).getTextChannelById(liveChannelID);

                StringBuilder message = new StringBuilder();

                try {
                    message.append(GuildHandler.getCustomGuild(guildID).getLiveNotificationsRole().getAsMention())
                            .append(", ");
                } catch (NumberFormatException | NullPointerException ignored) {
                }

                message.append(event.getChannel().getName())
                        .append(", is now live on ")
                        .append("https://www.twitch.tv/")
                        .append(event.getChannel().getName());

                try {
                    liveChannel.sendMessage(message.toString()).setEmbeds(liveEmbed(event)).queue();
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
        return new EmbedBuilder()
                .setColor(Color.pink)
                .setAuthor(event.getChannel().getName())
                .setTitle(event.getStream().getTitle(), "https://www.twitch.tv/" + event.getChannel().getName())
                .setImage(event.getStream().getThumbnailUrl(320, 180))
                .addField("Game", event.getStream().getGameName(), true)
                .addField("Viewers", String.valueOf(event.getStream().getViewerCount()), true)
                .build();
    }

}
