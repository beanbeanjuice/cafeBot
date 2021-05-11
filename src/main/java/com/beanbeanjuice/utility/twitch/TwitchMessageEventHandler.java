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

    /**
     * Creates a new {@link TwitchMessageEventHandler} object.
     * @param guildID The ID of the {@link Guild} for the message to be sent in.
     */
    public TwitchMessageEventHandler(@NotNull String guildID) {
        this.guildID = guildID;
    }

    /**
     * @param event The {@link ChannelGoLiveEvent}.
     */
    @EventSubscriber
    public void printChannelLive(@NotNull ChannelGoLiveEvent event) {
        String liveChannelID = BeanBot.getGuildHandler().getCustomGuild(guildID).getLiveChannelID();
        TextChannel liveChannel = BeanBot.getGuildHandler().getGuild(guildID).getTextChannelById(liveChannelID);

        StringBuilder message = new StringBuilder();

        try {
            message.append(BeanBot.getGuildHandler().getCustomGuild(guildID).getLiveNotificationsRole().getAsMention())
            .append(", ");
        } catch (NumberFormatException | NullPointerException ignored) {}

        message.append(event.getChannel().getName())
                .append(", is now live on ")
                .append("https://www.twitch.tv/")
                .append(event.getChannel().getName());

        try {
            liveChannel.sendMessage(message.toString()).embed(liveEmbed(event)).queue();
        } catch (NullPointerException ignored) {} // If the live channel no longer exists, then just don't print the message.
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