package com.beanbeanjuice.utility.twitch;

import com.beanbeanjuice.main.BeanBot;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A class for handling twitch message events.
 *
 * @author beanbeanjuice
 */
public class TwitchMessageEventHandler extends SimpleEventHandler {

    private String guildID;
    private String liveChannelID;

    /**
     * Creates a new {@link TwitchMessageEventHandler} object.
     * @param guildID The ID of the {@link Guild} for the message to be sent in.
     * @param liveChannelID The ID of the {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} for the message to be sent in.
     */
    public TwitchMessageEventHandler(@NotNull String guildID, @NotNull String liveChannelID) {
        this.guildID = guildID;
        this.liveChannelID = liveChannelID;
    }

    /**
     * @param event Where to send the {@link ChannelGoLiveEvent}.
     */
    @EventSubscriber
    public void printChannelLive(@NotNull ChannelGoLiveEvent event) {
        System.out.println(event.getChannel().getName() + " is now online at https://www.twitch.tv/" + event.getChannel().getName());

        TextChannel liveChannel = BeanBot.getGuildHandler().getGuild(guildID).getTextChannelById(liveChannelID);

        try {
            liveChannel.sendMessage("@everyone, " + event.getChannel().getName() + ", is now live on " +
                    "https://www.twitch.tv/" + event.getChannel().getName()).embed(liveEmbed(event)).queue();
        } catch (NullPointerException ignored) {}
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