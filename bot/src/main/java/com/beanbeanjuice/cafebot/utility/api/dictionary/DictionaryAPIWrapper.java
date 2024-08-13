package com.beanbeanjuice.cafebot.utility.api.dictionary;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class DictionaryAPIWrapper {

    private String DICTIONARY_API_URL = "https://api.dictionaryapi.dev/api/v2/entries/{language_code}/{word}";
    private final CafeBot cafeBot;

    public DictionaryAPIWrapper(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
    }

    public CompletableFuture<MessageEmbed> getDictionaryEmbed(final String word, final String languageCode) {
        DictionaryWord dictionaryWord = new DictionaryWord(word, languageCode);

        DICTIONARY_API_URL = DICTIONARY_API_URL.replace("{language_code}", languageCode);
        DICTIONARY_API_URL = DICTIONARY_API_URL.replace("{word}", word);

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder().setHeader("User-Agent", cafeBot.getBotUserAgent()).uri(URI.create(DICTIONARY_API_URL)).build();
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply((responseBody) -> this.dictionaryEmbed(responseBody, dictionaryWord));
        }
    }

    private MessageEmbed dictionaryEmbed(final String responseBody, DictionaryWord dictionaryWord) {
        parse(responseBody, dictionaryWord);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(dictionaryWord.getWord());
        StringBuilder descriptionBuilder = new StringBuilder();

        dictionaryWord.getAudioLink().ifPresent((link) -> descriptionBuilder.append("[Audio](").append(link.replace("//", "https://")).append(")\n"));

        for (int i = 0; i < dictionaryWord.getMeanings().size(); i++) {
            descriptionBuilder
                    .append("**Definition #").append(i+1).append("** (*")
                    .append(dictionaryWord.getMeanings().get(i).getPartOfSpeech().orElse(null)).append("*): ")
                    .append(dictionaryWord.getMeanings().get(i).getDefinition().orElse(null)).append("\n");

            dictionaryWord.getMeanings().get(i).getExample().ifPresent((example) -> descriptionBuilder.append("**Example**: ").append(example).append("\n"));

            descriptionBuilder.append("\n");
        }
        embedBuilder.setDescription(descriptionBuilder.toString());

        embedBuilder.setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    private void parse(String responseBody, DictionaryWord dictionaryWord) {
        try {
            ObjectMapper defaultObjectMapper = new ObjectMapper();
            JsonNode node = defaultObjectMapper.readTree(responseBody);

            try { dictionaryWord.setAudioLink(node.get(0).get("phonetics").get(0).get("audio").textValue()); }
            catch (NullPointerException ignored) { }

            for (JsonNode jsonNode : node.get(0).get("meanings")) {
                String partOfSpeech;
                try { partOfSpeech = jsonNode.get("partOfSpeech").textValue(); }
                catch (NullPointerException e) { partOfSpeech = null; }

                String definition;
                try { definition = jsonNode.get("definitions").get(0).get("definition").textValue(); }
                catch (NullPointerException e) { definition = null; }

                String example;
                try { example = jsonNode.get("definitions").get(0).get("example").textValue(); }
                catch (NullPointerException e) { example = null; }

                dictionaryWord.addMeaning(new DictionaryMeaning(partOfSpeech, definition, example));
            }
        } catch (JsonProcessingException e) {
            throw new CompletionException(e);
        }
    }

}
