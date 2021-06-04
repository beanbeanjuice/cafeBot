package com.beanbeanjuice.utility.sections.music.custom;

import com.beanbeanjuice.CafeBot;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link CustomSong} class used for the {@link CustomSongManager}.
 *
 * @author beanbeanjuice
 */
public class CustomSong {

    private final String name;
    private final String author;
    private final Long length;
    private final User requester;

    /**
     * Creates a new {@link CustomSong} object.
     * @param name The name of the {@link CustomSong}.
     * @param author The author for the {@link CustomSong}.
     * @param length The length in milliseconds of the {@link CustomSong}.
     * @param requester The {@link User} who requested the {@link CustomSong}.
     */
    public CustomSong(@NotNull String name, @NotNull String author, @NotNull Long length, @NotNull User requester) {
        this.name = name;
        this.author = author;
        this.length = length;
        this.requester = requester;
    }

    /**
     * @return The name of the {@link CustomSong}.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @return The author for the {@link CustomSong}.
     */
    @NotNull
    public String getAuthor() {
        return author;
    }

    /**
     * @return The formatted time length for the {@link CustomSong}.
     */
    @NotNull
    public String getLengthString() {
        return CafeBot.getGeneralHelper().formatTime(length);
    }

    /**
     * @return The search term for the {@link CustomSong}.
     */
    @NotNull
    public String getSearchString() {
        return "ytsearch:" + name + " by " + author;
    }

    /**
     * @return The person who requested the {@link CustomSong}.
     */
    @NotNull
    public User getRequester() {
        return requester;
    }

}
