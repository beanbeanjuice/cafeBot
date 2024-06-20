package com.beanbeanjuice.cafebot.utility.api.dictionary;

import org.jetbrains.annotations.Nullable;

/**
 * A class used for a {@link DictionaryMeaning} in conjunction with the {@link DictionaryHelper}.
 *
 * @author beanbeanjuice
 */
// TODO: Convert to record?
public class DictionaryMeaning {

    private final String partOfSpeech;
    private final String definition;
    private final String example;

    /**
     * Creates a new {@link DictionaryMeaning} object.
     * @param partOfSpeech The part of speech for the word.
     * @param definition The definition for the part of speech.
     * @param example The example to be used with the part of speech.
     */
    public DictionaryMeaning(@Nullable String partOfSpeech, @Nullable String definition, @Nullable String example) {
        this.partOfSpeech = partOfSpeech;
        this.definition = definition;
        this.example = example;
    }

    /**
     * Retrieves the part of speech of the dictionary word.
     * @return Null, if error getting the part of speech.
     */
    @Nullable
    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    /**
     * Retrieves the definition of the dictionary word.
     * @return Null, if error getting the definition.
     */
    @Nullable
    public String getDefinition() {
        return definition;
    }

    /**
     * Retrieves the example of the dictionary word.
     * @return Null, if error getting the example.
     */
    @Nullable
    public String getExample() {
        return example;
    }

}
