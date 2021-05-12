package com.beanbeanjuice.utility.cafe;

import org.jetbrains.annotations.NotNull;

/**
 * A custom {@link ServeWord} class used for handling words from the database.
 *
 * @author beanbeanjuice
 */
public class ServeWord {

    private String word;
    private int uses;

    public ServeWord(@NotNull String word, @NotNull Integer uses) {
        this.word = word;
        this.uses = uses;
    }

    @NotNull
    public String getWord() {
        return word;
    }

    @NotNull
    public Integer getUses() {
        return uses;
    }

}
