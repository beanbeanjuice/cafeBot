package com.beanbeanjuice.meme.api;

import com.beanbeanjuice.meme.api.wrapper.MemeAPI;
import com.beanbeanjuice.meme.api.wrapper.objects.RedditMeme;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class MemeApiTest {

    @Test
    @DisplayName("can get coffee meme")
    public void testCanGetCoffeeMeme() throws ExecutionException, InterruptedException {
        RedditMeme meme = MemeAPI.get("coffeeporn").get();

        Assertions.assertNotNull(meme.getTitle());
        Assertions.assertNotNull(meme.getSubreddit());
        Assertions.assertNotNull(meme.getUrl());
        Assertions.assertNotNull(meme.getImageUrl());
        Assertions.assertFalse(meme.isNsfw());
        Assertions.assertNotNull(meme.getAuthor());
    }

    @Test
    @DisplayName("should cause error when subreddit does not exist")
    public void testShouldCauseErrorForInvalidSubreddit() {
        CompletableFuture<RedditMeme> memeFuture = MemeAPI.get("thisisnotasubredditlmao");

        Assertions.assertThrows(
                CompletionException.class,
                memeFuture::join
        );
    }

}
