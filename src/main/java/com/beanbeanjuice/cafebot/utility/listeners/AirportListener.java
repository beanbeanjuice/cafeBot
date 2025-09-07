package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.api.wrapper.CafeAPI;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.AirportMessageType;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.airport.AirportMessage;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomChannel;
import com.beanbeanjuice.cafebot.api.wrapper.type.airport.PartialAirportMessage;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class AirportListener extends ListenerAdapter {

    private final CafeAPI api;

    public AirportListener(CafeAPI api) {
        this.api = api;
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        handleAirportEvent(event, event.getUser(), CustomChannelType.AIRPORT_WELCOME, AirportMessageType.WELCOME);
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        handleAirportEvent(event, event.getUser(), CustomChannelType.AIRPORT_GOODBYE, AirportMessageType.GOODBYE);
    }

    public void handleAirportEvent(GenericGuildEvent event, User user, CustomChannelType channelType, AirportMessageType messageType) {
        String guildId = event.getGuild().getId();

        CompletableFuture<CustomChannel> channelIdFuture = api.getCustomChannelApi().getCustomChannel(guildId, channelType);
        CompletableFuture<AirportMessage> airportMessageFuture = api.getAirportApi().getAirportMessage(guildId, messageType)
                .exceptionally((ex) -> {
                    if (ex.getCause() instanceof ApiRequestException apiRequestException && apiRequestException.getStatusCode() == 404) return null;
                    throw new CompletionException(ex);
                });

        channelIdFuture.thenCombine(airportMessageFuture, ((customChannel, airportMessage) -> {
            // airportMessage may be null here.
            String channelId = customChannel.getChannelId();
            TextChannel channel = event.getGuild().getTextChannelById(channelId);

            if (channel == null) return false;
            if (airportMessage == null) {
                // Use default airport message.
                channel.sendMessageEmbeds(getAirportEmbed(getDefaultMessage(messageType), user)).queue();
                return true;
            }

            airportMessage.getMessage().ifPresentOrElse(
                    (message) -> channel.sendMessage(parseDescription(message, user)).addEmbeds(getAirportEmbed(airportMessage, user)).queue(),
                    () -> channel.sendMessageEmbeds(getAirportEmbed(airportMessage, user)).queue()
            );

            return true;
        }));
    }

    private PartialAirportMessage getDefaultMessage(AirportMessageType type) {
        String description = type == AirportMessageType.WELCOME ? "Welcome, {user}!" : "Goodbye, {user}...";

        return new PartialAirportMessage(type, null, null, null, null, null, description, null);
    }

    public static MessageEmbed getAirportEmbed(PartialAirportMessage airportMessage, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        airportMessage.getTitle().ifPresent((title) -> embedBuilder.setTitle(parseDescription(title, user)));
        airportMessage.getDescription().ifPresent((description) -> embedBuilder.setDescription(parseDescription(description, user)));

        // Attempts to set the thumbnail URL.
        airportMessage.getThumbnailUrl().ifPresent((thumbnailURL) -> {
            try { embedBuilder.setThumbnail(thumbnailURL); }
            catch (IllegalArgumentException ignored) { }
        });

        // Attempts to set the image URL.
        airportMessage.getImageUrl().ifPresent((imageURL) -> {
            try { embedBuilder.setImage(imageURL); }
            catch (IllegalArgumentException ignored) { }
        });

        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        return embedBuilder.build();
    }

    private static String parseDescription(String description, final User joiner) {
        description = description.replace("{user}", joiner.getAsMention());
        description = description.replace("\\n", "\n");
        return description;
    }

}
