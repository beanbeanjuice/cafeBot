package com.beanbeanjuice.cafeapi.wrapper.endpoints.words;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class WordsEndpoint extends CafeEndpoint {

    public CompletableFuture<ArrayList<Word>> getAllWords() {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/words")
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> {
                    ArrayList<Word> wordList = new ArrayList<>();
                    request.getData()
                            .get("words")
                            .forEach(word -> wordList.add(new Word(word.get("word").asText(), word.get("uses").asInt())));

                    return wordList;
                });
    }

    public CompletableFuture<Word> getWord(final String word) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/words/" + word)
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> new Word(word, request.getData().get("word").get("uses").asInt()));
    }

    public CompletableFuture<Boolean> updateWord(final String word, final int uses) {
        return RequestBuilder.create(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/words/" + word)
                .addParameter("uses", String.valueOf(uses))
                .setAuthorization(apiKey)
                .buildAsync()
                .thenApplyAsync((request) -> request.getStatusCode() == 200);
    }

}
