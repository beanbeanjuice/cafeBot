package com.beanbeanjuice.cafebot.utility.api.dictionary;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public class DictionaryWord {

    @Getter private final String word;
    @Getter private final String languageCode;
    @Getter private final ArrayList<DictionaryMeaning> meanings;

    @Setter @Nullable private String audioLink;

    public DictionaryWord(final String word, final String languageCode) {
        this.word = word;
        this.languageCode = languageCode;
        this.meanings = new ArrayList<>();
    }

    public Optional<String> getAudioLink() {
        if (audioLink != null && audioLink.isBlank()) return Optional.empty();
        return Optional.ofNullable(audioLink);
    }

    public void addMeaning(final DictionaryMeaning meaning) {
        meanings.add(meaning);
    }

}
