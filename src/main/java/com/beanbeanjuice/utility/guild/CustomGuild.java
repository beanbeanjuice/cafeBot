package com.beanbeanjuice.utility.guild;

import com.beanbeanjuice.main.BeanBot;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link CustomGuild} that contains {@link net.dv8tion.jda.api.entities.Guild Guild} information.
 *
 * @author beanbeanjuice
 */
public class CustomGuild {

    private String guildID;
    private String prefix;

    /**
     * Creates a new {@link CustomGuild} object.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     * @param prefix The bot's prefix for the {@link net.dv8tion.jda.api.entities.Guild Guild}.
     */
    public CustomGuild(@NotNull String guildID, @NotNull String prefix) {
        this.guildID = guildID;
        this.prefix = prefix;
    }

    /**
     * @return The ID for the {@link CustomGuild}.
     */
    @NotNull
    public String getGuildID() {
        return guildID;
    }

    /**
     * @return The prefix for the {@link CustomGuild}.
     */
    @NotNull
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the prefix for the {@link CustomGuild}.
     * @param newPrefix The new prefix.
     * @return Whether or not setting the prefix was successful.
     */
    @NotNull
    public Boolean setPrefix(String newPrefix) {
        return BeanBot.getGuildHandler().updateGuildPrefix(guildID, newPrefix);
    }

}
