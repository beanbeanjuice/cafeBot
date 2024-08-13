package com.beanbeanjuice.cafeapi.wrapper.endpoints.words;

import lombok.Getter;

public class Word {

    @Getter private final String word;
    @Getter private final int uses;

    /**
     * Creates a new {@link Word} object.
     * @param word The {@link String word}.
     * @param uses The amount of {@link Integer uses} the {@link Word} has.
     */
    public Word(final String word, final int uses) {
        this.word = word;
        this.uses = uses;
    }

}
