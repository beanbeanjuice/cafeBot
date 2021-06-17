package com.beanbeanjuice.utility.sections.social.vent;

import com.beanbeanjuice.CafeBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.HashMap;

/**
 * A custom handler to handle {@link Vent} objects.
 */
public class VentHandler {

    private HashMap<String, Vent> ventMap;

    /**
     * Creates a new {@link VentHandler} object.
     */
    public VentHandler() {
        ventMap = new HashMap<>();
    }

    /**
     * Gets a specific {@link Vent} from the {@link net.dv8tion.jda.api.entities.User User} ID.
     * @param userID The ID of the specified {@link net.dv8tion.jda.api.entities.User User}.
     * @return The {@link Vent} associated with the {@link net.dv8tion.jda.api.entities.User User}.
     */
    @Nullable
    public Vent getVent(@NotNull String userID) {
        try {
            return ventMap.get(userID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Adds a {@link Vent} for the {@link net.dv8tion.jda.api.entities.User User}.
     * @param userID The ID of the {@link net.dv8tion.jda.api.entities.User User}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @return Whether or not a vent was successfully added.
     */
    @NotNull
    public Boolean addVent(@NotNull String userID, @NotNull String guildID) {
        if (ventMap.containsKey(userID)) {
            return false;
        }

        ventMap.put(userID, new Vent(userID, guildID));
        return true;
    }

    /**
     * Removes a vent for a specified {@link net.dv8tion.jda.api.entities.User User}.
     * @param userID The ID of the specified {@link net.dv8tion.jda.api.entities.User User}.
     */
    public void removeVent(@NotNull String userID) {
        ventMap.remove(userID);
    }

    /**
     * Sends a {@link Vent}.
     * @param content The {@link String} content of the {@link Vent}.
     * @param channel The {@link TextChannel} to send the {@link Vent} in.
     */
    public void sendVent(@NotNull String content, @NotNull TextChannel channel) {
        channel.sendMessage(ventBuilder(content)).queue();
    }

    private MessageEmbed ventBuilder(@NotNull String ventContent) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Anonymous Vent");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription(ventContent);
        embedBuilder.setTimestamp(new Date().toInstant());
        return embedBuilder.build();
    }

}
