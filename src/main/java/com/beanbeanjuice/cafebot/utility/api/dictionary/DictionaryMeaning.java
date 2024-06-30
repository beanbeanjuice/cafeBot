package com.beanbeanjuice.cafebot.utility.api.dictionary;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DictionaryMeaning {

    @Nullable private final String partOfSpeech;
    @Nullable private final String definition;
    @Nullable private final String example;

    public DictionaryMeaning(@Nullable String partOfSpeech, @Nullable String definition, @Nullable String example) {
        this.partOfSpeech = partOfSpeech;
        this.definition = definition;
        this.example = example;
    }

    public Optional<String> getPartOfSpeech() {
        return Optional.ofNullable(partOfSpeech);
    }

    public Optional<String> getDefinition() {
        return Optional.ofNullable(definition);
    }

    public Optional<String> getExample() {
        return Optional.ofNullable(example);
    }



}
