package com.beanbeanjuice.utility.cafe.object;

import org.jetbrains.annotations.NotNull;

/**
 * A custom {@link ServeWord} class used for handling words from the database.
 *
 * @author beanbeanjuice
 */
public class ServeWord {

    private String word;
    private int uses;

    /**
     * Creates a new {@link ServeWord} object.
     * @param word The {@link String} word.
     * @param uses The amount of times the word has been used.
     */
    public ServeWord(@NotNull String word, @NotNull Integer uses) {
        this.word = word;
        this.uses = uses;
    }

    /**
     * @return The word for the {@link ServeWord}.
     */
    @NotNull
    public String getWord() {
        return word;
    }

    /**
     * @return The amount of uses for the {@link ServeWord}.
     */
    @NotNull
    public Integer getUses() {
        return uses;
    }

}
