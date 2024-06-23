package com.beanbeanjuice.cafeapi.wrapper.endpoints.words;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.CafeEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ResponseException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.UndefinedVariableException;
import com.beanbeanjuice.cafeapi.wrapper.requests.Request;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestBuilder;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestRoute;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestType;

import java.util.ArrayList;

/**
 * A class used to retrieve {@link WordsEndpoint} from the database.
 *
 * @author beanbeanjuice
 */
public class WordsEndpoint extends CafeEndpoint {

    /**
     * Gets an {@link ArrayList} of {@link Word} from the {@link com.beanbeanjuice.cafeapi.wrapper.CafeAPI CafeAPI}.
     * @return The {@link ArrayList} of {@link Word} from the {@link com.beanbeanjuice.cafeapi.wrapper.CafeAPI CafeAPI}.
     * @throws ResponseException Thrown when there is a generic server-side exception.
     */
    public ArrayList<Word> getAllWords() throws ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/words")
                .setAuthorization(apiKey)
                .build().orElseThrow();

        ArrayList<Word> wordList = new ArrayList<>();
        request.getData()
                .get("words")
                .forEach(word -> wordList.add(new Word(word.get("word").asText(), word.get("uses").asInt())));

        return wordList;
    }

    /**
     * Gets a {@link Word} in the {@link com.beanbeanjuice.cafeapi.wrapper.CafeAPI CafeAPI}.
     * @param word The {@link String word} to get.
     * @return The {@link Word} from the {@link com.beanbeanjuice.cafeapi.wrapper.CafeAPI CafeAPI}.
     * @throws NotFoundException Thrown when the word is not found.
     * @throws ResponseException Thrown when there is a generic server-side exception.
     */
    public Word getWord(final String word) throws NotFoundException, ResponseException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.GET)
                .setRoute("/words/" + word)
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return new Word(word, request.getData().get("word").get("uses").asInt());
    }

    /**
     * Updates a {@link Word} in the {@link com.beanbeanjuice.cafeapi.wrapper.CafeAPI CafeAPI}.
     * @param word The {@link String word} to be updated.
     * @param uses The new {@link Integer uses} to be set to.
     * @return True, if successful.
     * @throws NotFoundException Thrown when the word was not found.
     * @throws ResponseException Thrown when there is a generic server-side exception.
     * @throws AuthorizationException Thrown when the current API key is invalid.
     * @throws UndefinedVariableException Thrown when a variable is undefined.
     */
    public boolean updateWord(final String word, final int uses)
    throws NotFoundException, ResponseException, AuthorizationException, UndefinedVariableException {
        Request request = new RequestBuilder(RequestRoute.CAFEBOT, RequestType.PATCH)
                .setRoute("/words/" + word)
                .addParameter("uses", String.valueOf(uses))
                .setAuthorization(apiKey)
                .build().orElseThrow();

        return request.getStatusCode() == 200;
    }

}
